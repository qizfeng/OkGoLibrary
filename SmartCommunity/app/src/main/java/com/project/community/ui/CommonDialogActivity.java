package com.project.community.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.life.wuye.AddHouseNoDialogActivity;
import com.project.community.ui.life.wuye.PayDetailActivity;
import com.project.community.ui.life.wuye.PayIndexActivity;
import com.project.community.util.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zipingfang on 17/8/25.
 */

public class CommonDialogActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.tv_content)
    TextView mTvContent;

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddHouseNoDialogActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_common);
        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        // p.height = (int) (ScreenUtils.getScreenHeight(this) * 0.4); // 高度设置为屏幕的0.4
        p.width = (int) (ScreenUtils.getScreenWidth(this) * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle!=null){
            mTvContent.setText(bundle.getString("info"));
        }
    }

    @OnClick({R.id.btn_cancel, R.id.iv_close})
    public void onCancelClick(View v) {
        this.finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm(View v) {
    }
}
