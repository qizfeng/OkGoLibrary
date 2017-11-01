package com.project.community.ui.me;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.Event.AddAddressEvent;
import com.project.community.Event.AddHouseEvent;
import com.project.community.Event.ChangeAddressEvent;
import com.project.community.Event.DefaultAddressEvent;
import com.project.community.Event.DelAddressEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.AddressListBean;
import com.project.community.ui.adapter.MyAddressAdapter;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/23 14:23
 * em：617716355@qq.com
 */
public class MyAddressActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.rv_address)
    RecyclerView rvAddress;
    @Bind(R.id.tv_new_address)
    TextView tvNewAddress;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;


    private MyAddressAdapter myAddressAdapter;


    private int page = 1;

    private int cj=0;


    private List<AddressListBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        steTitle();

        steAdapter();
    }

    private void steTitle() {

        initToolBar(toolbar, tvTitle, true, "地址管理", R.mipmap.iv_back);

        if (getIntent().getExtras()!=null){
            cj=getIntent().getIntExtra("cj",0);
        }

    }

    private void steAdapter() {
        swipeRl.setRefreshing(true);

        myAddressAdapter = new MyAddressAdapter(R.layout.item_my_address, mData);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(myAddressAdapter);
        myAddressAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        myAddressAdapter.loadMoreEnd();

                    }

                }, 1000);
            }
        }, rvAddress);

        myAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                AddAddressActivity.startActivity(MyAddressActivity.this, mData.get(position));


            }
        });


        swipeRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        swipeRl.setRefreshing(false);
                        getAddressList();
                    }
                }, 1000);
            }
        });

        getAddressList();
    }


    /**
     * 获取消息列表
     */
    private void getAddressList() {
        swipeRl.setRefreshing(true);
        if (mData != null && mData.size() > 0) {
            mData.clear();
            myAddressAdapter.notifyDataSetChanged();
        }
        serverDao.getAddressList(getUser(this).id, new JsonCallback<BaseResponse<List<AddressListBean>>>() {
            @Override
            public void onSuccess(BaseResponse<List<AddressListBean>> listBaseResponse, Call call, Response response) {

                swipeRl.setRefreshing(false);
                if (listBaseResponse.errNum.equals("0")) {

                    if (listBaseResponse.retData != null) {
                        mData.addAll(listBaseResponse.retData);
                        myAddressAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showToast(MyAddressActivity.this, listBaseResponse.message + "");

                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                swipeRl.setRefreshing(false);
                ToastUtil.showToast(MyAddressActivity.this, e.getMessage() + "");
            }
        });

    }


    @OnClick(R.id.tv_new_address)
    public void onViewClicked() {

        AddAddressActivity.startActivity(this, null);
    }


    /**
     * 有添加地址刷新页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AddAddressEvent event) {

        getAddressList();
    }

    /**
     * 删除地址
     *
     * @param delAddressEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DelAddressEvent delAddressEvent) {

//      getAddressList();
        if (delAddressEvent.getAddressListBean() != null)
            delAddress(delAddressEvent);
        else
            ToastUtil.showToast(this,"参数错误");
    }
  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DefaultAddressEvent def) {

//      getAddressList();
        if (def.getAddressListBean() != null)
            defaultAddress(def);
        else
            ToastUtil.showToast(this,"参数错误");
    }

    /**
     * 设为默认地址
     * @param def
     */

    private void defaultAddress(final DefaultAddressEvent def) {
        progressDialog.show();
        serverDao.setDefaultAddress(getUser(this).id, def.getAddressListBean().getId(), new JsonCallback<BaseResponse<List<String>>>() {
            @Override
            public void onSuccess(BaseResponse<List<String>> stringBaseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(MyAddressActivity.this, stringBaseResponse.message + "");
                if (cj!=0) {
                    EventBus.getDefault().post(new ChangeAddressEvent(def.getAddressListBean()));
                    finish();
                }
                if (stringBaseResponse.errNum.equals("0")) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getId().equals(def.getAddressListBean().getId())){
                           mData.get(i).setIsDefault("0");
                        }else {
                            mData.get(i).setIsDefault("1");

                        }
                    }
                    myAddressAdapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
                Log.e("TAG_F", e.getMessage() + "");

            }
        });

    }


    /**
     * 删除地址
     */
    private void delAddress(final DelAddressEvent delAddressEvent) {
        progressDialog.show();
        Log.e("id0 = ",getUser(this).id);
        Log.e("id1 = ",delAddressEvent.getAddressListBean().getId());
        serverDao.deleteAddress(getUser(this).id, delAddressEvent.getAddressListBean().getId(), new JsonCallback<BaseResponse<List<String>>>() {
            @Override
            public void onSuccess(BaseResponse<List<String>> stringBaseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(MyAddressActivity.this, stringBaseResponse.message + "");

                if (stringBaseResponse.errNum.equals("0")) {

                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getId().equals(delAddressEvent.getAddressListBean().getId())){
                            mData.remove(i);
                            myAddressAdapter.notifyDataSetChanged();
                        }
                    }


                } else {

                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
                Log.e("TAG_F", e.getMessage() + "");

            }
        });


    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
