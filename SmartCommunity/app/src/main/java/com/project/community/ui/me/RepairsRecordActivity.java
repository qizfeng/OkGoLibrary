package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.RepairsRecordBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.RepairsRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/24 16:52
 * em：617716355@qq.com
 */
public class RepairsRecordActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.rv_government)
    RecyclerView rvGovernment;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;
    @Bind(R.id.tv_repairs)
    TextView tvRepairs;

    private RepairsRecordAdapter repairsRecordAdapter;
    private List<RepairsRecordBean> mData;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_record);
        ButterKnife.bind(this);
        setTitles();
        steAdapter();
    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "报修记录", R.mipmap.iv_back);
    }


    private void steAdapter() {
        mData = new ArrayList<>();

        repairsRecordAdapter = new RepairsRecordAdapter(R.layout.item_repairs_record, mData);
        rvGovernment.setLayoutManager(new LinearLayoutManager(this));
        rvGovernment.setAdapter(repairsRecordAdapter);
        repairsRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
//                        repairsRecordAdapter.loadMoreEnd();
                        getData();
                    }

                }, 1000);
            }
        }, rvGovernment);


        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(RepairsRecordActivity.this, RepairsDetailsActivity.class));
            }
        });


        swipeRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.clear();
                        repairsRecordAdapter.removeAllData();
                        swipeRl.setRefreshing(true);
                        getData();
                    }
                }, 1000);
            }
        });

        swipeRl.setRefreshing(true);
        getData();
    }


    /**
     * 获取我的报修列表
     */
    private void getData() {
        serverDao.getRepairsRecord(getUser(this).id, String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<List<RepairsRecordBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<RepairsRecordBean>> objectBaseResponse, Call call, Response response) {

                        if (swipeRl != null)
                            swipeRl.setRefreshing(false);

                        mData.addAll(objectBaseResponse.retData);
                        if (page == 1) {
                            repairsRecordAdapter.setNewData(mData);
                            repairsRecordAdapter.setEnableLoadMore(true);
                        } else {
                            repairsRecordAdapter.addData(objectBaseResponse.retData);
                            repairsRecordAdapter.loadMoreComplete();
                        }
                        if (objectBaseResponse.retData.size() < AppConstants.PAGE_SIZE)
                            repairsRecordAdapter.loadMoreEnd();


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        if (swipeRl != null)
                            swipeRl.setRefreshing(false);
                    }
                });


    }

    @OnClick(R.id.tv_repairs)
    public void onViewClicked() {

        startActivity(new Intent(this, ImRepairsActivity.class));
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


    /**
     * 取消报修
     */
    private void propRepairCancel(final String id) {
        serverDao.propRepairCancel(getUser(this).id, id,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> objectBaseResponse, Call call, Response response) {

                        if (objectBaseResponse.errNum.equals("0")) {

                            for (int i = 0; i < repairsRecordAdapter.getData().size(); i++) {
                                if (repairsRecordAdapter.getData().get(i).getOrderNo().equals(id)) {
                                    repairsRecordAdapter.getData().get(i).setRepairStatus("1");
                                    repairsRecordAdapter.notifyDataSetChanged();

                                }

                            }

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        if (swipeRl != null)
                            swipeRl.setRefreshing(false);
                    }
                });


    }


    /**
     * 完成订单
     * @param id
     */
    private void propRepairComplete(final String id) {
        serverDao.propRepairComplete(getUser(this).id, id,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> objectBaseResponse, Call call, Response response) {

                        if (objectBaseResponse.errNum.equals("0")) {

                            for (int i = 0; i < repairsRecordAdapter.getData().size(); i++) {
                                if (repairsRecordAdapter.getData().get(i).getOrderNo().equals(id)) {
                                    repairsRecordAdapter.getData().get(i).setHandleStatus("2");
                                    repairsRecordAdapter.notifyDataSetChanged();

                                }

                            }

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        if (swipeRl != null)
                            swipeRl.setRefreshing(false);
                    }
                });


    }
}
