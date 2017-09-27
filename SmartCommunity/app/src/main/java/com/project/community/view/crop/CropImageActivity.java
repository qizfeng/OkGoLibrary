package com.project.community.view.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.photo.PhotoUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.sina.weibo.sdk.utils.ImageUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zipingfang on 17/9/27.
 */

public class CropImageActivity extends BaseActivity {
    private ClipImageLayout mView;
    private Uri uri;
    private int width = 480;
    private int height = 480;
    @Bind(R.id.tv_cancel)
    TextView tv_cancel;
    @Bind(R.id.tv_finish)
    TextView tv_finish;

    public static void startCrop(Activity activity, String orgUri, int width, int height, int requestCode) {
        Intent intent = new Intent(activity, CropImageActivity.class);
        intent.putExtra("uri", orgUri);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mView = (ClipImageLayout) findViewById(R.id.cropImage);
        uri = Uri.parse(getIntent().getStringExtra("uri"));
        Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri, this);
        mView.setImageDrawable(PhotoUtils.bitmapToDrawable(bitmap));

    }


    @OnClick(R.id.tv_cancel)
    public void onCancel(View view) {
        finish();
    }

    @OnClick(R.id.tv_finish)
    public void onResult(View view) {
        showToast("点击了");
        Bitmap mBitmap = mView.clip();
        LogUtils.e("bitmap:" + mBitmap.getWidth() + "," + mBitmap.getHeight());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", mBitmap);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
