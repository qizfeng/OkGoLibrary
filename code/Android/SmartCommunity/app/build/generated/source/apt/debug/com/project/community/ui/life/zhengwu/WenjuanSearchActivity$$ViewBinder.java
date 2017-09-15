// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WenjuanSearchActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.WenjuanSearchActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131624105, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131624300, "field 'pb'");
    target.pb = finder.castView(view, 2131624300, "field 'pb'");
    view = finder.findRequiredView(source, 2131624183, "field 'mAppbar'");
    target.mAppbar = finder.castView(view, 2131624183, "field 'mAppbar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624258, "field 'etSearchContent'");
    target.etSearchContent = finder.castView(view, 2131624258, "field 'etSearchContent'");
    view = finder.findRequiredView(source, 2131624107, "field 'tvCancel' and method 'onCancel'");
    target.tvCancel = finder.castView(view, 2131624107, "field 'tvCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancel(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624263, "field 'mListViewHistory'");
    target.mListViewHistory = finder.castView(view, 2131624263, "field 'mListViewHistory'");
    view = finder.findRequiredView(source, 2131624260, "field 'llHistory'");
    target.llHistory = finder.castView(view, 2131624260, "field 'llHistory'");
    view = finder.findRequiredView(source, 2131624265, "field 'llResult'");
    target.llResult = finder.castView(view, 2131624265, "field 'llResult'");
    view = finder.findRequiredView(source, 2131624259, "field 'ivClear' and method 'ClearContent'");
    target.ivClear = finder.castView(view, 2131624259, "field 'ivClear'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClearContent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624264, "field 'mClearHistoryBtn' and method 'ClearSearchHistory'");
    target.mClearHistoryBtn = finder.castView(view, 2131624264, "field 'mClearHistoryBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClearSearchHistory(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624262, "field 'mViewLineTop'");
    target.mViewLineTop = view;
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.pb = null;
    target.mAppbar = null;
    target.mTvTitle = null;
    target.etSearchContent = null;
    target.tvCancel = null;
    target.mListViewHistory = null;
    target.llHistory = null;
    target.llResult = null;
    target.ivClear = null;
    target.mClearHistoryBtn = null;
    target.mViewLineTop = null;
  }
}
