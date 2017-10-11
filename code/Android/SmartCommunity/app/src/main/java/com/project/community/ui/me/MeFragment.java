package com.project.community.ui.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.model.UserModel;
import com.project.community.ui.life.family.FamilyAddActivity;
import com.project.community.ui.life.family.FamilyInfoActivity;
import com.project.community.ui.user.LoginActivity;
import com.project.community.ui.user.RegisterActivity;
import com.project.community.ui.user.SettingActivity;
import com.project.community.ui.user.UserInfoActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.view.RedPointDrawable;
import com.project.community.view.VpSwipeRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/12.
 * 我的
 */

public class MeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;

    @Bind(R.id.layout_header_bg)
    LinearLayout mLayoutHeaderBg;//顶部背景
    @Bind(R.id.iv_header)
    ImageView mIvHeader;//头像

    @Bind(R.id.tv_name)
    TextView mTvName;//用户名
    @Bind(R.id.btn_info)
    Button btn_info;//编辑用户信息按钮
    @Bind(R.id.layout_my_order)
    LinearLayout mLayoutMyOrder;//我的订单
    @Bind(R.id.layout_my_collect)
    LinearLayout mLayoutMyCollect;//我的收藏
    @Bind(R.id.layout_chat)
    RelativeLayout mLayoutChat;//即时聊天
    @Bind(R.id.tv_chat)
    TextView mTvChat;//用于即时聊天添加红点
    @Bind(R.id.layout_my_topic)
    LinearLayout mLayoutMyTopic;//我的帖子
    @Bind(R.id.layout_repair_record)
    RelativeLayout mLayoutRepairRecord;//报修记录
    @Bind(R.id.layout_family_info)
    RelativeLayout mLayoutFamilyInfo;//家庭信息
    @Bind(R.id.layout_shop_manage)
    RelativeLayout mLayoutShopManage;//商铺管理
    @Bind(R.id.btn_apply_shop)
    Button mBtnApplyShop;//申请商铺状态
    @Bind(R.id.layout_my_repair_order)
    RelativeLayout mLayoutMyRepairOrder;//我的维修单
    @Bind(R.id.tv_my_repair_order)
    TextView mTvMyRepairOrder;//用于维修单添加红点
    @Bind(R.id.layout_system_message)
    RelativeLayout mLayoutSystemMessage;//系统消息
    @Bind(R.id.tv_system_message)
    TextView mTvSystemMessage;
    //    @Bind(R.id.iv_header_bg)
