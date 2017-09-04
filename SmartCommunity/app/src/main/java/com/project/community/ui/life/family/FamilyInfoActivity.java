package com.project.community.ui.life.family;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
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

import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.FamilyModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.ui.CommonDialogActivity;
import com.project.community.ui.adapter.FamilyAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/8/23.
 * 家庭信息
 */

public class FamilyInfoActivity extends BaseActivity implements View.OnClickListener {
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
    private FamilyAdapter mAdapter;
    private View footer;
    private View header;
    private Dialog mDialog;

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
        initTabLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            FamilyModel familyModel1 = new FamilyModel();
            familyModel1.id = "9";
            familyModel1.familyNo = "1000001";
            familyModel1.familyAddr = "北京市海淀区西三旗";
            familyModel1.familyName = bundle.getString("houseName");
            familyModel1.familyPersons = new ArrayList<>();
            mData.add(familyModel1);
            mTabLayout.addTab(mTabLayout.newTab().setText(bundle.getString("houseName")));
            if (mTabLayout.getTabCount() > 4) {
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            } else {
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            }
        }
    }

    private void initView() {
        footer = LayoutInflater.from(this).inflate(R.layout.layout_footer_family, null);
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_family, null);
        mBtnDeleteFamily = (Button) footer.findViewById(R.id.btn_delete_family);
        mBtnAddPerson = (Button) footer.findViewById(R.id.btn_add_person);
        mBtnDeleteFamily.setOnClickListener(this);
        mBtnAddPerson.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * 初始化tabLayout
     */
    private void initTabLayout() {
        //  tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_wuye_all));
//        mTabLayout.addTab(mTabLayout.newTab().setText("家庭1"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("family2"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("家庭1"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("family2"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("家庭1"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("family2"));

        for (int i = 0; i < mData.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mData.get(i).familyName));
        }
        if (mTabLayout.getTabCount() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchData(tab.getPosition());
                if (mTabLayout.getTabCount() > 4) {
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

            }
        });
    }

    /**
     * 初始化家庭数据
     */
    public void initData() {
        mData = new ArrayList<>();

        List<FamilyPersonModel> familyPersonModels1 = new ArrayList<>();

        FamilyPersonModel familyPersonModelInfo = new FamilyPersonModel();
        familyPersonModelInfo.itemType = 1;


        FamilyPersonModel familyPersonModel1 = new FamilyPersonModel();
        familyPersonModel1.id = "1-1";
        familyPersonModel1.itemType = 2;
        familyPersonModel1.relative = "户主";
        familyPersonModel1.relativeId = "1";
        familyPersonModel1.name = "努尔哈赤";
        familyPersonModel1.hasTag = true;
        familyPersonModel1.tagRes = R.mipmap.d24_renzhen;
        familyPersonModel1.res = R.mipmap.d54_tx;

        FamilyPersonModel familyPersonModel2 = new FamilyPersonModel();
        familyPersonModel2.id = "1-2";
        familyPersonModel2.itemType = 2;
        familyPersonModel2.relative = "皇后";
        familyPersonModel2.relativeId = "2";
        familyPersonModel2.name = "哈哈纳扎青";
        familyPersonModel2.hasTag = true;
        familyPersonModel2.tagRes = R.mipmap.d24_renzhen;
        familyPersonModel2.res = R.mipmap.d54_tx;

        FamilyPersonModel familyPersonModel3 = new FamilyPersonModel();
        familyPersonModel3.id = "1-3";
        familyPersonModel3.itemType = 2;
        familyPersonModel3.relative = "儿子";
        familyPersonModel3.relativeId = "2";
        familyPersonModel3.name = "皇太极";
        familyPersonModel3.hasTag = false;
        familyPersonModel3.tagRes = R.mipmap.d24_renzhen;
        familyPersonModel3.res = R.mipmap.d54_tx;


        familyPersonModels1.add(familyPersonModelInfo);
        familyPersonModels1.add(familyPersonModel1);
        familyPersonModels1.add(familyPersonModel2);
        familyPersonModels1.add(familyPersonModel3);


        FamilyModel familyModel1 = new FamilyModel();
        familyModel1.id = "1";
        familyModel1.familyNo = "1000001";
        familyModel1.familyAddr = "北京市海淀区西三旗";
        familyModel1.familyName = "海淀家";
        familyModel1.familyPersons = familyPersonModels1;


        FamilyModel familyModel2 = new FamilyModel();
        familyModel2.id = "2";
        familyModel2.familyNo = "1000002";
        familyModel2.familyAddr = "北京市丰台区镇岗塔路";
        familyModel2.familyName = "丰台家";
        familyModel2.familyPersons = familyPersonModels1;

        FamilyModel familyModel3 = new FamilyModel();
        familyModel3.id = "2";
        familyModel3.familyNo = "1000003";
        familyModel3.familyAddr = "德克萨斯州休斯顿丰田中心";
        familyModel3.familyName = "火箭家";
        familyModel3.familyPersons = familyPersonModels1;

        mData.add(familyModel1);
        mData.add(familyModel2);
        mData.add(familyModel3);
        mData.add(familyModel3);
        mData.add(familyModel3);
        mData.add(familyModel3);
        mData.add(familyModel3);
        mData.add(familyModel3);
        mData.add(familyModel3);
//        if (mData.size() > 0)
        mAdapter = new FamilyAdapter(mData.get(0).familyPersons, new FamilyAdapter.OnAdapterItemClickListener() {
            @Override
            public void onItemClick(FamilyPersonModel item, int position) {

            }

            @Override
            public void onDeleteClick(FamilyPersonModel item, int position) {
                showAlertDialog(position);

            }

            @Override
            public void onEdit(FamilyPersonModel item, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.activity_family_edit_person));
                FamilyAddPersonActivity.startActivity(FamilyInfoActivity.this, bundle);
            }
        });
