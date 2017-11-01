package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.project.community.R;
import com.project.community.base.BaseRecyclerAdapter;
import com.project.community.bean.RepairsDetailsBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.ImageBrowseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * author：fangkai on 2017/10/25 18:11
 * em：617716355@qq.com
 */
public class RepairsDetailsAdapter extends BaseRecyclerAdapter<RepairsDetailsBean.ProcessBean, RepairsDetailsAdapter.RepairsDetailsHolder> {


    private List<String> item = new ArrayList<>();

    private RepairsDetailsBean repairsDetailsBean;

    public RepairsDetailsAdapter(Context context) {
        super(context);
    }

    public RepairsDetailsAdapter(Context context, List datas, RepairsDetailsBean repairsDetailsBean) {
        super(context, datas);
        this.repairsDetailsBean = repairsDetailsBean;
    }

    public void setThisItem(List<String> item) {
        this.item = item;
        notifyDataSetChanged();
    }


    @Override
    public RepairsDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repairs_details, parent, false);
        return new RepairsDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepairsDetailsHolder holder, final int position) {


        holder.tvValue.setText(mDatas.get(position).getValue());

        holder.tvTime.setText(mDatas.get(position).getDate());


        if (position == 0) {
            holder.llEvaluateOne.setVisibility(View.GONE);
            holder.llEvaluateTow.setVisibility(View.GONE);
        }
        if (position == 1) {
            //受理，   判断当前订单状态，1=后台指派订单给维修人员
            // 2维修人员点击处理订单订单变为服务中
            // 3维修人员提交处理过程提交
            holder.llEvaluateOne.setVisibility(View.VISIBLE);
            holder.llEvaluateTow.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
            holder.itemOrderDetailShouliCommentComment.setVisibility(View.GONE);

            Glide.with(mContext)
                    .load(AppConstants.HOST + repairsDetailsBean.getOrder().getRepair().getPhoto())
                    .placeholder(R.mipmap.d54_tx)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(holder.ivHeader);

            holder.itemOrderDetailShouliCommentName.setText(repairsDetailsBean.getOrder().getRepair().getName());

            holder.itemOrderDetailShouliCommentNum.setText("工号： " + repairsDetailsBean.getOrder().getRepair().getNo());
            holder.itemOrderDetailShouliCommentPro.setText(repairsDetailsBean.getOrder().getRepair().getUserType());


//            holder.llEvaluateOne.setVisibility(View.VISIBLE);
//            holder.llEvaluateTow.setVisibility(View.GONE);
//            holder.viewLine.setVisibility(View.GONE);
//            holder.itemOrderDetailShouliCommentComment.setVisibility(View.GONE);
//            if (repairsDetailsBean.getOrder().getOrderStatus().equals("1")
//                    || repairsDetailsBean.getOrder().getOrderStatus().equals("2")) {
//                holder.itemOrderDetailShouliCommentComment.setVisibility(View.GONE);
//            }
//            if (repairsDetailsBean.getOrder().getOrderStatus().equals("3")) {
//                holder.itemOrderDetailShouliCommentComment.setVisibility(View.VISIBLE);
//                holder.llEvaluateTow.setVisibility(View.GONE);
//                holder.viewLine.setVisibility(View.GONE);
//                if (repairsDetailsBean.getReply().size() > 0)
//                    holder.itemOrderDetailShouliCommentComment.setText(repairsDetailsBean.getReply().get(0).getContent());
//            }

        }

        if (position == 2) {

            Glide.with(mContext)
                    .load(AppConstants.HOST + repairsDetailsBean.getOrder().getRepair().getPhoto())
                    .placeholder(R.mipmap.d54_tx)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(holder.ivHeader);


            holder.itemOrderDetailShouliCommentName.setText(repairsDetailsBean.getOrder().getRepair().getName());

            holder.itemOrderDetailShouliCommentNum.setText("工号： " + repairsDetailsBean.getOrder().getRepair().getNo());
            holder.itemOrderDetailShouliCommentPro.setText(repairsDetailsBean.getOrder().getRepair().getUserType());

            holder.llEvaluateOne.setVisibility(View.VISIBLE);
            holder.llEvaluateTow.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
            //已完成
//            if (repairsDetailsBean.getOrder().getOrderStatus().equals("3")) {
//                holder.itemOrderDetailShouliCommentComment.setVisibility(View.VISIBLE);
//                holder.llEvaluateTow.setVisibility(View.GONE);
//                holder.viewLine.setVisibility(View.GONE);
//                if (repairsDetailsBean.getReply().size() > 0)
//                    holder.itemOrderDetailShouliCommentComment.setText(repairsDetailsBean.getReply().get(0).getContent());
//            }


            if (repairsDetailsBean.getReply().size() >= 0) {
                holder.itemOrderDetailShouliCommentComment.setText(repairsDetailsBean.getReply().get(0).getContent());


                List<String> result = Arrays.asList(repairsDetailsBean.getReply().get(0).getImagesUrl().split(","));

                final List<String> img = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    img.add(AppConstants.HOST + result.get(i).toString());
                }

                if (result.size() == 1) {

                    holder.getView(R.id.bbs_item_big_img).setVisibility(View.VISIBLE);
                    holder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);

                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(0))
                            .into((ImageView) holder.getView(R.id.bbs_item_big_img));


                    holder.getView(R.id.bbs_item_big_img).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });

                } else if (result.size() == 2) {
                    holder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.VISIBLE);
                    holder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(0))
                            .into((ImageView) holder.getView(R.id.bbs_item_ll_two_img_1));
                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(1))
                            .into((ImageView) holder.getView(R.id.bbs_item_ll_two_img_2));


                    holder.getView(R.id.bbs_item_ll_two_img_1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });
                    holder.getView(R.id.bbs_item_ll_two_img_2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });

                } else if (result.size() == 3) {
                    holder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(0))
                            .into((ImageView) holder.getView(R.id.bbs_item_ll_three_img_1));
                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(1))
                            .into((ImageView) holder.getView(R.id.bbs_item_ll_three_img_2));
                    Glide.with(mContext)
                            .load(AppConstants.HOST + result.get(2))
                            .into((ImageView) holder.getView(R.id.bbs_item_ll_three_img_3));
                    holder.getView(R.id.bbs_item_ll_three_img_1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });
                    holder.getView(R.id.bbs_item_ll_three_img_2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });
                    holder.getView(R.id.bbs_item_ll_three_img_3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageBrowseActivity.startActivity(mContext, new ArrayList<String>(img));
                        }
                    });
                } else if (result.size() == 0) {
                    holder.getView(R.id.bbs_item_big_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_two_img).setVisibility(View.GONE);
                    holder.getView(R.id.bbs_item_ll_three_img).setVisibility(View.GONE);
                }

            }


        }

        if (position == 3) {
            //用户已评价


            holder.llEvaluateOne.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
            holder.llEvaluateTow.setVisibility(View.VISIBLE);
            if (repairsDetailsBean.getCommentList().size()>0) {
                holder.ratingBar.setCountSelected(Integer.parseInt(repairsDetailsBean.getCommentList().get(0).getStarLevel()));
                    holder.tvComment.setText(repairsDetailsBean.getCommentList().get(0).getCreateDate());
            }
        }

        /**
         * 进度条
         */
        holder.ivOn.setImageResource(R.mipmap.d41_dian);
        holder.vOne.setVisibility(View.VISIBLE);
        holder.vTow.setVisibility(View.VISIBLE);
        if (position == 0) {
            holder.vOne.setVisibility(View.INVISIBLE);
            holder.vTow.setVisibility(View.VISIBLE);
            holder.ivOn.setImageResource(R.mipmap.d41_dian_p);

        }
        if (position == mDatas.size() - 1) {
            holder.vOne.setVisibility(View.VISIBLE);
            holder.vTow.setVisibility(View.INVISIBLE);
            holder.ivOn.setImageResource(R.mipmap.d41_dian_p);
        }

        if (mDatas.size()==1){
            holder.vOne.setVisibility(View.INVISIBLE);
        }

    }


    static class RepairsDetailsHolder extends BaseViewHolder {

        @Bind(R.id.v_one)
        View vOne;
        @Bind(R.id.iv_on)
        ImageView ivOn;
        @Bind(R.id.v_tow)
        View vTow;
        @Bind(R.id.tv_value)
        TextView tvValue;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_header)
        ImageView ivHeader;
        @Bind(R.id.item_order_detail_shouli_comment_name)
        TextView itemOrderDetailShouliCommentName;
        @Bind(R.id.item_order_detail_shouli_comment_pro)
        TextView itemOrderDetailShouliCommentPro;
        @Bind(R.id.item_order_detail_shouli_comment_time)
        TextView itemOrderDetailShouliCommentTime;
        @Bind(R.id.item_order_detail_shouli_comment_num)
        TextView itemOrderDetailShouliCommentNum;
        @Bind(R.id.item_order_detail_shouli_comment_comment)
        TextView itemOrderDetailShouliCommentComment;
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
        @Bind(R.id.ll_evaluate_one)
        LinearLayout llEvaluateOne;
        @Bind(R.id.ratingBar)
        XLHRatingBar ratingBar;
        @Bind(R.id.tv_comment)
        TextView tvComment;
        @Bind(R.id.ll_evaluate_tow)
        LinearLayout llEvaluateTow;
        @Bind(R.id.view_line)
        View viewLine;

        RepairsDetailsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
