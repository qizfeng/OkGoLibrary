package com.project.community.ui.me;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.photo.PhotoUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.RepairsDetailsBean;
import com.project.community.constants.AppConstants;
import com.project.community.model.CommentModel;
import com.project.community.model.FileUploadModel;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.ArticleDetailsImagsAdapter;
import com.project.community.ui.adapter.OrderDetailShouliApdater;
import com.project.community.ui.adapter.RepairsDetailsAdapter;
import com.project.community.ui.adapter.SendMessageAdapter;
import com.project.community.ui.life.minsheng.SendMessageActivity;
import com.project.community.util.ListAdapterUtils;
import com.project.community.util.ScreenUtils;
import com.project.community.util.StringUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.MyGridView;
import com.project.community.view.crop.CropImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by cj on 17/10/24
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.order_detail_address)
    TextView orderDetailAddress;
    @Bind(R.id.order_detail_name)
    TextView orderDetailName;
    @Bind(R.id.order_detail_type)
    TextView orderDetailType;
    @Bind(R.id.order_detail_content)
    TextView orderDetailContent;
    @Bind(R.id.bbs_item_big_img)
    ImageView bbsItemBigImg;
    @Bind(R.id.bbs_item_ll_two_img_1)
    ImageView bbsItemLlTwoImg1;
    @Bind(R.id.bbs_item_ll_two_img_2)
    ImageView bbsItemLlTwoImg2;
    @Bind(R.id.bbs_item_ll_two_img)
    LinearLayout bbsItemLlTwoImg;
    @Bind(R.id.bbs_item_ll_three_img_1)
    ImageView bbsItemLlThreeImg1;
    @Bind(R.id.bbs_item_ll_three_img_2)
    ImageView bbsItemLlThreeImg2;
    @Bind(R.id.bbs_item_ll_three_img_3)
    ImageView bbsItemLlThreeImg3;
    @Bind(R.id.bbs_item_ll_three_img)
    LinearLayout bbsItemLlThreeImg;
    @Bind(R.id.order_detail_time)
    TextView orderDetailTime;
    @Bind(R.id.order_detail_jiaoyi_danhao)
    TextView orderDetailJiaoyiDanhao;
    @Bind(R.id.order_detail_xiadan_time)
    TextView orderDetailXiadanTime;
    @Bind(R.id.order_detail_recylerview)
    RecyclerView orderDetailRecylerview;
    @Bind(R.id.order_detail_et_dispose)
    EditText orderDetailEtDispose;
    @Bind(R.id.order_detail_add_imgs)
    MyGridView orderDetailAddImgs;
    @Bind(R.id.order_detail_complete)
    Button orderDetailComplete;
    @Bind(R.id.layout_root)
    CoordinatorLayout layoutRoot;
    @Bind(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private List<String> mImages = new ArrayList<>();
    private List<String> mAddImages = new ArrayList<>();
    private List<CommentModel> mShouliList = new ArrayList<>();
    ArticleDetailsImagsAdapter grid_photoAdapter;
    SendMessageAdapter grid_photoAdapterAdd;
    OrderDetailShouliApdater orderDetailShouliApdater;
    ArrayList<String> imgs = new ArrayList();

    private MenuItem menuItem;


    private String orderNo;


    private RepairsDetailsBean repairsDetailsBean;


    private RepairsDetailsAdapter repairsDetailsAdapter;
    private List<RepairsDetailsBean.ProcessBean> mData;


    public static void startActivity(Context context, String orderNo) {

        Intent intent = new Intent(context, OrderDetailActivity.class);

        intent.putExtra("orderNo", orderNo);

        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        orderNo = getIntent().getStringExtra("orderNo");


        if (orderNo == null) {
            ToastUtil.showToast(this, "参数错误");
        } else
            getData();

        initToolBar(toolbar, tvTitle, true, getString(R.string.order_detail), R.mipmap.iv_back);
//        for (int i = 0; i < 6; i++) {
//            mImages.add("");
////            mAddImages.add("");
//        }
//        for (int i = 0; i < 10; i++) {
//            CommentModel commentModel = new CommentModel();
//            if (i == 6) {
//                commentModel.id = "1";
//            } else {
//                commentModel.id = "0";
//            }
//            mShouliList.add(commentModel);
//        }
//        imgs.add("");
//        orderDetailShouliApdater = new OrderDetailShouliApdater(mShouliList, new RecycleItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onCustomClick(View view, int position) {
//
//            }
//        });
////        orderDetailRecylerview.setItemAnimator(new DefaultItemAnimator());
//        orderDetailRecylerview.setLayoutManager(new LinearLayoutManager(this));
////        SpacesItemDecoration decoration = new SpacesItemDecoration(0, false);
////        orderDetailRecylerview.addItemDecoration(decoration);
//        orderDetailRecylerview.setAdapter(orderDetailShouliApdater);
//
//        grid_photoAdapter = new ArticleDetailsImagsAdapter(this, mImages);
//        grid_photoAdapterAdd = new SendMessageAdapter(mAddImages, this);
////        orderDetailImgs.setAdapter(grid_photoAdapter);
////        orderDetailImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                ImageBrowseActivity.startActivity(OrderDetailActivity.this, imgs);
////            }
////        });

        RxView.clicks(orderDetailComplete)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onViewClicked();
                    }
                });

    }


    /**
     * 获取订单详情
     */
    private void getData() {
        progressDialog.show();
        serverDao.getPropRepair(getUser(this).id, orderNo,
                new JsonCallback<BaseResponse<RepairsDetailsBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<RepairsDetailsBean> objectBaseResponse, Call call, Response response) {
                        progressDialog.dismiss();
                        if (objectBaseResponse.errNum.equals("0")) {
                            setView(objectBaseResponse.retData);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        progressDialog.dismiss();
                    }
                });


    }

    private void setView(RepairsDetailsBean model) {
        repairsDetailsBean = model;

        orderDetailAddress.setText(model.getOrder().getRoomAddress());
        orderDetailName.setText(model.getOrder().getUserName());
        orderDetailType.setText(model.getOrder().getOrderType());
        orderDetailContent.setText(model.getOrder().getContent());
        orderDetailTime.setText(model.getOrder().getBespeakDate());
        orderDetailJiaoyiDanhao.setText(orderNo);
        orderDetailXiadanTime.setText(model.getOrder().getCreateDate());

        //设置图片
        final List<String> result = Arrays.asList(model.getOrder().getImagesUrl().split(","));

        final List<String> img = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            img.add(AppConstants.HOST + result.get(i).toString());
        }

        if (result.size() == 1) {

            bbsItemBigImg.setVisibility(View.VISIBLE);
            bbsItemLlTwoImg.setVisibility(View.GONE);
            bbsItemLlThreeImg.setVisibility(View.GONE);

            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into(bbsItemBigImg);
            bbsItemBigImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(OrderDetailActivity.this, new ArrayList<String>(img));
                }
            });

        } else if (result.size() == 2) {
            bbsItemBigImg.setVisibility(View.GONE);
            bbsItemLlTwoImg.setVisibility(View.VISIBLE);
            bbsItemLlThreeImg.setVisibility(View.GONE);
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into(bbsItemLlTwoImg1);
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(1))
                    .into(bbsItemLlTwoImg2);
            bbsItemLlTwoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(OrderDetailActivity.this, new ArrayList<String>(img));
                }
            });


        } else if (result.size() == 3) {
            bbsItemBigImg.setVisibility(View.GONE);
            bbsItemLlTwoImg.setVisibility(View.GONE);
            bbsItemLlThreeImg.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(AppConstants.HOST + result.get(0))
                    .into(bbsItemLlThreeImg1);
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(1))
                    .into(bbsItemLlThreeImg2);
            Glide.with(this)
                    .load(AppConstants.HOST + result.get(2))
                    .into(bbsItemLlThreeImg3);
            bbsItemLlThreeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(OrderDetailActivity.this, new ArrayList<String>(img));
                }
            });


        } else if (result.size() == 0) {
            bbsItemBigImg.setVisibility(View.GONE);
            bbsItemLlTwoImg.setVisibility(View.GONE);
            bbsItemLlThreeImg.setVisibility(View.GONE);
        }


        if (repairsDetailsBean.getOrder().getOrderStatus().equals("2")) {
            llEvaluate.setVisibility(View.VISIBLE);
            orderDetailComplete.setVisibility(View.VISIBLE);
            for (int i = 0; i < 3; i++) {
                mImages.add("");
//            mAddImages.add("");
            }
            grid_photoAdapterAdd = new SendMessageAdapter(mAddImages, this);
//        orderDetailImgs.setAdapter(grid_photoAdapter);
//        orderDetailImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ImageBrowseActivity.startActivity(OrderDetailActivity.this, imgs);
//            }
//        });


            orderDetailAddImgs.setAdapter(grid_photoAdapterAdd);
            orderDetailAddImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mAddImages.size() >= 3) {
                        return;
                    }
                    if (i == grid_photoAdapterAdd.getCount() - 1) {
                        showPhotoDialog();
                    }
                }
            });
        } else if (repairsDetailsBean.getOrder().getOrderStatus().equals("3")) {
            llEvaluate.setVisibility(View.GONE);
            orderDetailComplete.setVisibility(View.GONE);
        } else {
            llEvaluate.setVisibility(View.GONE);
            orderDetailComplete.setVisibility(View.GONE);
        }


        setAdapter();
    }

    /**
     *
     */
    private void setAdapter() {
        mData = new ArrayList<>();
        mData.addAll(repairsDetailsBean.getProcess());


        repairsDetailsAdapter = new RepairsDetailsAdapter(this, mData, repairsDetailsBean);
        orderDetailRecylerview.setLayoutManager(new LinearLayoutManager(this));
        orderDetailRecylerview.setAdapter(repairsDetailsAdapter);


    }

    //    @OnClick(R.id.order_detail_complete)
    public void onViewClicked() {
       if (TextUtils.isEmpty(orderDetailEtDispose.getText().toString().trim())){
           ToastUtil.showToast(OrderDetailActivity.this,"请输入处理过程");
           return;
       }
       if (mAddImages.size()<=0){
           ToastUtil.showToast(OrderDetailActivity.this,"请选择图片");
           return;
       }

        if (mAddImages.size() == 1)
//            uploadFile(new File(mImags.get(0)));
            uploadFile(new File(StringUtils.getRealFilePath(OrderDetailActivity.this, Uri.parse(mAddImages.get(0)))));

        if (mAddImages.size() == 2)
            uploadFile(new File(StringUtils.getRealFilePath(OrderDetailActivity.this, Uri.parse(mAddImages.get(1)))));
        if (mAddImages.size() == 3)
            uploadFile(new File(StringUtils.getRealFilePath(OrderDetailActivity.this, Uri.parse(mAddImages.get(2)))));
//        }
    }

    private List<String> upImage = new ArrayList<String>();

    /**
     * 文件上传
     *
     * @param file
     */
    private void uploadFile(File file) {
        serverDao.uploadFile(file, new JsonCallback<BaseResponse<FileUploadModel>>() {
            @Override
            public void onSuccess(BaseResponse<FileUploadModel> baseResponse, Call call, Response response) {

                upImage.add(baseResponse.retData.filePath);

                if (upImage.size() < mAddImages.size()) {
                    onViewClicked();
                } else {

                    releaseEvaluate();

                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());

            }
        });
    }

    private void releaseEvaluate() {
        progressDialog.show();


        serverDao.replaySave(getUser(this).id, orderNo,
                ListAdapterUtils.toStr(",", upImage),
                orderDetailEtDispose.getText().toString().trim(),
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {

                        progressDialog.dismiss();


                        if (listBaseResponse.errNum.equals("0")) {
                            ToastUtil.showToast(OrderDetailActivity.this, "回复成功");
                            finish();
                        } else {
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                        Log.e("tag_f", e.getMessage().toString() + "");
                    }
                });


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
//        menuItem = menu.findItem(R.id.navigation_tel);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_tel:
                Intent intent = new Intent();
                intent.putExtra("hasHeader", false);
                intent.putExtra("type", "1");
                intent.setClass(OrderDetailActivity.this, PhoneDialogActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
//                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
//                    uploadFile(fileCropUri);//上传图片
                    CropImageActivity.startCrop(this, imageUri.toString(), output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        try {
                            cropImageUri = Uri.fromFile(fileCropUri);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(this, "com.project.community", new File(newUri.getPath()));
                            }
//                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                            CropImageActivity.startCrop(this, newUri.toString(), output_X, output_Y, CODE_RESULT_REQUEST);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast(getString(R.string.toast_error_photo));
                        }
//                        uploadFile(new File( Uri.parse(PhotoUtils.getPath(this, data.getData())).getPath()));
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        mAddImages.add(data.getStringExtra("uri"));
                        if (mAddImages.size() >= 3) {
                            grid_photoAdapterAdd.setGoneAdd(0);
                        } else {
                            grid_photoAdapterAdd.setGoneAdd(1);
                        }
                        grid_photoAdapterAdd.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showToast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.project.community", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                showToast("设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private static final int output_X = 480;
    private static final int output_Y = 480;
    private PopupWindow mPopupWindow;


    public void showPhotoDialog() {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_dialog_photo, null);
        TextView tv_take_photo = (TextView) inflate.findViewById(R.id.tv_take_photo);
        TextView tv_pick_photo = (TextView) inflate.findViewById(R.id.tv_pick_photo);
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_pick_photo.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        mPopupWindow = new PopupWindow(this);
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(bbsItemLlThreeImg, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainCameraPermission();
                break;
            case R.id.tv_pick_photo:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                autoObtainStoragePermission();
                break;
            case R.id.tv_cancel:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
        }
    }
}
