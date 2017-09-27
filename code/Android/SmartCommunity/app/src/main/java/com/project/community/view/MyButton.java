package com.project.community.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by cj on 2016/7/28.
 */
@SuppressLint("AppCompatCustomView")
public class MyButton extends Button {
    Drawable drawable = getBackground();

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawable.setAlpha(125);
                setBackground(drawable);
                break;
            case MotionEvent.ACTION_UP:
                drawable.setAlpha(255);
                setBackground(drawable);
                break;
            case MotionEvent.ACTION_CANCEL:
                drawable.setAlpha(255);
                setBackground(drawable);

                break;

        }
        return super.onTouchEvent(event);
    }




}
