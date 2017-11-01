package com.project.community.ui.me;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.bean.RepairListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.ui.adapter.ServiesComApdater;
import com.project.community.util.ToastUtil;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * 已完成
 */
public class ServiesCompFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    private Dialog mDialog;
    ServiesComApdater mAdapter;
    List<RepairListBean> mData = new ArrayList<>();
    private int page = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servies_comp, container, false);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mData.clear();
                mAdapter.notifyDataSetChanged();
                getData();
            }
        });
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter = new ServiesComApdater(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.showLongToast(getActivity(),position);
                OrderDetailActivity.startActivity(getActivity(),mAdapter.getData().get(position).getOrderNo());

            }

            @Override
            public void onCustomClick(View view, int position) {
                showAlertDialog(position);

            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        });

        recyclerView.setAdapter(mAdapter);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        serverDao.repairList(getUser(getActivity()).id, "3", String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<List<RepairListBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<RepairListBean>> listBaseResponse, Call call, Response response) {

                        refreshLayout.setRefreshing(false);

                        if (listBaseResponse.errNum.equals("0")) {
                            if (page == 1) {
                                mData.addAll(listBaseResponse.retData);
                                mAdapter.setNewData(mData);
                                mAdapter.setEnableLoadMore(true);
                            } else {
                                mData.addAll(listBaseResponse.retData);
                                mAdapter.addData(listBaseResponse.retData);
                                mAdapter.loadMoreComplete();
                            }

                            if (listBaseResponse.retData.size() < AppConstants.PAGE_SIZE)
                                mAdapter.loadMoreEnd();

                        } else {
                            mAdapter.loadMoreEnd();
                            ToastUtil.showToast(getActivity(), listBaseResponse.message + "");

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage() + "");
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                    }
                });

    }

    /**
     * 删除成员对话框
     *
     * @param position
     */
    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mAdapter.notifyItemRemoved(position);
            }
        });
    }
}
