package com.project.community.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.library.okgo.utils.KeyBoardUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.MyUserBean;
import com.project.community.ui.adapter.SearchMyUserAdapter;
import com.project.community.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * author：fangkai on 2017/10/31 20:52
 * em：617716355@qq.com
 */
public class SearchMyUserActivity extends BaseActivity {


    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.iv_clear_content)
    ImageView ivClearContent;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.global_search_action_bar_rl)
    RelativeLayout globalSearchActionBarRl;
    @Bind(R.id.rc_view)
    RecyclerView rcView;

//    private List<MyUserBean> user = new ArrayList<>();

    private SearchMyUserAdapter searchMyUserAdapter;
    private List<MyUserBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_my_user);
        ButterKnife.bind(this);

        setListener();

    }

    private void setListener() {

        etSearchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (i == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    KeyBoardUtils.closeKeybord(etSearchContent, SearchMyUserActivity.this);
                    etSearchContent.setCursorVisible(false);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    RxView.clicks(view)
                            .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                            .subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
//                            if (index==2)
//                                showLoading();
                                    StartSearch();
                                }
                            });

                }
                return false;
            }
        });
    }

    private void StartSearch() {
        if (TextUtils.isEmpty(etSearchContent.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入搜索内容");
        }

        setAdapter();


    }

    private void setAdapter() {

        mData = new ArrayList<>();

        MyUserBean myUserBean = new MyUserBean();
        myUserBean.setName("xxx");

        mData.add(myUserBean);
        searchMyUserAdapter = new SearchMyUserAdapter(this, mData);


        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(searchMyUserAdapter);
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        finish();
    }
}
