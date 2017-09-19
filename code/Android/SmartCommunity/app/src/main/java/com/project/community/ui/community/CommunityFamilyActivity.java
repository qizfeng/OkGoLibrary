package com.project.community.ui.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.FamilyModel;
import com.project.community.model.UnitModel;
import com.project.community.ui.adapter.CommunityFamilyAdapter;
import com.project.community.ui.adapter.CommunityUnitSingleChoiceAdapter;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/9/15.
 * 社区-小区人员列表
 */

public class CommunityFamilyActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.recyclerViewSelect)
    RecyclerView mRecyclerViewSelect;
    private List<FamilyModel> mData = new ArrayList<>();
    private List<UnitModel> unitModelList = new ArrayList<>();
    private CommunityFamilyAdapter mAdapter;
    private CommunityUnitSingleChoiceAdapter singleChoiceAdapter;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommunityFamilyActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_person_list);
        initToolBar(mToolBar, mTvTitle, true, "一区一栋居民", R.mipmap.iv_back);
        initView();
        initData();
        loadData();
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化家庭数据
     */
    public void initData() {
        mAdapter = new CommunityFamilyAdapter(mData, new CommunityFamilyAdapter.AdapterItemClickListener() {
            @Override
            public void onItemClick(View view, int position,int childPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("id",mData.get(position).memberList.get(childPosition).id);
                CommunityPersonActivity.startActivity(CommunityFamilyActivity.this, bundle);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);


        initTestUnitData();
        singleChoiceAdapter = new CommunityUnitSingleChoiceAdapter(this, unitModelList, new CommunityUnitSingleChoiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                singleChoiceAdapter.check(position);
                loadData();
            }
        });
        // 默认选中第一个item
        singleChoiceAdapter.setDefaultCheckedItemPosition(0);
        // 这个方法不能忘，指定显示布局
        mRecyclerViewSelect.setLayoutManager(new LinearLayoutManager(this));
        // 添加分隔线，DividerItemDecoration这个类是系统提供的，在support包中
        decoration = new SpacesItemDecoration(0, false);
        mRecyclerViewSelect.addItemDecoration(decoration);
        mRecyclerViewSelect.setAdapter(singleChoiceAdapter);
    }

    private void initTestUnitData() {
        unitModelList = new ArrayList<>();
        UnitModel unitModel1 = new UnitModel();
        unitModel1.unit = "一单元";
        UnitModel unitModel2 = new UnitModel();
        unitModel2.unit = "二单元";
        UnitModel unitModel3 = new UnitModel();
        unitModel3.unit = "三单元";
        UnitModel unitModel4 = new UnitModel();
        unitModel4.unit = "四单元";
        UnitModel unitModel5 = new UnitModel();
        unitModel5.unit = "五单元";
        unitModelList.add(unitModel1);
        unitModelList.add(unitModel2);
        unitModelList.add(unitModel3);
        unitModelList.add(unitModel4);
        unitModelList.add(unitModel5);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        serverDao.getFamilyListInfo(getUser(this).id, getUsername(this), getUser(this).roomNo, new DialogCallback<BaseResponse<List<FamilyModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<FamilyModel>> baseResponse, Call call, Response response) {
                mData = new ArrayList<>();
                mData = baseResponse.retData;
                if (mData.size() == 0) {
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_data_family));
                } else {
                    mAdapter.setNewData(mData);
                    mAdapter.notifyDataSetChanged();
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
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_all:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                if (mRecyclerViewSelect.getVisibility() == View.VISIBLE) {
                    mRecyclerViewSelect.setVisibility(View.GONE);
                    Drawable drawable = getResources().getDrawable(R.mipmap.d44_xiala);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    TextView tvChoice = item.getActionView().findViewById(R.id.tv_choice);
                    tvChoice.setCompoundDrawables(null, null, drawable, null);
                } else if (mRecyclerViewSelect.getVisibility() == View.GONE) {
                    mRecyclerViewSelect.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.mipmap.d44_shangla);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    TextView tvChoice = item.getActionView().findViewById(R.id.tv_choice);
                    tvChoice.setCompoundDrawables(null, null, drawable, null);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community_toolbar_choise, menu);
        final MenuItem item = menu.findItem(R.id.action_all);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
