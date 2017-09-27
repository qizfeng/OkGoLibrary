package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.ArticleDetailsImagsAdapter;
import com.project.community.ui.adapter.OrderDetailShouliApdater;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.order_detail_address)
    TextView orderDetailAddress;
    @Bind(R.id.order_detail_name)
    TextView orderDetailName;
    @Bind(R.id.order_detail_type)
    TextView orderDetailType;
    @Bind(R.id.order_detail_content)
    TextView orderDetailContent;
    @Bind(R.id.order_detail_imgs)
    GridView orderDetailImgs;
    @Bind(R.id.order_detail_time)
    TextView orderDetailTime;
    @Bind(R.id.order_detail_jiaoyi_danhao)
    TextView orderDetailJiaoyiDanhao;
    @Bind(R.id.order_detail_xiadan_time)
    TextView orderDetailXiadanTime;
    @Bind(R.id.order_detail_recylerview)
    RecyclerView orderDetailRecylerview;
    @Bind(R.id.order_detail_et_dispose)
    EditText orderDetailEtDispose;
    @Bind(R.id.order_detail_add_imgs)
    GridView orderDetailAddImgs;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private List<String> mImages = new ArrayList<>();
    private List<String> mAddImages = new ArrayList<>();
    private List<CommentModel> mShouliList =new ArrayList<>();
    ArticleDetailsImagsAdapter grid_photoAdapter;
    ArticleDetailsImagsAdapter grid_photoAdapterAdd;
    OrderDetailShouliApdater orderDetailShouliApdater;
    private MenuItem menuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.order_detail), R.mipmap.iv_back);
        for (int i = 0; i < 6; i++) {
            mImages.add("");
            mAddImages.add("");
        }
        for (int i = 0; i < 10; i++) {
            CommentModel commentModel =new CommentModel();
            if (i==6){
                commentModel.id="1";
            }else {
                commentModel.id="0";
            }
            mShouliList.add(commentModel);
        }
        orderDetailShouliApdater=new OrderDetailShouliApdater(mShouliList, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
//        orderDetailRecylerview.setItemAnimator(new DefaultItemAnimator());
        orderDetailRecylerview.setLayoutManager(new LinearLayoutManager(this));
//        SpacesItemDecoration decoration = new SpacesItemDecoration(0, false);
//        orderDetailRecylerview.addItemDecoration(decoration);
        orderDetailRecylerview.setAdapter(orderDetailShouliApdater);

        grid_photoAdapter=new ArticleDetailsImagsAdapter(this,mImages);
        grid_photoAdapterAdd=new ArticleDetailsImagsAdapter(this,mAddImages);
        orderDetailImgs.setAdapter(grid_photoAdapter);
        orderDetailAddImgs.setAdapter(grid_photoAdapterAdd);

    }

    @OnClick(R.id.order_detail_complete)
    public void onViewClicked() {
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
//        menuItem = menu.findItem(R.id.navigation_tel);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_tel:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
