package com.project.community.view.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.photo.PhotoUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zipingfang on 17/9/27.
 */

public class CropImageActivity extends BaseActivity implements View.OnClickListener {
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
        tv_finish.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    public static Bitmap mBitmap;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_finish:
                mBitmap = mView.clip();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, null, null));
                intent.putExtra("uri", uri.toString());
//                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    public void onCancel(View view) {
        finish();
    }

    public void onResult(View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bitmap mBitmap = mView.clip();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bitmap", mBitmap);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
