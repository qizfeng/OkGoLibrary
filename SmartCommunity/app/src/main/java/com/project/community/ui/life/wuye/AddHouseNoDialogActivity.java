package com.project.community.ui.life.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.HouseModel;
import com.project.community.util.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/21.
 * 添加房屋编码弹窗对话框
 */

public class AddHouseNoDialogActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    private String houseNo;
    private String address;
    private String title;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddHouseNoDialogActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_house_no);
        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        // p.height = (int) (ScreenUtils.getScreenHeight(this) * 0.4); // 高度设置为屏幕的0.4
        p.width = (int) (ScreenUtils.getScreenWidth(this) * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            houseNo = bundle.getString("houseNo");
            address = bundle.getString("address");
            title = bundle.getString("bundle");
            try {
                mTvContent.setText(houseNo + "\n" + address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.btn_cancel, R.id.iv_close})
    public void onCancelClick(View v) {
        this.finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm(View v) {
        addHouse();
    }


    /**
     * 添加房屋
     */
    private void addHouse() {
        serverDao.addHouse(getUser(this).id, houseNo, new DialogCallback<BaseResponse<HouseModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<HouseModel> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
                Bundle bundle = getIntent().getBundleExtra("bundle");
                bundle.putString("title", bundle.getString("title"));
                if ("物业费".equals(title))
                    PayDetailWuyeActivity.startActivity(AddHouseNoDialogActivity.this, bundle);
                else
                    PayDetailActivity.startActivity(AddHouseNoDialogActivity.this, bundle);
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
