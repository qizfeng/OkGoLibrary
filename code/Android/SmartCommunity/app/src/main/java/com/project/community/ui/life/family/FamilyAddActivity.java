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
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

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
        showAlertDialog();
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

    public void showAlertDialog() {
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
        Window window=mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_title =(TextView) mDialog.getWindow().findViewById(R.id.tv_title);
        tv_title.setText("房屋编码");
        TextView tv_content = (TextView)mDialog.getWindow().findViewById(R.id.tv_content);
        tv_content.setText("6567899875\n一区十六号楼七单元601室");
        Button btn_confirm =(Button) mDialog.getWindow().findViewById(R.id.btn_confirm);
        Button btn_cancel =(Button) mDialog.getWindow().findViewById(R.id.btn_cancel);
        ImageView iv_close =(ImageView) mDialog.getWindow().findViewById(R.id.iv_close);
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
                showToast("添加成功");
                Bundle bundle = new Bundle();
                bundle.putString("houseName", mEtHouseName.getText().toString());
                FamilyInfoActivity.startActivity(FamilyAddActivity.this, bundle);
            }
        });
    }

}