//    ImageView mIvHeaderBg;
    @Bind(R.id.iv_chat_arrow)
    ImageView iv_chat_arrow;
    @Bind(R.id.layout_login)
    RelativeLayout mLayoutLogin;//已登录

    @Bind(R.id.layout_unlogin)//未登录
            LinearLayout mLayoutUnLogin;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.iv_setting)
    ImageView mIvSetting;
    @Bind(R.id.iv_toolbar_setting)
    ImageView mIvToolbarSetting;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_me, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        initToolbar(mToolBar, mTvTitle, "");
        /**
         * 监听 AppBarLayout Offset 变化，动态设置 SwipeRefreshLayout 是否可用
         */
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }

        });

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mIvHeader.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        mLayoutMyOrder.setOnClickListener(this);
        mLayoutMyCollect.setOnClickListener(this);
        mLayoutChat.setOnClickListener(this);
        mLayoutMyTopic.setOnClickListener(this);
        mLayoutRepairRecord.setOnClickListener(this);
        mLayoutFamilyInfo.setOnClickListener(this);
        mLayoutShopManage.setOnClickListener(this);
        mBtnApplyShop.setOnClickListener(this);
        mLayoutMyRepairOrder.setOnClickListener(this);
        mLayoutSystemMessage.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mIvSetting.setOnClickListener(this);
        mIvToolbarSetting.setOnClickListener(this);
        addRedPoint(mLayoutChat, true);
        addRedPoint(mLayoutMyRepairOrder, false);
        addRedPoint(mLayoutSystemMessage, false);
        mLayoutMyOrder.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) / 3 - 40;
        mLayoutMyCollect.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) / 3 - 40;
        mLayoutMyTopic.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) / 3 - 40;
        if(!isLogin(getActivity())){
            mLayoutMyRepairOrder.setVisibility(View.GONE);
        }else {
            if("3".equals(getUser(getActivity()).roleType)){
                mLayoutMyRepairOrder.setVisibility(View.VISIBLE);
            }else {
                mLayoutMyRepairOrder.setVisibility(View.GONE);
            }
        }
        // glide.onDisplayImageBlur(getActivity(), mIvHeaderBg, R.mipmap.iv_header_bg, 25);
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissDialog();
        if (isLogin(getActivity())) {
            mLayoutLogin.setVisibility(View.VISIBLE);
            mLayoutUnLogin.setVisibility(View.GONE);
//            setRefreshing(true);
            loadData();
//            onRefresh();
        } else {
            mIvHeader.setImageResource(R.mipmap.d54_tx);
            mLayoutLogin.setVisibility(View.GONE);
            mLayoutUnLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if(!isLogin(getActivity())){
            mLayoutMyRepairOrder.setVisibility(View.GONE);
        }else {
            if("3".equals(getUser(getActivity()).roleType)){
                mLayoutMyRepairOrder.setVisibility(View.VISIBLE);
            }else {
                mLayoutMyRepairOrder.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header://头像
                Intent intent = new Intent(getActivity(), MyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_info://编辑资料
                UserInfoActivity.startActivity(getActivity());
                break;
            case R.id.layout_my_order://我的订单
                showToast(getString(R.string.toast_online));
                break;
            case R.id.layout_my_collect://我的收藏
                showToast(getString(R.string.toast_online));
                break;
            case R.id.layout_chat://即时聊天
                break;
            case R.id.layout_my_topic://我的帖子
                showToast(getString(R.string.toast_online));
                break;
            case R.id.layout_repair_record://维修记录
                showToast(getString(R.string.toast_online));
                break;
            case R.id.layout_family_info://家庭信息
                if (isLogin(getActivity()))
                    FamilyInfoActivity.startActivity(getActivity(), null);
                else
                    showToast(getString(R.string.toast_no_login));
                break;
            case R.id.layout_shop_manage://店铺管理
                showToast(getString(R.string.toast_online));
                break;
            case R.id.btn_apply_shop://申请店铺
                break;
            case R.id.layout_my_repair_order://我的维修单
                showToast(getString(R.string.toast_online));
                break;
            case R.id.layout_system_message://系统消息
                showToast(getString(R.string.toast_online));
                break;
            case R.id.btn_login:
                LoginActivity.startActivity(getActivity());
                break;
            case R.id.btn_register:
                RegisterActivity.startActivity(getActivity());
                break;
            case R.id.iv_setting:
                SettingActivity.startActivity(getActivity());
                break;
            case R.id.iv_toolbar_setting:
                SettingActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                SettingActivity.startActivity(getActivity());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onRefresh() {

        if (isLogin(getActivity()))
            loadData();
        else
            setRefreshing(false);
    }


    /**
     * 向view上添加紅點
     *
     * @param view
     * @param isShowRedPoint true显示红点 false隐藏红点
     */
    private void addRedPoint(View view, boolean isShowRedPoint) {
        RedPointDrawable redPointDrawable = RedPointDrawable.wrap(getActivity(), getActivity().getResources().getDrawable(R.mipmap.white_bg), 10);
        redPointDrawable.setGravity(Gravity.CENTER_VERTICAL);
        redPointDrawable.setShowRedPoint(isShowRedPoint);
        view.setBackground(redPointDrawable);
    }

    private void loadData() {
        if (!isLogin(getActivity())) {
            setRefreshing(false);
            return;
        }
        serverDao.getUserInfo(getUser(getActivity()).id, new JsonCallback<BaseResponse<UserModel>>() {
            @Override
            public void onSuccess(BaseResponse<UserModel> userResponseBaseResponse, Call call, Response response) {
                Gson gson = new Gson();
                String userStr = gson.toJson(userResponseBaseResponse.retData);
                saveUser(getActivity(), userStr);
                Glide.with(getActivity())
                        .load(AppConstants.HOST + userResponseBaseResponse.retData.photo)
                        .placeholder(R.mipmap.d54_tx)
                        .bitmapTransform(new CropCircleTransformation(getActivity()))
                        .into(mIvHeader);
                mTvName.setText(getUser(getActivity()).loginName);
                if(!isLogin(getActivity())){
                    mLayoutMyRepairOrder.setVisibility(View.GONE);
                }else {
                    if("3".equals(getUser(getActivity()).roleType)){
                        mLayoutMyRepairOrder.setVisibility(View.VISIBLE);
                    }else {
                        mLayoutMyRepairOrder.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }

            @Override
            public void onAfter(BaseResponse<UserModel> userModelBaseResponse, Exception e) {
                super.onAfter(userModelBaseResponse, e);
                setRefreshing(false);
                dismissDialog();


            }
        });
    }

    /**
     * 设置是否刷新动画
     *
     * @param refreshing true开始刷新动画 false结束刷新动画
     */
    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }
}
