package com.project.community.ui.life.minsheng;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ModuleModel;
import com.project.community.ui.adapter.ShopMuduleAdapter;
import com.project.community.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/10/17.
 * 更多
 */

public class MerchantModuleMoreActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    private List<ModuleModel> mData = new ArrayList<>();
    private ShopMuduleAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minsheng_module_more);
        initToolBar(mToolBar,mTvTitle,true,getString(R.string.txt_shop_more),R.mipmap.iv_back);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager =new GridLayoutManager(this,4);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 4;
//            }
//        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this,0));
        initData();
    }
    private void initData(){
        ModuleModel moduleModel = new ModuleModel();
        moduleModel.title="附近商圈";
        moduleModel.res=R.mipmap.d27_icon1;
        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title="家政";
        moduleModel1.res=R.mipmap.d27_icon2;

        ModuleModel moduleModel2 = new ModuleModel();
        moduleModel2.title="美容美发";
        moduleModel2.res=R.mipmap.d28_icon7;
        ModuleModel moduleModel3 = new ModuleModel();
        moduleModel3.title="便利店";
        moduleModel3.res=R.mipmap.d28_icon8;
        mData.add(moduleModel);
        mData.add(moduleModel1);
        mData.add(moduleModel2);
        mData.add(moduleModel3);
        mAdapter=new ShopMuduleAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

}