//        else {
//            mAdapter = new FamilyAdapter(null, new RecycleItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//
//                }
//
//                @Override
//                public void onTextClick(View view, int position) {
//
//                }
//            });
//        }
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addFooterView(footer);
        mAdapter.addHeaderView(header);
//        ItemTouchHelper helper = new ItemTouchHelper(new RecyclerItemTouchHelperCallBack(mRecyclerView, mData));
//        helper.attachToRecyclerView(mRecyclerView);
    }

    private void switchData(int position) {
        mAdapter.setNewData(mData.get(position).familyPersons);
        mAdapter.notifyDataSetChanged();
    }


    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
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
        TextView tv_content = (TextView)mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel =(Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close =(ImageView) mDialog.findViewById(R.id.iv_close);
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
                mData.remove(position);
                mAdapter.remove(position);
                mAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });

//        Bundle bundle = new Bundle();
//        bundle.putString("title",getString(R.string.txt_confirm_delete));
//        CommonDialogActivity.startActivity(this,bundle);
    }

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
        TextView tv_content =(TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(content);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel =(Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close =(ImageView) mDialog.findViewById(R.id.iv_close);
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
                mTabLayout.removeTabAt(position);
                mDialog.dismiss();
            }
        });

//        Bundle bundle = new Bundle();
//        CommonDialogActivity.startActivity(this, bundle);
    }

    /**
     * 添加人口
     *
     * @param view
     */
    public void onAddPerson(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.activity_family_add_person));
        FamilyAddPersonActivity.startActivity(this, bundle);
    }

    /**
     * 删除家庭
     *
     * @param view
     */
    public void onDeleteFamily(View view) {
        showAlertDialog(getString(R.string.txt_family_delete_confirm), mTabLayout.getSelectedTabPosition());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_person:
                onAddPerson(view);
                break;
            case R.id.btn_delete_family:
                onDeleteFamily(view);
                break;
            case R.id.iv_add:
                FamilyAddActivity.startActivity(this, new Bundle());
                break;
        }
    }
}
