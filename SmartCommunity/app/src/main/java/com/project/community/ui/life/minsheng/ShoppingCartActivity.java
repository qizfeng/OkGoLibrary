package com.project.community.ui.life.minsheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.GoodsModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.ShoppingCartAdapter;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/10/17.
 * 购物车
 */

public class ShoppingCartActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private ShoppingCartAdapter mAdapter;
    private List<ShoppingCartModel> mData = new ArrayList<>();

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ShoppingCartActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_shopping_cart), R.mipmap.iv_back);
        init();
    }

    private void init() {
        mData = getListData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShoppingCartAdapter(this, mData, new ShoppingCartAdapter.OnGoodsClickListener() {
            @Override
            public void onGoodsItemClick(View view,int parentPosition, int childPosition) {

            }
        }, new ShoppingCartAdapter.OnFooterClickListener() {
            @Override
            public void onFooterDeleteClick(View view, int position) {
                LogUtils.e("delete:"+position);
            }

            @Override
            public void onSettlementClick(View view, int position) {
                LogUtils.e("settlement:"+position);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    public ArrayList<ShoppingCartModel> getListData() {
        ArrayList<ShoppingCartModel> myParents = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ShoppingCartModel myParent = getShoppingCart();
            myParents.add(myParent);
        }
        return myParents;
    }

    public ShoppingCartModel getShoppingCart() {
        ShoppingCartModel myParent = new ShoppingCartModel();
        ArrayList<GoodsModel> myChildren = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            GoodsModel myChild = new GoodsModel();
            myChildren.add(myChild);
        }
        myParent.goods = myChildren;
        return myParent;
    }
}
