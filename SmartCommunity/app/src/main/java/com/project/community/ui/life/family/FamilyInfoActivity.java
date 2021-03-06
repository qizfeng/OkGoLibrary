package com.project.community.ui.life.family;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.FamilyModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.ui.adapter.FamilyAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by qizfeng on 17/8/23.
 * 家庭信息
 */

public class FamilyInfoActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    Button mBtnAddPerson;
    Button mBtnDeleteFamily;

    private List<FamilyModel> mData = new ArrayList<>();
    private List<FamilyPersonModel> personList = new ArrayList<>();
    private FamilyAdapter mAdapter;
    private View footer;
    private View header;
    private TextView mTvFamilyNo;
    private TextView mTvFamilyAddress;
    private Dialog mDialog;
    private static int tabSelection = 0;
    private boolean isNetFinish = false;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FamilyInfoActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_family_info), R.mipmap.iv_back);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        footer = LayoutInflater.from(this).inflate(R.layout.layout_footer_family, null);
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_family, null);
        mTvFamilyNo = (TextView) header.findViewById(R.id.tv_family_no);
        mTvFamilyAddress = (TextView) header.findViewById(R.id.tv_family_addr);
        mBtnDeleteFamily = (Button) footer.findViewById(R.id.btn_delete_family);
        mBtnAddPerson = (Button) footer.findViewById(R.id.btn_add_person);
        RxView.clicks(mBtnDeleteFamily)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onClick(mBtnDeleteFamily);
                    }
                });
        RxView.clicks(mBtnAddPerson)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onClick(mBtnAddPerson);
                    }
                });
        RxView.clicks(mIvAdd)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onClick(mIvAdd);
                    }
                });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化tabLayout
     */
    private void initTabLayout(List<FamilyModel> models) {
        if (models.size() != mTabLayout.getTabCount()) {
            mTabLayout.removeAllTabs();
            if (mTabLayout.getTabCount() == 0) {
                for (int i = 0; i < models.size(); i++) {
                    mTabLayout.addTab(mTabLayout.newTab().setText(models.get(i).familyName));
                }
            }
        }
        if (mTabLayout.getTabCount() > 2) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelection = tab.getPosition();
                LogUtils.e("posistion:"+mTabLayout.getSelectedTabPosition()+","+tabSelection);
                switchData(tab.getPosition());
                if (mTabLayout.getTabCount() > 2) {
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                } else {
                    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabSelection = tab.getPosition();
                LogUtils.e("onTabReselected:"+mTabLayout.getSelectedTabPosition()+","+tabSelection);
                switchData(tab.getPosition());
                if (mTabLayout.getTabCount() > 2) {
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                } else {
                    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                }

            }
        });
    }

    /**
     * 初始化家庭数据
     */
    public void initData() {
        mAdapter = new FamilyAdapter(personList, new FamilyAdapter.OnAdapterItemClickListener() {
            @Override
            public void onItemClick(View view, final FamilyPersonModel item, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.activity_family_look_person));
                bundle.putString("familyId", mData.get(mTabLayout.getSelectedTabPosition()).id);
                bundle.putString("personId", item.id);
                bundle.putString("roomNo", mData.get(mTabLayout.getSelectedTabPosition()).room.roomNo);
                bundle.putBoolean("isLook", true);
                FamilyAddPersonActivity.startActivity(FamilyInfoActivity.this, bundle);

            }

            @Override
            public void onDeleteClick(View view, FamilyPersonModel item, final int position) {
                if (!"2".equals(mData.get(mTabLayout.getSelectedTabPosition()).auditStatus)) {
                    showToast(getString(R.string.toast_error_permission));
                    return;
                }
                showAlertDeletePersonDialog(position - 2);//-1 去掉头部 -2去掉flag
            }

            @Override
            public void onEdit(View view, final FamilyPersonModel item, int position) {
                if (!"2".equals(mData.get(mTabLayout.getSelectedTabPosition()).auditStatus)) {
                    showToast(getString(R.string.toast_error_permission));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.activity_family_edit_person));
                bundle.putString("familyId", mData.get(mTabLayout.getSelectedTabPosition()).id);
                bundle.putString("personId", item.id);
                bundle.putString("roomNo", mData.get(mTabLayout.getSelectedTabPosition()).room.roomNo);
                FamilyAddPersonActivity.startActivity(FamilyInfoActivity.this, bundle);

            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addFooterView(footer);
        mAdapter.addHeaderView(header);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    private void switchData(int position) {
        try {
            if (mData.size() > 0)
                getFamilyData(mData.get(position).room.roomNo, mData.get(position).id);
            else {
                mAdapter.setNewData(null);
                mAdapter.setEmptyView(R.layout.empty_view);
                TextView textView = (TextView) mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                textView.setText(getString(R.string.empty_no_data_family));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除成员对话框
     *
     * @param position
     */
    public void showAlertDeletePersonDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        LogUtils.e("alert:"+position);
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
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
                deletePerson(position);
                mDialog.dismiss();
            }
        });
    }

    /**
     * 删除家庭对话框
     *
     * @param content
     * @param position
     */
    public void showAlertDialog(String content, final int position) {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
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
//                mTabLayout.removeTabAt(position);
                deleteFamily(position, mData.get(position).id);
                mDialog.dismiss();
            }
        });

    }

    /**
     * 删除家庭接口调用
     *
     * @param position
     * @param familyId
     */
    private void deleteFamily(final int position, String familyId) {
        serverDao.deleteFamily(getUser(this).id, mData.get(mTabLayout.getSelectedTabPosition()).room.roomNo, familyId, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                mTabLayout.removeTabAt(position);
                showToast(baseResponse.message);
                if(mTabLayout.getTabCount()<=0){
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = (TextView) mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_data_family));
                }else {
                   loadData();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 添加人口
     *
     * @param view
     */
    public void onAddPerson(View view) {
        if (!isNetFinish) {
            return;
        }
        if (!"2".equals(mData.get(mTabLayout.getSelectedTabPosition()).auditStatus)) {
            showToast(getString(R.string.toast_error_permission));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.activity_family_add_person));
        bundle.putString("familyId", mData.get(mTabLayout.getSelectedTabPosition()).id);
        bundle.putString("roomNo", mData.get(mTabLayout.getSelectedTabPosition()).room.roomNo);
        FamilyAddPersonActivity.startActivity(this, bundle);
    }

    /**
     * 删除家庭点击事件
     *
     * @param view
     */
    public void onDeleteFamily(View view) {
        if (!isNetFinish) {
            return;
        }
        if (!"2".equals(mData.get(mTabLayout.getSelectedTabPosition()).auditStatus)) {
            showToast(getString(R.string.toast_error_permission));
            return;
        }
        showAlertDialog(getString(R.string.txt_family_delete_confirm), mTabLayout.getSelectedTabPosition());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_person:
                if (!isNetFinish) {
                    return;
                }
                try {
                    onAddPerson(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_delete_family:
                if (!isNetFinish) {
                    return;
                }
                try {
                    onDeleteFamily(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_add:
                if (!isNetFinish) {
                    return;
                }
                try {
                    FamilyAddActivity.startActivity(this, new Bundle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        serverDao.getFamilyListInfo(getUser(this).id, getUsername(this), getUser(this).roomNo, new JsonCallback<BaseResponse<List<FamilyModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<FamilyModel>> baseResponse, Call call, Response response) {
                mData = new ArrayList<>();
                mData = baseResponse.retData;
                if (mData.size() == 0) {
                    personList = new ArrayList<>();
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = (TextView) mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_data_family));
                } else {
                    initTabLayout(mData);
                    LogUtils.e("pos:" + tabSelection);
                    switchData(tabSelection);
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                mAdapter.setNewData(null);
                mAdapter.setEmptyView(R.layout.empty_view);
                TextView textView = (TextView) mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                textView.setText(getString(R.string.empty_no_data_family));
                showToast(e.getMessage());
            }

            @Override
            public void onAfter(BaseResponse<List<FamilyModel>> baseResponse, Exception e) {
                super.onAfter(baseResponse, e);
                isNetFinish = true;
            }
        });
    }

    /**
     * 切换选项卡获取单个家庭信息
     */
    private void getFamilyData(String roomNo, String familyId) {
        serverDao.getFamilyInfo(getUser(this).id, getUsername(this), roomNo, familyId, new DialogCallback<BaseResponse<List<FamilyModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<FamilyModel>> baseResponse, Call call, Response response) {
                if (baseResponse.retData.size() > 0) {
                    mTvFamilyNo.setText("房屋编号" + baseResponse.retData.get(0).room.roomNo);
                    mTvFamilyAddress.setText(baseResponse.retData.get(0).room.address);

                    personList = new ArrayList<>();
                    FamilyPersonModel familyPersonModelInfo = new FamilyPersonModel();
                    familyPersonModelInfo.itemType = 1;
                    personList.add(0, familyPersonModelInfo);
                    personList.addAll(baseResponse.retData.get(0).memberList);
                    mAdapter.setNewData(personList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 删除家庭成员接口调用
     *
     * @param position
     */
    private void deletePerson(final int position) {
        if (!isNetFinish) {
            return;
        }
        serverDao.deletePerson(getUser(this).id, mData.get(mTabLayout.getSelectedTabPosition()).memberList.get(position).id,
                mData.get(mTabLayout.getSelectedTabPosition()).room.getRoomNo(),
                new DialogCallback<BaseResponse<List>>(this) {
                    @Override
                    public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                        mAdapter.remove(position);
                        LogUtils.e("posistion:"+tabSelection+","+position);
                       // mData.get(tabSelection).memberList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        showToast(baseResponse.message);
                        switchData(0);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast(e.getMessage());
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabSelection=0;
    }
}
