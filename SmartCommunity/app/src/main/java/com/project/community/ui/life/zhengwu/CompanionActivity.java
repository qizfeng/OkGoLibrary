package com.project.community.ui.life.zhengwu;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.DictionaryModel;
import com.project.community.model.DictionaryResponse;
import com.project.community.model.GuideModel;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.GuideAdapter;
import com.project.community.util.ScreenUtils;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/4.
 * 指南选择界面
 */

public class CompanionActivity extends BaseActivity implements View.OnClickListener {
    private static int SHOW_DIALOG_TYPE_DEPART = 1;
    private static int SHOW_DIALOG_TYPE_THEME = 2;
    private static int show_dialog_type = 1;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.layout_theme)
    RelativeLayout mLayoutTheme;
    @Bind(R.id.layout_depart)
    RelativeLayout mLayoutDepart;
    @Bind(R.id.layout_parent)
    LinearLayout layout_parent;
    @Bind(R.id.tv_depart)
    TextView mTvDepart;
    @Bind(R.id.tv_theme)
    TextView mTvTheme;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.btn_submit)
    Button mBtnSubmit;
    private LoopView mLoopView;
    private PopupWindow mPopupWindow;
    private GuideAdapter mAdapter;
    private List<GuideModel> mData = new ArrayList<>();
    private List<DictionaryModel> dictionaryModels = new ArrayList<>();
    private String theme;
    private String part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion);
        initView();
    }

    private void initView() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.title_masses_guide), R.mipmap.iv_back);
        mLayoutDepart.setOnClickListener(this);
        mLayoutTheme.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        mAdapter = new GuideAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CompanionActivity.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.title_masses_guide));
                bundle.putString("url", mData.get(position).guideContent);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        serverDao.getWorkGuide(theme, part, new DialogCallback<BaseResponse<List<GuideModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<GuideModel>> baseResponse, Call call, Response response) {

                mData = new ArrayList<>();
                mData.addAll(baseResponse.retData);
                if (mData.size() == 0)
                    showToast(getString(R.string.toast_no_data));
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_depart:
                show_dialog_type = SHOW_DIALOG_TYPE_DEPART;
                getDictionary(getString(R.string.txt_choise_depart), "gov_part");
                break;
            case R.id.layout_theme:
                show_dialog_type = SHOW_DIALOG_TYPE_THEME;
                getDictionary(getString(R.string.txt_choise_theme), "gov_theme");
                break;
            case R.id.tv_cancel:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_confirm:
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    if (show_dialog_type == SHOW_DIALOG_TYPE_DEPART) {
                        mTvDepart.setText(mLoopView.getCurrentItem().toString());
                        part = dictionaryModels.get(mLoopView.getSelectedItem()).value;
                    } else if (show_dialog_type == SHOW_DIALOG_TYPE_THEME) {
                        mTvTheme.setText(mLoopView.getCurrentItem().toString());
                        theme = dictionaryModels.get(mLoopView.getSelectedItem()).value;
                    }
                }
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(mTvDepart.getText()) && TextUtils.isEmpty(mTvTheme.getText())) {
                    return;
                }
                initData();
                break;
        }
    }

    /**
     * 获取字典数据
     *
     * @param type
     */
    private void getDictionary(final String title, final String type) {
        serverDao.getDictionaryData(type + "", new DialogCallback<BaseResponse<DictionaryResponse>>(this) {
            @Override
            public void onSuccess(BaseResponse<DictionaryResponse> baseResponse, Call call, Response response) {
                dictionaryModels = new ArrayList<>();
                dictionaryModels.addAll(baseResponse.retData.dictList);
                try {
                    showDialog(title, dictionaryModels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    private void showDialog(String title, List<DictionaryModel> dictionaryModels) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < dictionaryModels.size(); i++) {
            strings.add(dictionaryModels.get(i).label);
        }
        //初始化控件
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        mLoopView = (LoopView) inflate.findViewById(R.id.loopView);
        mLoopView.setItems(strings);
        mLoopView.setNotLoop();
        mLoopView.setCenterTextColor(getResources().getColor(R.color.txt_color));
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

            }
        });
        if (mPopupWindow == null)
            mPopupWindow = new PopupWindow(CompanionActivity.this);
        // 设置视图
        mPopupWindow.setContentView(inflate);
        // 设置弹出窗体的宽和高
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.popwin_comment_anim);
        mPopupWindow.showAtLocation(layout_parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
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
//                finish();
                CompanionSearchActivity.startActivity(CompanionActivity.this, null);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_actionbar, menu);
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
