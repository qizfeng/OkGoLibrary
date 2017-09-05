package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.HouseModel;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddHouseNoActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house_no);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_add_house), R.mipmap.iv_back);
        try {
            title = getIntent().getBundleExtra("bundle").getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @OnClick(R.id.btn_next)
    public void onNextClick(View v) {
//        String houseNo = mEtHouseNo.getText().toString().trim();
//        if (TextUtils.isEmpty(houseNo)) {
//            showToast(getString(R.string.txt_add_house_no_hint));
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("houseNo", houseNo);
//        AddHouseNoDialogActivity.startActivity(this, bundle);
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
                AddHouseNoDialogActivity.startActivity(AddHouseNoActivity.this, bundle);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }
}
