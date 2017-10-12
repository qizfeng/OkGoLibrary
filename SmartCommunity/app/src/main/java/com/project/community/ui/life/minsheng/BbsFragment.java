package com.project.community.ui.life.minsheng;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.okgo.utils.DateUtil;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.BbsApdater;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CommentPopwindow;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BbsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    BbsApdater bbsApdater;

    List<CommentModel> data = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsApdater commentsPopwinAdapter;
    private CommentPopwindow popupWindow;

    public static BbsFragment newInstance(int id) {
        final BbsFragment f = new BbsFragment();
        final Bundle args = new Bundle();
        args.putInt("ncid", id);
        f.setArguments(args);
        return f;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();

        for (int i = 0; i < 5; i++) {
            CommentModel commentModel = new CommentModel();
            commentModel.id="0";
            data.add(commentModel);
        }
        bbsApdater = new BbsApdater(data, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()){
                    case R.id.bbs_item_like_comment:
                        popAwindow(view, position);
                        break;
                    case R.id.bbs_item_like:
                        if (data.get(position).id.equals("10")) data.get(position).id="0";
                            else data.get(position).id="10";
                        bbsApdater.notifyItemChanged(position,data.get(position));
                        break;
                }
            }
        });
        recyclerView.setAdapter(bbsApdater);
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        setRefreshing(false);
    }
    /**
     * 设置是否刷新动画
     *
     * @param refreshing true开始刷新动画 false结束刷新动画
     */
    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    /**
     * 弹出评论列表
     *
     * @param parent
     */
    private void popAwindow(View parent, int position) {
        comments = new ArrayList<>();
        CommentModel comment1 = new CommentModel();
        comment1.userId = "張三";
        comment1.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment1.content = "張三:這個文章不錯喲";
        comment1.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484183249877077333.jpg";
        CommentModel comment2 = new CommentModel();
        comment2.userId = "李三";
        comment2.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment2.content = "李四 回复 张三:多谢支持";
        comment2.photo = "https://d-image.i4.cn/i4web/image//upload/20170111/1484114886498013658.jpg";
        CommentModel comment3 = new CommentModel();
        comment3.userId = "王五";
        comment3.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment3.content = "王五:呵呵";
        comment3.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484185403611050214.jpg";
        comments.add(comment1);
//        comments.add(comment2);
//        comments.add(comment3);

        commentsPopwinAdapter = new CommentsApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                popupWindow.et_comment.setText(getString(R.string.txt_receive) + comments.get(position).userName + ":");
                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        popupWindow = new CommentPopwindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                popupWindow.et_comment.setText("");
            }
        });
        popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(getActivity()) * 0.8);
        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
        commentsPopwinAdapter.bindToRecyclerView(popupWindow.lv_container);
        if (comments.size() > 0)
            popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
        if (comments.size() == 0) {
            commentsPopwinAdapter.setNewData(null);
            commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
            TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
            textView.setText(getString(R.string.empty_no_comment));
        }
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
