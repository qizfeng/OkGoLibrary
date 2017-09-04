// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TopicDetailActivity$$ViewBinder<T extends com.project.community.ui.life.TopicDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624197, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624197, "field 'refreshLayout'");
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624270, "field 'mEtInput'");
    target.mEtInput = finder.castView(view, 2131624270, "field 'mEtInput'");
    view = finder.findRequiredView(source, 2131624271, "field 'mBtnSend'");
    target.mBtnSend = finder.castView(view, 2131624271, "field 'mBtnSend'");
    view = finder.findRequiredView(source, 2131624268, "field 'mCoordinatorLayout'");
    target.mCoordinatorLayout = finder.castView(view, 2131624268, "field 'mCoordinatorLayout'");
    view = finder.findRequiredView(source, 2131624269, "field 'mBottomLayout'");
    target.mBottomLayout = finder.castView(view, 2131624269, "field 'mBottomLayout'");
  }

  @Override public void unbind(T target) {
    target.refreshLayout = null;
    target.recyclerView = null;
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mEtInput = null;
    target.mBtnSend = null;
    target.mCoordinatorLayout = null;
    target.mBottomLayout = null;
  }
}
