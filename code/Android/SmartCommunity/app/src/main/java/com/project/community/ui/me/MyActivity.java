package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.GetRepairBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.PhoneDialogActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/9/27.
 * 我的
 */

public class MyActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.my_name)
    TextView myName;
    @Bind(R.id.my_gonghao)
    TextView myGonghao;
    @Bind(R.id.my_jiedan)
    TextView myJiedan;
    @Bind(R.id.ratingBar)
    XLHRatingBar ratingBar;
    @Bind(R.id.layout_stars)
    LinearLayout layoutStars;
    @Bind(R.id.my_pingtai)
    TextView myPingtai;
    @Bind(R.id.rl_my_kefu)
    RelativeLayout rlMyKefu;
    @Bind(R.id.rl_my_fuwu)
    RelativeLayout rlMyFuwu;
    @Bind(R.id.rl_my_zhinan)
    RelativeLayout rlMyZhinan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(toolbar, tvTitle, true, "", R.mipmap.iv_back);

        getData();
    }


    /**
     * 获取数据
     */
    private void getData() {
        progressDialog.show();
        serverDao.getRepair(getUser(this).id, new JsonCallback<BaseResponse<GetRepairBean>>() {
            @Override
            public void onSuccess(BaseResponse<GetRepairBean> getRepairBeanBaseResponse, Call call, Response response) {

                progressDialog.dismiss();
                if (getRepairBeanBaseResponse.errNum.equals("0")) {

                    setView(getRepairBeanBaseResponse.retData);

                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                progressDialog.dismiss();
            }
        });


    }


    /**
     * 匹配数据
     */
    private void setView(GetRepairBean model) {

        Glide.with(this)
                .load(AppConstants.HOST + model.getMember().getPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivHeader);

        myName.setText(model.getMember().getName());

        myGonghao.setText("工号 " + model.getMember().getUserNo());

        myJiedan.setText("已接" + model.getMember().getOrderTotal() + "单");

        ratingBar.setCountSelected( model.getMember().getStarLevel());

        myPingtai.setText(model.getContact());

    }


    @OnClick({R.id.rl_my_kefu, R.id.rl_my_fuwu, R.id.rl_my_zhinan})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_my_kefu:
                intent = new Intent();
                intent.putExtra("hasHeader", false);
                intent.putExtra("type", "1");
                intent.setClass(MyActivity.this, PhoneDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_fuwu:
                intent = new Intent(this, ServiesClauseActivity.class);
                intent.putExtra("code", 1);
                startActivity(intent);
                break;
            case R.id.rl_my_zhinan:
                intent = new Intent(this, ServiesClauseActivity.class);
                intent.putExtra("code", 2);
                startActivity(intent);
                break;
        }
    }
}
