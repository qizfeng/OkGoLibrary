package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.RepairsRecordBean;
import com.project.community.constants.AppConstants;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/24 18:09
 * em：617716355@qq.com
 */
public class RepairsEvaluateActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.ratingBar)
    XLHRatingBar ratingBar;
    @Bind(R.id.et_evaluate)
    EditText etEvaluate;
    @Bind(R.id.tv_size)
    TextView tvSize;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_times)
    TextView tvTimes;
    @Bind(R.id.tv_orderType)
    TextView tvOrderType;
    @Bind(R.id.tv_orderNo)
    TextView tvOrderNo;
    @Bind(R.id.tv_roomNo)
    TextView tvRoomNo;
    @Bind(R.id.tv_roomAddress)
    TextView tvRoomAddress;

    private int rating = 5;

    private RepairsRecordBean item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_evaluate);
        ButterKnife.bind(this);
        setTitles();
        addlistening();
    }

    private void addlistening() {

        item = EventBus.getDefault().getStickyEvent(RepairsRecordBean.class);

        if (item != null)
            setView();


        ratingBar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                rating = countSelected;
            }
        });

        etEvaluate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /**
                 * 编辑框的内容发生改变之前的回调方法
                 */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
                 * 我们可以在这里实时地 通过搜索匹配用户的输入
                 */
            }

            @Override
            public void afterTextChanged(Editable s) {
                /**
                 * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
                 */
                int nuber = s.length();
                tvSize.setText(nuber + "/350字");
            }
        });


    }


    /**
     * 适配数据
     */
    private void setView() {

        if (item.getCreateDate() != null && item.getCreateDate().length() >= 10)
            tvTime.setText(item.getCreateDate().substring(0, 5));
        if (item.getCreateDate() != null && item.getCreateDate().length() >= 10)
            tvTimes.setText(item.getCreateDate().substring(5, 10));
        tvOrderType.setText("【" + item.getOrderType() + "】");
        tvOrderNo.setText("订单号：" + item.getOrderNo());
        tvRoomNo.setText("房屋编号：" + item.getRoomNo());
        tvRoomAddress.setText(item.getRoomAddress());


    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "报修评价", R.mipmap.iv_back);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setTitle("提交").setIcon(null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                submit();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * 提交
     */
    private void submit() {

        if (TextUtils.isEmpty(etEvaluate.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入评价内容");
            return;
        }


//        serverDao.proRepairCommentSave(getUser(this).id, String.valueOf(page),
//                String.valueOf(AppConstants.PAGE_SIZE),
//                new JsonCallback<BaseResponse<List<RepairsRecordBean>>>() {
//                    @Override
//                    public void onSuccess(BaseResponse<List<RepairsRecordBean>> objectBaseResponse, Call call, Response response) {
//
//                        if (swipeRl != null)
//                            swipeRl.setRefreshing(false);
//
//                        mData.addAll(objectBaseResponse.retData);
//                        if (page == 1) {
//                            repairsRecordAdapter.setNewData(mData);
//                            repairsRecordAdapter.setEnableLoadMore(true);
//                        } else {
//                            repairsRecordAdapter.addData(objectBaseResponse.retData);
//                            repairsRecordAdapter.loadMoreComplete();
//                        }
//                        if (objectBaseResponse.retData.size() < AppConstants.PAGE_SIZE)
//                            repairsRecordAdapter.loadMoreEnd();
//
//
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        Log.e("tag_f", e.getMessage().toString() + "");
//                        if (swipeRl != null)
//                            swipeRl.setRefreshing(false);
//                    }
//                });


    }
}
