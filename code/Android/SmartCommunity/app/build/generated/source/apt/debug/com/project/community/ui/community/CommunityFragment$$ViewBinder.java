// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.community;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommunityFragment$$ViewBinder<T extends com.project.community.ui.community.CommunityFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624115, "field 'mIvFlash'");
    target.mIvFlash = finder.castView(view, 2131624115, "field 'mIvFlash'");
    view = finder.findRequiredView(source, 2131624112, "field 'mIvBack'");
    target.mIvBack = finder.castView(view, 2131624112, "field 'mIvBack'");
    view = finder.findRequiredView(source, 2131624109, "field 'mDrawerLayout'");
    target.mDrawerLayout = finder.castView(view, 2131624109, "field 'mDrawerLayout'");
    view = finder.findRequiredView(source, 2131624114, "field 'mIvDrawer'");
    target.mIvDrawer = finder.castView(view, 2131624114, "field 'mIvDrawer'");
    view = finder.findRequiredView(source, 2131624116, "field 'mDrawerRight'");
    target.mDrawerRight = finder.castView(view, 2131624116, "field 'mDrawerRight'");
    view = finder.findRequiredView(source, 2131624117, "field 'mDrawerRightContent'");
    target.mDrawerRightContent = finder.castView(view, 2131624117, "field 'mDrawerRightContent'");
    view = finder.findRequiredView(source, 2131624118, "field 'mDrawerBottom'");
    target.mDrawerBottom = finder.castView(view, 2131624118, "field 'mDrawerBottom'");
  }

  @Override public void unbind(T target) {
    target.mIvFlash = null;
    target.mIvBack = null;
    target.mDrawerLayout = null;
    target.mIvDrawer = null;
    target.mDrawerRight = null;
    target.mDrawerRightContent = null;
    target.mDrawerBottom = null;
  }
}