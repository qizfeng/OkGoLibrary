package com.library.okgo.callback;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.library.okgo.R;
import com.library.okgo.request.BaseRequest;
import com.library.okgo.view.CustomProgress;

/**
 * ================================================
 * 作    者：qizfeng
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private CustomProgress progressDialog;
    private void initDialog(Activity activity) {
        progressDialog = new CustomProgress(activity,R.style.Custom_Progress);
    }

    public DialogCallback(Activity activity) {
        super();
        initDialog(activity);
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        try {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onAfter(@Nullable T t, @Nullable Exception e) {
        super.onAfter(t, e);
        //网络请求结束后关闭对话框
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
