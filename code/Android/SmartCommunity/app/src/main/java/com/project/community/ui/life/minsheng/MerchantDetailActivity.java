package com.project.community.ui.life.minsheng;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.GoodsModel;
import com.project.community.model.MerchantDeailModel;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.MerchantCartPopwinAdapter;
import com.project.community.ui.adapter.MerchantDetailAdapter;
import com.project.community.ui.me.MyActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ScreenUtils;
import com.project.community.view.SpacesItemDecoration;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/10/17.
 * 商店详情
 */

public class MerchantDetailActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.layout_shoppingcart)
    LinearLayout mLayoutShoppingCart;
    @Bind(R.id.layout_bottom)
    LinearLayout mLayoutBottom;
    @Bind(R.id.tv_count)
    TextView mTvCount;

    ImageView headeCover;
    TextView headeTvMerchantName;
    TextView headeTvState;
    TextView headeTvStatus;
    RatingBar headeRatingBar;
    TextView headeTvPhone;
    ImageView headeIvCall;
    TextView headeTvAddress;
    TextView headeTvDistance;
    TextView tvNavigation;

    private MenuItem menuItem;
    private View header;
    private RelativeLayout mLayoutNavigation;
    private MerchantDetailAdapter mAdapter;
    private List<GoodsModel> mData = new ArrayList<>();
    private MerchantCartPopwinAdapter mMerchantCartPopwinAdapter;
    private PopupWindow mPopupWindow;
    private List<GoodsModel> mCartData = new ArrayList<>();
    private int totalCount = 0;

    private String mShopId;


    private String merchant_id;
    private String merchant_distance;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MerchantDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_merchant_detail), R.mipmap.iv_back);
        initView();
        initData();
    }

    private void initView() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_merchant, null);

        headeCover=header.findViewById(R.id.heade_cover);
        headeTvMerchantName=header.findViewById(R.id.heade_tv_merchant_name);
        headeTvState=header.findViewById(R.id.heade_tv_state);
        headeTvStatus=header.findViewById(R.id.heade_tv_status);
        headeRatingBar=header.findViewById(R.id.heade_ratingBar);
        headeTvPhone=header.findViewById(R.id.heade_tv_phone);
        headeIvCall=header.findViewById(R.id.heade_iv_call);
        headeTvAddress=header.findViewById(R.id.heade_tv_address);
        headeTvDistance=header.findViewById(R.id.heade_tv_distance);
        tvNavigation=header.findViewById(R.id.tv_navigation);
