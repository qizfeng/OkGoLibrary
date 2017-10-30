package com.project.community.ui.me;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.RoomList;
import com.project.community.model.HouseModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import rx.functions.Action1;


/**
 * author：fangkai on 2017/10/30 15:52
 * em：617716355@qq.com
 */
public class AddHouseNumberActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.et_house_no)
    EditText etHouseNo;
    @Bind(R.id.btn_next)
    Button mBtnNext;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house_number);
        ButterKnife.bind(this);
        initToolBar(toolbar, tvTitle, true, getString(R.string.add_house_number), R.mipmap.iv_back);

        RxView.clicks(mBtnNext)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onNext(mBtnNext);
                    }
                });
    }

    public void onNext(View view) {
        addFamily();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.closeKeybord(etHouseNo, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    /**
     * 添加家庭
     */
    private void addFamily() {
        String roomNo = etHouseNo.getText().toString();
        if (TextUtils.isEmpty(roomNo)) {
            showToast(getString(R.string.txt_add_family_no_hint));
            return;
        }
        serverDao.saveHouseNumber(roomNo, getUser(this).id, new JsonCallback<BaseResponse<RoomList>>() {
            @Override
            public void onSuccess(BaseResponse<RoomList> listBaseResponse, Call call, Response response) {
                showToast(listBaseResponse.message);
                if ( listBaseResponse.errNum.equals("0")){
                    finish();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                showToast(e.getMessage());
            }
        });
    }
}
