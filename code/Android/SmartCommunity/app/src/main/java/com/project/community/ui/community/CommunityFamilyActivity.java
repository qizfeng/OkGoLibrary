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
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommunityFamilyModel;
import com.project.community.model.DictionaryModel;
import com.project.community.model.DictionaryResponse;
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
    private List<CommunityFamilyModel> mData = new ArrayList<>();
    private List<DictionaryModel> unitModelList = new ArrayList<>();
    private CommunityFamilyAdapter mAdapter;
    private CommunityUnitSingleChoiceAdapter singleChoiceAdapter;
    private String floorId;//楼栋id
    private String floorName;//楼栋名称
    private String disName;//小区名

    private String unitId;//单元id

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CommunityFamilyActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_person_list);
        initView();
        initData();
        loadData();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            floorId = bundle.getString("floorId");
            floorName = bundle.getString("floorName");
            disName = bundle.getString("disName");
            initToolBar(mToolBar, mTvTitle, true, disName + floorName + "居民", R.mipmap.iv_back);
        }
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
            public void onItemClick(View view, int position, int childPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("memberId", mData.get(position).memberList.get(childPosition).id);
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

        getUnitData();

        // 这个方法不能忘，指定显示布局
        mRecyclerViewSelect.setLayoutManager(new LinearLayoutManager(this));
        // 添加分隔线，DividerItemDecoration这个类是系统提供的，在support包中
        decoration = new SpacesItemDecoration(0, false);
        mRecyclerViewSelect.addItemDecoration(decoration);

    }


    private void getUnitData() {
        serverDao.getDictionaryData("prop_room_unit", new JsonCallback<BaseResponse<DictionaryResponse>>() {
            @Override
            public void onSuccess(BaseResponse<DictionaryResponse> baseResponse, Call call, Response response) {
                unitModelList = new ArrayList<>();
                unitModelList = baseResponse.retData.dictList;
                singleChoiceAdapter = new CommunityUnitSingleChoiceAdapter(CommunityFamilyActivity.this, unitModelList, new CommunityUnitSingleChoiceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, long id) {
                        singleChoiceAdapter.check(position);
                        unitId = unitModelList.get(position).value;
                        loadData();

                    }
                });
                // 默认选中第一个item
//                singleChoiceAdapter.setDefaultCheckedItemPosition(0);
                mRecyclerViewSelect.setAdapter(singleChoiceAdapter);
            }
        });
        mRecyclerViewSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerViewSelect.setVisibility(View.GONE);
                Drawable drawable = getResources().getDrawable(R.mipmap.d44_xiala);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                TextView tvChoice = item.getActionView().findViewById(R.id.tv_choice);
                tvChoice.setCompoundDrawables(null, null, drawable, null);
                return false;
            }
        });

    }

    /**
     * userId
     * unit
     * floor
     * 加载数据
     */
    private void loadData() {
        serverDao.getCommunityFamilyPersonList(getUser(this).id, unitId, floorId, new DialogCallback<BaseResponse<List<CommunityFamilyModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<CommunityFamilyModel>> baseResponse, Call call, Response response) {
                mData = new ArrayList<>();
                mData = baseResponse.retData;
                if (mData.size() == 0) {
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_data_person));
                } else {
                    int count = 0;
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).memberList.size() == 0)
                            count++;
                    }
                    if (count == mData.size()) {
                        mAdapter.setNewData(null);
                        mAdapter.setEmptyView(R.layout.empty_view);
                        TextView textView = mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                        textView.setText(getString(R.string.empty_no_data_person));
                    } else {
                        mAdapter.setNewData(mData);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mRecyclerViewSelect.setVisibility(View.GONE);
                Drawable drawable = getResources().getDrawable(R.mipmap.d44_xiala);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                TextView tvChoice = item.getActionView().findViewById(R.id.tv_choice);
                tvChoice.setCompoundDrawables(null, null, drawable, null);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                mAdapter.setNewData(null);
                mAdapter.setEmptyView(R.layout.empty_view);
                TextView textView = mAdapter.getEmptyView().findViewById(R.id.tv_tips);
                textView.setText(getString(R.string.empty_no_data_person));
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
//                    loadData();
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

    private MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community_toolbar_choise, menu);
        item = menu.findItem(R.id.action_all);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
