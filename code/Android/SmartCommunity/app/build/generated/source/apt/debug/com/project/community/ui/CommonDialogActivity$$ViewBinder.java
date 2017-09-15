// Generated code from Butter Knife. Do not modify!
package com.project.community.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommonDialogActivity$$ViewBinder<T extends com.project.community.ui.CommonDialogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624168, "field 'mIvClose' and method 'onCancelClick'");
    target.mIvClose = finder.castView(view, 2131624168, "field 'mIvClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancelClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624169, "field 'mTvContent'");
    target.mTvContent = finder.castView(view, 2131624169, "field 'mTvContent'");
    view = finder.findRequiredView(source, 2131624170, "method 'onCancelClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancelClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624171, "method 'onConfirm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onConfirm(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTvTitle = null;
    target.mIvClose = null;
    target.mTvContent = null;
  }
}
