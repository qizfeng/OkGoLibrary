package com.project.community.ui.life.minsheng;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.GoodsModel;
import com.project.community.ui.adapter.MerchantCartPopwinAdapter;
import com.project.community.ui.adapter.MerchantDetailAdapter;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.view.SpacesItemDecoration;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

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
    private MenuItem menuItem;
    private View header;
    private RelativeLayout mLayoutNavigation;
    private MerchantDetailAdapter mAdapter;
    private List<GoodsModel> mData = new ArrayList<>();
    private MerchantCartPopwinAdapter mMerchantCartPopwinAdapter;
    private PopupWindow mPopupWindow;
    private List<GoodsModel> mCartData = new ArrayList<>();
    private int totalCount = 0;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MerchantDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_merchant_detail), R.mipmap.iv_back);
        initView();
        initData();
    }

    private void initView() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_merchant, null);
        mLayoutNavigation = header.findViewById(R.id.layout_navigation);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        // mRecyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            GoodsModel goodsModel = new GoodsModel();
            goodsModel.goodsCount = 0;
            goodsModel.goodsPrice = i + 1;
            goodsModel.parentPosition = i;
            goodsModel.partentId = i;
            mData.add(goodsModel);
        }

        mAdapter = new MerchantDetailAdapter(mData, new MerchantDetailAdapter.OnMerchantItemClickListener() {
            @Override
            public void onMinusClick(View view, TextView tv_count, int position) {
                position = position - 1;//去掉头部
                int count = Integer.parseInt(tv_count.getText().toString()) - 1;
                if (count > 0) {
                    tv_count.setText("" + count);
                    for (int j = 0; j < mCartData.size(); j++) {
                        if (mAdapter.getData().get(position).partentId == mCartData.get(j).childId) {
                            mCartData.get(j).goodsCount = count;
                        }
                    }
                    mData.get(position).goodsCount = count;
                } else {

                    if (count == 0) {
                        for (int j = 0; j < mCartData.size(); j++) {
                            if (mAdapter.getData().get(position).partentId == mCartData.get(j).childId) {
                                mCartData.remove(j);
                            }
                        }
                    }
                    mData.get(position).goodsCount = 0;
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

            @Override
            public void onAddClick(View view, TextView tv_count, ImageView iv_minus, int position) {
                int count = Integer.parseInt(tv_count.getText().toString()) + 1;
                position = position - 1;//去除头部
                tv_count.setText("" + count);
                tv_count.setVisibility(View.VISIBLE);
                iv_minus.setVisibility(View.VISIBLE);
                mTvCount.setVisibility(View.VISIBLE);
                mData.get(position).goodsCount = count;
                if (mCartData.size() == 0 || mCartData == null) {
                    mCartData = new ArrayList<>();
                    GoodsModel goodsModel = new GoodsModel();
                    goodsModel.goodsCount = count;
                    goodsModel.goodsPrice = mAdapter.getData().get(position).goodsPrice;
                    goodsModel.partentId = mAdapter.getData().get(position).partentId;
                    goodsModel.childId = mAdapter.getData().get(position).partentId;
                    mCartData.add(goodsModel);
                } else {
                    boolean hasChild = false;
                    int hasPosition = 0;
                    for (int i = 0; i < mCartData.size(); i++) {
                        if (mAdapter.getData().get(position).partentId == mCartData.get(i).childId) {
                            hasChild = true;
                            hasPosition = i;
                            break;
                        }
                    }
                    if (hasChild) {
                        mCartData.get(hasPosition).goodsCount = count;
                        mCartData.get(hasPosition).goodsPrice = mAdapter.getData().get(position).goodsPrice;
                    } else {
                        GoodsModel goodsModel = new GoodsModel();
                        goodsModel.goodsCount = count;
                        goodsModel.goodsPrice = mAdapter.getData().get(position).goodsPrice;
                        goodsModel.partentId = mAdapter.getData().get(position).partentId;
                        goodsModel.childId = mAdapter.getData().get(position).partentId;
                        mCartData.add(goodsModel);

                    }
                }
                totalCount++;
                mTvCount.setText("" + totalCount);
            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(header);
    }

    @OnClick(R.id.layout_shoppingcart)
    public void onCartClick(View view) {
        if (mCartData.size() > 0)
            showCartPopwin(view);
    }

    public void onNavigationClick(View view) {
        Intent intent = new Intent(this, MerchantNavigationActivity.class);
        startActivity(intent);
    }


    /**
     * 弹出评论列表
     *
     * @param parent
     */
    private void showCartPopwin(final View parent) {
        if (mCartData == null)
            mCartData = new ArrayList<>();


        //填充对话框的布局
        final View inflate = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_merchant_cart, null);
        RecyclerView lv_container = inflate.findViewById(R.id.lv_container);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        lv_container.setLayoutManager(new LinearLayoutManager(this));
        lv_container.addItemDecoration(decoration);
        TextView tv_clear = inflate.findViewById(R.id.tv_clear);
        LinearLayout pop_layout = inflate.findViewById(R.id.pop_layout);

        mMerchantCartPopwinAdapter = new MerchantCartPopwinAdapter(mCartData, new MerchantCartPopwinAdapter.OnMerchantCartItemClickListener() {
            @Override
            public void onMinusClick(View view, TextView tv_count, TextView tv_price, int position) {
                int count = Integer.parseInt(tv_count.getText().toString()) - 1;
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
                    tv_price.setText("¥" + mMerchantCartPopwinAdapter.getData().get(position).goodsPrice * count);
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
                        if (mPopupWindow != null)
                            mPopupWindow.dismiss();
                        if (mMerchantCartPopwinAdapter.getData().size() == 0) {
                            return;
                        }
                        mPopupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT
                                , ViewGroup.LayoutParams.WRAP_CONTENT);
                        // 设置弹出窗体的背景
                        mPopupWindow.setBackgroundDrawable(dw);
                        // 设置弹出窗体显示时的动画，从底部向上弹出
                        inflate.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        mPopupWindow.setFocusable(true);
                        int height = inflate.getMeasuredHeight();
                        int[] location = new int[2];
                        parent.getLocationInWindow(location);
                        mPopupWindow.showAtLocation(parent, Gravity.TOP, ScreenUtils.getScreenWidth(MerchantDetailActivity.this), location[1] - height);

                    }
                }
            }

            @Override
            public void onAddClick(View view, TextView tv_count, ImageView iv_minus, TextView tv_price, int position) {
                int count = Integer.parseInt(tv_count.getText().toString()) + 1;
                tv_count.setText("" + count);
                tv_price.setText("¥" + mMerchantCartPopwinAdapter.getData().get(position).goodsPrice * count);
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
        mMerchantCartPopwinAdapter.bindToRecyclerView(lv_container);
        lv_container.setAdapter(mMerchantCartPopwinAdapter);
        if (mCartData.size() == 0) {
            mMerchantCartPopwinAdapter.setNewData(null);
            mMerchantCartPopwinAdapter.setEmptyView(R.layout.empty_view);
            TextView textView = (TextView) mMerchantCartPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
            textView.setText(getString(R.string.empty_no_comment));
        }
        tv_clear.setOnClickListener(new View.OnClickListener() {
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
                mPopupWindow.dismiss();
            }
        });

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        inflate.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mCartData.size() > 5) {
            lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(this) * 0.5);
            inflate.measure(ViewGroup.LayoutParams.MATCH_PARENT,(int) (ScreenUtils.getScreenHeight(this) * 0.5));
            LogUtils.e(">"+lv_container.getLayoutParams().height);
        }
        mPopupWindow.setFocusable(true);
        int height = inflate.getMeasuredHeight();
        int[] location = new int[2];
        parent.getLocationInWindow(location);
//        pop_layout.setMinimumHeight(ScreenUtils.getScreenHeight(this)-height);
        mPopupWindow.showAtLocation(parent, Gravity.TOP, ScreenUtils.getScreenWidth(this), location[1] - height);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
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
}
