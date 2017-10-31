package com.project.community.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.ArticleIndexBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.ImageBrowseActivity;
import com.project.community.ui.life.minsheng.ArticleDetailsActivity;
import com.project.community.ui.life.minsheng.BBSActivity;
import com.project.community.ui.life.minsheng.SendMessageActivity;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/24 14:22
 * em：617716355@qq.com
 */
public class MyForumDetailsActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.bbs_item_head)
    ImageView bbsItemHead;
    @Bind(R.id.bbs_item_name)
    TextView bbsItemName;
    @Bind(R.id.bbs_item_time)
    TextView bbsItemTime;
    @Bind(R.id.bbs_item_comment)
    TextView bbsItemComment;
    @Bind(R.id.bbs_item_content)
    TextView bbsItemContent;
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
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.tv_anew)
    TextView tvAnew;
    @Bind(R.id.tv_audit_content)
    TextView tvAuditContent;


    private ArticleIndexBean model;

    /**
     * @param context
     * @param articleIndexBean 帖子详情
     */
    public static void startActivity(Context context, ArticleIndexBean articleIndexBean) {

        Intent intent = new Intent(context, MyForumDetailsActivity.class);

        intent.putExtra("articleIndexBean", articleIndexBean);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forum_details);
        ButterKnife.bind(this);
        setTitles();
    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "信息详情", R.mipmap.iv_back);

        model = (ArticleIndexBean) getIntent().getSerializableExtra("articleIndexBean");

        if (model == null) {
            ToastUtil.showToast(this, "参数错误");
        } else {
            setView();
        }
    }

    private void setView() {


        bbsItemName.setText(model.getUserName() + "");
        bbsItemTime.setText(model.getCreateDate() + "");
        bbsItemContent.setText(model.getContent() + "");
        bbsItemComment.setText(model.getComments() + "评论");

        Glide.with(this)
                .load(AppConstants.HOST + model.getUserPhoto())
                .placeholder(R.mipmap.d54_tx)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(bbsItemHead);


        if (model.getIsShow() == 0) {
            tvAuditContent.setVisibility(View.INVISIBLE);
            tvAnew.setVisibility(View.VISIBLE);
        } else {
            tvAuditContent.setVisibility(View.VISIBLE);
            tvAuditContent.setText("审核未通过原因：" + model.getAudit_content());
            tvAnew.setVisibility(View.VISIBLE);
        }


        List<String> result = Arrays.asList(model.getImagesUrl().split(","));
        final List<String>  img=new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            img.add(AppConstants.HOST +result.get(i).toString());
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
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
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
            bbsItemLlTwoImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
                }
            });
            bbsItemLlTwoImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
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
            bbsItemLlThreeImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
                }
            });
            bbsItemLlThreeImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
                }
            });
            bbsItemLlThreeImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageBrowseActivity.startActivity(MyForumDetailsActivity.this,new ArrayList<String>(img));
                }
            });
        } else if (result.size() == 0) {
            bbsItemBigImg.setVisibility(View.GONE);
            bbsItemLlTwoImg.setVisibility(View.GONE);
            bbsItemLlThreeImg.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_delete, R.id.tv_anew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                delete();
                break;
            case R.id.tv_anew:
                Intent intent =new Intent(MyForumDetailsActivity.this,SendMessageActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 删除帖子
     */
    private void delete() {

        progressDialog.show();
        serverDao.delArticle(getUser(this).id, model.getId(), new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(MyForumDetailsActivity.this, baseResponse.message + "");
                if (baseResponse.errNum.equals("0")) {
                    EventBus.getDefault().post(model);
                    finish();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
            }
        });

    }
}
