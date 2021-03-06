package com.project.community.ui.me.all_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;
import com.project.community.ui.adapter.GoodsOrderDetailApdater;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/10/24.
 * 收货评价
 */

public class TakeDeliveryOfGoodsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.recylerview)
    RecyclerView recylerview;
    @Bind(R.id.ratingBar)
    RatingBar mRatingBar;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.build_new_tv_describe_num)
    TextView buildNewTvDescribeNum;

    private GoodsOrderDetailApdater mAdapter;//商品详情订单适配器
    List<GoodsModel> list =new ArrayList<>();
    private float rating=4.0f;
    private OrderModel orderModel;

    public static void startActivity(Context context ,OrderModel orderModel) {
        Intent intent = new Intent(context, TakeDeliveryOfGoodsActivity.class);
        intent.putExtra("item",orderModel);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_delivery_of_goods);
        ButterKnife.bind(this);
//        clickRatingBar();
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.take_delivery_of_goods_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        orderModel= (OrderModel) getIntent().getSerializableExtra("item");
        list.addAll(orderModel.detailList);

        mRatingBar.setStar(4.0f);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {
//                        Toast.makeText(EvaluateAty.this, "the fill star is" + RatingCount, Toast.LENGTH_SHORT).show();
                        rating = RatingCount;
                        Log.e("rating   =   " ,"------>"+ RatingCount);
                    }
                }
        );

        recylerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoodsOrderDetailApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onCustomClick(View view, int position) {
            }
        });
        recylerview.setAdapter(mAdapter);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setTitle(R.string.take_delivery_of_goods_commit);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/
            case R.id.action_favorite:
                if (rating==0.0f){
                    ToastUtil.showToast(TakeDeliveryOfGoodsActivity.this,getString(R.string.take_delivery_of_goods_ratingbar_hit));
                    return false;
                }
                if (TextUtils.isEmpty(etComment.getText().toString())){
                    ToastUtil.showToast(TakeDeliveryOfGoodsActivity.this,getString(R.string.take_delivery_of_goods_edit_hit));
                    return false;
                }
                comment();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * D58评价
     */

    private void comment() {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.comment(
                getUser(this).id,
                orderModel.orderNo,
                orderModel.shopId,
                rating,
                etComment.getText().toString(),
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
                        Intent intent = new Intent();
                        CommentModel commentModel = new CommentModel();
                        commentModel.rating = rating;
                        intent.putExtra("value",commentModel);
                        setResult(100,intent);
                        finish();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }




    private void clickRatingBar() {

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.mipmap.d29_icon1_p));
        mRatingBar.setStarHalfDrawable(getResources().getDrawable(R.mipmap.d27_banxing));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.mipmap.d29_icon1));
        mRatingBar.setStarCount(5);
        mRatingBar.setStar(2.5f);
        mRatingBar.halfStar(true);
        mRatingBar.setmClickable(true);
        mRatingBar.setStarImageWidth(120f);
        mRatingBar.setStarImageHeight(60f);
        mRatingBar.setImagePadding(35);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {
//                        Toast.makeText(EvaluateAty.this, "the fill star is" + RatingCount, Toast.LENGTH_SHORT).show();
//                        rating = (int) RatingCount;
//                        Log.e(TAG,"rating   =   " + rating);
                    }
                }
        );

    }
}
