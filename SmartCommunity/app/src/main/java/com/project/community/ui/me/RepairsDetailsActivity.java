package com.project.community.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.RepairsDetailsBean;
import com.project.community.constants.AppConstants;
import com.project.community.model.CommentModel;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.RepairsDetailsAdapter;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/25 17:50
 * em：617716355@qq.com
 */
public class RepairsDetailsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_evaluate)
    TextView tvEvaluate;
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
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.rv_data)
    RecyclerView rvData;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_tv_times)
    TextView tvTvTimes;
    @Bind(R.id.tv_orderType)
    TextView tvOrderType;
    @Bind(R.id.tv_orderNo)
    TextView tvOrderNo;
    @Bind(R.id.tv_roomNo)
    TextView tvRoomNo;
    @Bind(R.id.tv_roomAddress)
    TextView tvRoomAddress;
    @Bind(R.id.tv_order_types)
    TextView tvOrderTypes;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_bespeak_date)
    TextView tvBespeakDate;
    @Bind(R.id.tv_order_typess)
    TextView tvOrderTypess;
    @Bind(R.id.tv_type)
    TextView tvType;


    private RepairsDetailsAdapter repairsDetailsAdapter;
    private List<RepairsDetailsBean.ProcessBean> mData;


    private RepairsDetailsBean repairsDetailsBean;

    private String orderNo;

    public static void startActivity(Context context, String orderNo) {

        Intent intent = new Intent(context, RepairsDetailsActivity.class);

        intent.putExtra("orderNo", orderNo);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_details);
        ButterKnife.bind(this);
        setTitles();
        getData();
//        setAdapter();
    }




    /**
     * 获取订单详情
     */
    private void getData() {
        progressDialog.show();

        orderNo = getIntent().getStringExtra("orderNo");

        if (orderNo == null || orderNo.equals("")) {
            ToastUtil.showToast(this, "参数错误！");
            progressDialog.dismiss();
            return;
        }
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

        if (model.getOrder().getCreateDate() != null && model.getOrder().getCreateDate().length() >= 10)
            tvTime.setText(model.getOrder().getCreateDate().substring(0, 4));
        if (model.getOrder().getCreateDate() != null && model.getOrder().getCreateDate().length() >= 10)
            tvTvTimes.setText(model.getOrder().getCreateDate().substring(5, 10));


        tvOrderType.setText("【" + model.getOrder().getOrderType() + "】");
        tvOrderTypes.setText(model.getOrder().getOrderType());
        tvOrderTypess.setText("受理  (" + model.getOrder().getOrderType() + ")");
        tvContent.setText(model.getOrder().getContent());
        tvOrderNo.setText("订单号：" + orderNo);
        tvRoomNo.setText("房屋编号：" + model.getOrder().getRoomNo());
        tvRoomAddress.setText(model.getOrder().getRoomAddress());
        tvBespeakDate.setText(model.getOrder().getBespeakDate());


        if (model.getOrder().getRepairStatus() != null) {
            if (model.getOrder().getRepairStatus().equals("0")) {
                if (model.getOrder().getOrderStatus().equals("0")) {
                    //提交成功
                    tvType.setText("待派单");
                    tvEvaluate.setVisibility(View.VISIBLE);
                    tvEvaluate.setText("取消报修");
                } else if (model.getOrder().getOrderStatus().equals("1")) {
                    //后台指派订单给维修人员
                    tvType.setText("待服务");
                    tvEvaluate.setVisibility(View.GONE);
                } else if (model.getOrder().getOrderStatus().equals("2")) {
                    //维修人员点击处理订单订单变为服务中
                    tvType.setText("服务中");
                    tvEvaluate.setVisibility(View.GONE);
                } else if (model.getOrder().getOrderStatus().equals("3")) {
                    //维修人员提交处理过程提交
                    tvType.setText("已处理");
                    tvEvaluate.setVisibility(View.VISIBLE);
                    tvEvaluate.setText("完成");
                } else if (model.getOrder().getOrderStatus().equals("4")) {
                    //已完成
                    tvType.setText("已完成");
                    tvEvaluate.setVisibility(View.VISIBLE);
                    tvEvaluate.setText("去评价");

                } else if (model.getOrder().getOrderStatus().equals("5")) {
                    //已评价
                    tvType.setText("已评价");
                    tvEvaluate.setVisibility(View.GONE);
                    tvEvaluate.setText("去评价");
                }

            }else {
                tvType.setText("已取消");
                tvEvaluate.setVisibility(View.GONE);
                tvEvaluate.setText("去评价");
            }
        } else {

//            tvType.setText("已取消");
//            tvEvaluate.setVisibility(View.GONE);
//            tvEvaluate.setText("去评价");

        }

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
                    ImageBrowseActivity.startActivity(RepairsDetailsActivity.this, new ArrayList<String>(img));
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
                    ImageBrowseActivity.startActivity(RepairsDetailsActivity.this, new ArrayList<String>(img));
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
                    ImageBrowseActivity.startActivity(RepairsDetailsActivity.this, new ArrayList<String>(img));
                }
            });


        } else if (result.size() == 0) {
            bbsItemBigImg.setVisibility(View.GONE);
            bbsItemLlTwoImg.setVisibility(View.GONE);
            bbsItemLlThreeImg.setVisibility(View.GONE);
        }


        setAdapter();
    }
    private void setAdapter() {

        mData = new ArrayList<>();
        mData.addAll(repairsDetailsBean.getProcess());


        repairsDetailsAdapter = new RepairsDetailsAdapter(this, mData,repairsDetailsBean);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(repairsDetailsAdapter);

    }
    private void setTitles() {


        initToolBar(toolbar, tvTitle, true, "报修详情", R.mipmap.iv_back);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d70_dianhua1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                Intent intent = new Intent();
                intent.putExtra("type", "2");
                intent.setClass(this, PhoneDialogActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
