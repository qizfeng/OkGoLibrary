// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.wuye;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PaymentDialogActivity$$ViewBinder<T extends com.project.community.ui.life.wuye.PaymentDialogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624164, "field 'mIvClose' and method 'onCancelClick'");
    target.mIvClose = finder.castView(view, 2131624164, "field 'mIvClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancelClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624171, "field 'mLayoutAlipay'");
    target.mLayoutAlipay = finder.castView(view, 2131624171, "field 'mLayoutAlipay'");
    view = finder.findRequiredView(source, 2131624174, "field 'mLayoutWeipay'");
    target.mLayoutWeipay = finder.castView(view, 2131624174, "field 'mLayoutWeipay'");
    view = finder.findRequiredView(source, 2131624173, "field 'mBtnAlipay'");
    target.mBtnAlipay = finder.castView(view, 2131624173, "field 'mBtnAlipay'");
    view = finder.findRequiredView(source, 2131624175, "field 'mBtnWeipay'");
    target.mBtnWeipay = finder.castView(view, 2131624175, "field 'mBtnWeipay'");
    view = finder.findRequiredView(source, 2131624166, "method 'onCancelClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancelClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624167, "method 'onConfirm'");
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
    target.mLayoutAlipay = null;
    target.mLayoutWeipay = null;
    target.mBtnAlipay = null;
    target.mBtnWeipay = null;
  }
}