package com.project.community.ui.life.wuye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.HouseModel;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by qizfeng on 17/8/21.
 * 添加房屋编号
 */

public class AddHouseNoActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_house_no)
    EditText mEtHouseNo;
    @Bind(R.id.btn_next)
    Button mBtnNext;
    private String title;
    public static Activity instance;
    public static Activity getInstance(){
        return instance;
    }
    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddHouseNoActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house_no);
        instance=this;
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_add_house), R.mipmap.iv_back);
        try {
            title = getIntent().getBundleExtra("bundle").getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RxView.clicks(mBtnNext)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onNextClick(mBtnNext);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            KeyBoardUtils.closeKeybord(mEtHouseNo, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNextClick(View v) {
        searchHouse();
    }

    private void searchHouse() {
        String houseNo = mEtHouseNo.getText().toString().trim();
        if (TextUtils.isEmpty(houseNo)) {
            showToast(getString(R.string.txt_add_house_no_hint));
            return;
        }
        serverDao.selectHouseInfo(houseNo, new DialogCallback<BaseResponse<HouseModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<HouseModel> baseResponse, Call call, Response response) {
                Bundle bundle = new Bundle();
                bundle.putString("houseNo", baseResponse.retData.getRoomNo());
                bundle.putString("address", baseResponse.retData.getAddress());
                bundle.putString("title",title);
//                AddHouseNoDialogActivity.startActivity(AddHouseNoActivity.this, bundle);
                Intent intent = new Intent(AddHouseNoActivity.this,AddHouseNoDialogActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
//                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }
}
