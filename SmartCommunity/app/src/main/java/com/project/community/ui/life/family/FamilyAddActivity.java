package com.project.community.ui.life.family;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.platform.comapi.map.B;
import com.baidu.platform.comapi.map.D;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.AuditStatusModel;
import com.project.community.model.HouseModel;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/25.
 * 添加家庭
 */

public class FamilyAddActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_house_name)
    EditText mEtHouseName;
    @Bind(R.id.et_house_no)
    EditText mEtHouseNo;
    @Bind(R.id.btn_next)
    Button mBtnNext;
    private Dialog mDialog;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FamilyAddActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_family_add), R.mipmap.iv_back);

    }

    @OnClick(R.id.btn_next)
    public void onNext(View view) {
//        checkOwner();
        getHouseInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.closeKeybord(mEtHouseName, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void showAlertDialog(HouseModel houseModel) {
        if (TextUtils.isEmpty(mEtHouseName.getText())) {
            showToast(getString(R.string.txt_add_family_name_hint));
            return;
        }
        if (TextUtils.isEmpty(mEtHouseNo.getText())) {
            showToast(getString(R.string.txt_add_family_no_hint));
            return;
        }
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_title = (TextView) mDialog.getWindow().findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.txt_dialog_house_no));
        TextView tv_content = (TextView) mDialog.getWindow().findViewById(R.id.tv_content);
        tv_content.setText(houseModel.getRoomNo() + "\n" + houseModel.getAddress());
        Button btn_confirm = (Button) mDialog.getWindow().findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.getWindow().findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.getWindow().findViewById(R.id.iv_close);
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
                mDialog.dismiss();
                checkOwner();
//                addFamily();

            }
        });
    }

    /**
     * 未审核提示框
     */
    private void showUnAuditDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.getWindow().findViewById(R.id.tv_content);
        tv_content.setText(getString(R.string.txt_dialog_tips));
        Button btn_confirm = (Button) mDialog.getWindow().findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.getWindow().findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.getWindow().findViewById(R.id.iv_close);
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
                auditFamily();
                mDialog.dismiss();

            }
        });
    }

    /**
     * 提交业主审核
     */
    private void auditFamily() {
        String familyName = mEtHouseName.getText().toString();
        if (TextUtils.isEmpty(familyName)) {
            showToast(getString(R.string.txt_add_family_name_hint));
            return;
        }
        String roomNo = mEtHouseNo.getText().toString();
        if (TextUtils.isEmpty(roomNo)) {
            showToast(getString(R.string.txt_add_family_no_hint));
            return;
        }
        serverDao.auditFamily(getUser(this).id, familyName, roomNo, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }


    /**
     * 检查业主审核状态
     */
    private void checkOwner() {
        String familyName = mEtHouseName.getText().toString();
        if (TextUtils.isEmpty(familyName)) {
            showToast(getString(R.string.txt_add_family_name_hint));
            return;
        }
        String roomNo = mEtHouseNo.getText().toString();
        if (TextUtils.isEmpty(roomNo)) {
            showToast(getString(R.string.txt_add_family_no_hint));
            return;
        }
        serverDao.checkOwner(roomNo, getUser(this).id, getUsername(this), new DialogCallback<BaseResponse<AuditStatusModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<AuditStatusModel> baseResponse, Call call, Response response) {
                //auditStatus 0.未提交审核 1.未审核/审核中,2.审核通过,3.未通过
                String auditStatus = baseResponse.retData.auditStatus;
                if ("0".equals(auditStatus)) {//没有提交过审核
                    showUnAuditDialog();//未提交审核dialog
                } else if ("1".equals(auditStatus)) {//审核中/未审核
                    showToast(getString(R.string.txt_family_auditing));
                } else if ("2".equals(auditStatus)) {//只针对个人信息房屋,已审核通过的
                    addFamily();//添加家庭
                } else if ("3".equals(auditStatus)) {//审核不通过
                    showToast(getString(R.string.txt_family_audit_error));
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }


    /**
     * 查询房屋信息
     */
    private void getHouseInfo() {
        String roomNo = mEtHouseNo.getText().toString();
        serverDao.selectHouseInfo(roomNo, new DialogCallback<BaseResponse<HouseModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<HouseModel> baseResponse, Call call, Response response) {
                if (baseResponse.retData != null)
                    showAlertDialog(baseResponse.retData);
                else
                    showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }
        });
    }

    /**
     * 添加家庭
     */
    private void addFamily() {
        String familyName = mEtHouseName.getText().toString();
        if (TextUtils.isEmpty(familyName)) {
            showToast(getString(R.string.txt_add_family_name_hint));
            return;
        }
        String roomNo = mEtHouseNo.getText().toString();
        if (TextUtils.isEmpty(roomNo)) {
            showToast(getString(R.string.txt_add_family_no_hint));
            return;
        }
        serverDao.addFamily(getUser(this).id, roomNo, familyName, new DialogCallback<BaseResponse<HouseModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<HouseModel> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }

        });
    }
}