//        mLayoutNavigation = header.findViewById(R.id.layout_navigation);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        // mRecyclerView.addItemDecoration(decoration);
    }

    private void initData() {
//        for (int i = 0; i < 10; i++) {
//            GoodsModel goodsModel = new GoodsModel();
//            goodsModel.goodsCount = 0;
//            goodsModel.goodsPrice = i + 1;
//            goodsModel.parentPosition = i;
//            goodsModel.partentId = i;
//            mData.add(goodsModel);
//        }

        mAdapter = new MerchantDetailAdapter(mData, new MerchantDetailAdapter.OnMerchantItemClickListener() {
            @Override
            public void onMinusClick(final View view, final TextView tv_count, int position) {
                final int finalPosition = position - 1;//去掉头部
                final int count = Integer.parseInt(tv_count.getText().toString()) - 1;

                addCart(mShopId,count, mData.get(finalPosition).goodId, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        if (count > 0) {
                            tv_count.setText("" + count);
                            for (int j = 0; j < mCartData.size(); j++) {
                                if (mAdapter.getData().get(finalPosition).partentId == mCartData.get(j).childId) {
                                    mCartData.get(j).goodsCount = count;
                                }
                            }
                            mData.get(finalPosition).goodsCount = count;
                        } else {

                            if (count == 0) {
                                for (int j = 0; j < mCartData.size(); j++) {
                                    if (mAdapter.getData().get(finalPosition).partentId == mCartData.get(j).childId) {
                                        mCartData.remove(j);
                                    }
                                }
                            }
                            mData.get(finalPosition).goodsCount = 0;
                            tv_count.setText("0");
                            tv_count.setVisibility(View.INVISIBLE);
                            view.setVisibility(View.INVISIBLE);
                        }

                        totalCount--;
                        if (totalCount > 0) {
                            mTvCount.setText("" + totalCount);
                        } else {
                            totalCount = 0;
                            mTvCount.setVisibility(View.GONE);
                        }
                    }
                });

            }

            @Override
            public void onAddClick(View view, final TextView tv_count, final ImageView iv_minus, int position) {
                final int count = Integer.parseInt(tv_count.getText().toString()) + 1;
                final int finalPosition = position - 1;//去除头部

                addCart(mShopId,count, mData.get(finalPosition).goodId, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        tv_count.setText("" + count);
                        tv_count.setVisibility(View.VISIBLE);
                        iv_minus.setVisibility(View.VISIBLE);
                        mTvCount.setVisibility(View.VISIBLE);
                        mData.get(finalPosition).goodsCount = count;
                        if (mCartData.size() == 0 || mCartData == null) {
//                            mCartData = new ArrayList<>();
                            GoodsModel goodsModel = new GoodsModel();
                            goodsModel.goodsCount = count;
                            goodsModel.goodsPrice = mAdapter.getData().get(finalPosition).goodsPrice;
                            goodsModel.partentId = mAdapter.getData().get(finalPosition).partentId;
                            goodsModel.childId = mAdapter.getData().get(finalPosition).partentId;
                            goodsModel.goodId = mAdapter.getData().get(finalPosition).goodId;
                            mCartData.add(goodsModel);
                        } else {
                            boolean hasChild = false;
                            int hasPosition = 0;
                            for (int i = 0; i < mCartData.size(); i++) {
                                if (mAdapter.getData().get(finalPosition).partentId == mCartData.get(i).childId) {
                                    hasChild = true;
                                    hasPosition = i;
                                    break;
                                }
                            }
                            if (hasChild) {
                                mCartData.get(hasPosition).goodsCount = count;
                                mCartData.get(hasPosition).goodsPrice = mAdapter.getData().get(finalPosition).goodsPrice;
                            } else {
                                GoodsModel goodsModel = new GoodsModel();
                                goodsModel.goodsCount = count;
                                goodsModel.goodsPrice = mAdapter.getData().get(finalPosition).goodsPrice;
                                goodsModel.partentId = mAdapter.getData().get(finalPosition).partentId;
                                goodsModel.childId = mAdapter.getData().get(finalPosition).partentId;
                                goodsModel.goodId = mAdapter.getData().get(finalPosition).goodId;
                                mCartData.add(goodsModel);

                            }
                        }
                        totalCount++;
                        mTvCount.setText("" + totalCount);
                    }
                });


            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(header);


        lv_container.setLayoutManager(new LinearLayoutManager(this));
        lv_container.addItemDecoration(new SpacesItemDecoration(1, true));

        mMerchantCartPopwinAdapter = new MerchantCartPopwinAdapter(mCartData, new MerchantCartPopwinAdapter.OnMerchantCartItemClickListener() {
            @Override
            public void onMinusClick(View view, final TextView tv_count, final TextView tv_price, final int position) {
                final int count = Integer.parseInt(tv_count.getText().toString()) - 1;

                addCart(mShopId,count, mCartData.get(position).goodId, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        totalCount--;
                        if (totalCount > 0) {
                            mTvCount.setVisibility(View.VISIBLE);
                            mTvCount.setText("" + totalCount);
                        } else if (totalCount <= 0) {
                            totalCount = 0;
                            mTvCount.setVisibility(View.GONE);
                        }
                        if (count >= 0) {
                            tv_count.setText("" + count);
//                    tv_price.setText("¥" + mMerchantCartPopwinAdapter.getData().get(position).goodsPrice * count);
                            tv_price.setText("¥" + new BigDecimal(mMerchantCartPopwinAdapter.getData().get(position).goodsPrice).multiply(new BigDecimal(count)).toString());
                            mCartData.get(position).goodsCount = count;
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).partentId == mMerchantCartPopwinAdapter.getData().get(position).childId) {
                                    mData.get(i).goodsCount = count;
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            if (count == 0) {
                                mCartData.get(position).goodsCount = 0;
                                mMerchantCartPopwinAdapter.remove(position);
                                // 实例化一个ColorDrawable颜色为半透明
                                ColorDrawable dw = new ColorDrawable(0xb0000000);
                                if (mMerchantCartPopwinAdapter.getData().size() == 0) {
                                    return;
                                }
                                mPopLayout.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });

            }

            @Override
            public void onAddClick(View view, final TextView tv_count, final ImageView iv_minus, final TextView tv_price, final int position) {
                final int count = Integer.parseInt(tv_count.getText().toString()) + 1;

                addCart(mShopId,count, mCartData.get(position).goodId, "", new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        tv_count.setText("" + count);
//                tv_price.setText("¥" + mMerchantCartPopwinAdapter.getData().get(position).goodsPrice * count);
                        tv_price.setText("¥" + new BigDecimal(mMerchantCartPopwinAdapter.getData().get(position).goodsPrice).multiply(new BigDecimal(count)).toString());
                        tv_count.setVisibility(View.VISIBLE);
                        iv_minus.setVisibility(View.VISIBLE);
                        mTvCount.setVisibility(View.VISIBLE);
                        mCartData.get(position).goodsCount = count;
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).partentId == mMerchantCartPopwinAdapter.getData().get(position).childId) {
                                mData.get(i).goodsCount = count;
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        totalCount++;
                        mTvCount.setText("" + totalCount);

                    }
                });


            }
        });
        mMerchantCartPopwinAdapter.bindToRecyclerView(lv_container);
        lv_container.setAdapter(mMerchantCartPopwinAdapter);

        merchant_id=getIntent().getExtras().getString("merchant_id");
        merchant_distance=getIntent().getExtras().getString("merchant_distance");
        getData(merchant_id);
    }

    @OnClick(R.id.layout_shoppingcart)
    public void onCartClick(View view) {
        if (mCartData.size() > 0)
            showCartPopwin(view);
    }

    /**
     * 导航
     * @param view
     */
    public void onNavigationClick(View view) {
        Intent intent = new Intent(this, MerchantNavigationActivity.class);
        startActivity(intent);
    }

    /**
     * 打电话
     * @param view
     */
    public void onCallClick(View view) {
//        Intent intent= new Intent();
//        intent.putExtra("hasHeader", false);
//        intent.putExtra("type", "1");
//        intent.setClass(MerchantDetailActivity.this, PhoneDialogActivity.class);
//        startActivity(intent);
        onCall();
    }

    public static final int REQUEST_PERMISSION_CODE = 123;

    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;

    private static final String[] requestPermissions = {
            PERMISSION_CALL_PHONE,
    };

    private void onCall(){
        if (TextUtils.isEmpty(headeTvPhone.getText().toString())){
            ToastUtils.showShortToast(this, R.string.network_error);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + headeTvPhone.getText().toString().trim()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MerchantDetailActivity.this, PERMISSION_CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 没有权限，申请权限。
                ActivityCompat.requestPermissions(MerchantDetailActivity.this, requestPermissions, REQUEST_PERMISSION_CODE);
            } else {
                // 有权限了，去放肆吧。
                startActivity(intent);
            }
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(MerchantDetailActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限，申请权限。
                    ToastUtils.showShortToast(MerchantDetailActivity.this, getString(R.string.permission_tips));
                } else {
//                    if (grantResults.length > 0
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + headeTvPhone.getText().toString().trim()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return;
        }
    }



    @Bind(R.id.pop_layout)
    RelativeLayout mPopLayout;
    @Bind(R.id.tv_clear)
    TextView mTvClear;
    @Bind(R.id.lv_container)
    RecyclerView lv_container;

    /**
     * 弹窗购物车
     *
     * @param parent
     */
    private void showCartPopwin(final View parent) {
        if (mPopLayout.getVisibility() == View.VISIBLE) {
            mPopLayout.setVisibility(View.GONE);
            return;
        } else {
            mPopLayout.setVisibility(View.VISIBLE);
        }
        if (mCartData == null)
            mCartData = new ArrayList<>();
        if (mCartData.size() > 5) {
            lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(this) * 0.5);
        }
        mMerchantCartPopwinAdapter.setNewData(mCartData);
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).goodsCount = 0;
                    mAdapter.notifyDataSetChanged();
                }
                mCartData.clear();
                mMerchantCartPopwinAdapter.removeAllData();
                totalCount = 0;
                mTvCount.setVisibility(View.GONE);
                mPopLayout.setVisibility(View.GONE);
            }
        });

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopLayout.setBackground(dw);
        mPopLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopLayout.setVisibility(View.GONE);
                return false;
            }
        });
    }


    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPopLayout.getVisibility() == View.VISIBLE) {
                mPopLayout.setVisibility(View.GONE);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        menuItem = menu.findItem(R.id.navigation_collect);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_collect:
                return true;
            case R.id.navigation_share:
                UMWeb web = new UMWeb("http://www.baidu.com");
                web.setTitle("玉林酒店");//标题
                web.setThumb(new UMImage(this, R.mipmap.ic_launcher_round));  //缩略图
                web.setDescription("回锅肉好吃");//描述
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                config.setTitleText("");
                config.setShareboardBackgroundColor(0xffffffff);
                config.setIndicatorVisibility(false);
                new ShareAction(this)
                        .withText("回锅肉好吃")
                        .withMedia(new UMImage(this, R.mipmap.ic_launcher_round))
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener)
                        .open(config);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 获取商家详情
     */

    private void getData(String shopId) {
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }
        serverDao.getShopByUser(
                getUser(this).id,
                shopId,
                new JsonCallback<BaseResponse<MerchantDeailModel>>() {
                    @Override
                    public void onSuccess(BaseResponse<MerchantDeailModel> listBaseResponse, Call call, Response response) {
                        dismissDialog();

                        if (listBaseResponse.retData.cartTotal>0){
                            mTvCount.setText(listBaseResponse.retData.cartTotal+"");
                            mTvCount.setVisibility(View.VISIBLE);
                        }else {
                            mTvCount.setVisibility(View.GONE);
                        }
                        mShopId=listBaseResponse.retData.shop.id;

                        new GlideImageLoader().onDisplayImageWithDefault(MerchantDetailActivity.this,headeCover, AppConstants.HOST + listBaseResponse.retData.shop.shopPhoto,R.mipmap.c1_image2);
                        headeTvMerchantName.setText(listBaseResponse.retData.shop.shopsName);
//                        headeTvState.setText(listBaseResponse.retData.shop.shopsName);
                        headeTvPhone.setText(listBaseResponse.retData.shop.contactPhone);
                        headeTvAddress.setText(listBaseResponse.retData.shop.businessAddress);
                        headeTvDistance.setText("距离"+merchant_distance+"KM");
                        if (listBaseResponse.retData.shop.isOpen==1)
                            headeTvStatus.setVisibility(View.INVISIBLE);
                        headeRatingBar.setStar(listBaseResponse.retData.shop.starLevel);


                        mData.clear();
                        mData.addAll(listBaseResponse.retData.goodsList);
                        for (int i = 0; i < mData.size(); i++) {
                            mData.get(i).goodsPrice= mData.get(i).price;
                            mData.get(i).goodsCount= mData.get(i).cartNum;
                            mData.get(i).parentPosition= i;
                            mData.get(i).partentId= i;
                            if (mData.get(i).cartNum>0){
                                Log.e("onSuccess---->","---?" );
                                mCartData.add(mData.get(i));
                                mPopLayout.setVisibility(View.VISIBLE);
                            }
                            Log.e("onSuccess---->","---aaa" );

                        }
                        mAdapter.notifyDataSetChanged();
                        //        for (int i = 0; i < 10; i++) {
//            GoodsModel goodsModel = new GoodsModel();
//            goodsModel.goodsCount = 0;
//            goodsModel.goodsPrice = i + 1;
//            goodsModel.parentPosition = i;
//            goodsModel.partentId = i;
//            mData.add(goodsModel);
//        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }



    /**
     * D27添加购物车
     */

    private void addCart(String shopId,int num,String goodId,String id,JsonCallback<BaseResponse<List>> baseResponseJsonCallback) {
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }
        serverDao.addCart(
                getUser(this).id,
                num,
                shopId,
                goodId,
                id,
                baseResponseJsonCallback);
//                new JsonCallback<BaseResponse<List>>() {
//                    @Override
//                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
//                        dismissDialog();
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        dismissDialog();
//                        showToast(e.getMessage());
//                    }
//                });
    }



}
