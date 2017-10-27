package com.project.community.ui.life.minsheng;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.GoodsManagerModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.ShoppingCartAdapter;
import com.project.community.util.NetworkUtils;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

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

    private Dialog mDialog;

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
//        mData = getListData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShoppingCartAdapter(this, mData, new ShoppingCartAdapter.OnGoodsClickListener() {
            @Override
            public void onGoodsItemClick(View view,int parentPosition, int childPosition) {

            }
        }, new ShoppingCartAdapter.OnFooterClickListener() {
            @Override
            public void onFooterDeleteClick(View view, int position) {
                switch (view.getId()){
                    case R.id.tv_delete:
                        Log.e("onFooterDeleteClick: ", position+"-------");
                        showAlertDialog(position);
                        break;
                     case R.id.tv_settlement:
                        break;
                }
            }

            @Override
            public void onSettlementClick(View view, int position) {
                LogUtils.e("settlement:"+position);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        getData();
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

    /**
     * 获取购物车列表
     */
    private void getData(){

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }
        serverDao.getCartList(
                getUser(this).id,
                new JsonCallback<BaseResponse<List<ShoppingCartModel>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<ShoppingCartModel>> listBaseResponse, Call call, Response response) {
                        dismissDialog();
//                        showToast(listBaseResponse.message);
                        mData.clear();
                        mData.addAll(listBaseResponse.retData);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }
    /**
     * 删除
     */
    private void delData(String cartId, final int position){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.delCart(cartId,new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
                mDialog.dismiss();
                mAdapter.removeGroup(position);
                mData.remove(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 删除
     *
     * @param position
     */

    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = this.getWindowManager();
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
                delData(mData.get(position).id,position);
            }
        });
    }


}
