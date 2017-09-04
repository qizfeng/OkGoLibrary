package com.library.okgo.view.loopview;
import android.view.MotionEvent;
/**
 * Created by qizfeng on 17/8/23.
 */

final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final LoopView loopView;

    LoopViewGestureListener(LoopView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}

