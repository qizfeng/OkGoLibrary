package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.MyUserBean;
import com.project.community.ui.adapter.MyUserAdapter;
import com.project.community.util.Cn2Spell;
import com.project.community.util.MyLetterListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：fangkai on 2017/10/31 15:22
 * em：617716355@qq.com
 */
public class MyUserActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.et_search_content)
    TextView etSearchContent;
    @Bind(R.id.recyler_cityList)
    RecyclerView recylerCityList;
    @Bind(R.id.letterListView)
    MyLetterListView letterListView;
    @Bind(R.id.tv_noData)
    TextView tvNoData;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;

    private MyUserAdapter myUserAdapter;

    private List<MyUserBean> user = new ArrayList<>();


    private List<MyUserBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        initToolBar(toolbar, tvTitle, true, getString(R.string.my_user), R.mipmap.iv_back);


        MyUserBean myUserBean = new MyUserBean();
        myUserBean.setName("说的");
        user.add(myUserBean);
        MyUserBean myUserBean1 = new MyUserBean();
        myUserBean1.setName("为");
        user.add(myUserBean1);

        for (int i = 0; i < 30; i++) {
            MyUserBean m = new MyUserBean();
            m.setName("在");
            user.add(m);
        }
        setAdapter();

    }


    // 26个字母
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};


    private void setAdapter() {

        for (int j = 0; j < user.size(); j++) {
            user.get(j).setPy(Cn2Spell.getPinYinFirstLetter(user.get(j).getName()).toUpperCase());
            Log.e("TAG_city", user.get(j).getPy());
        }


//        List<CityListBean> cityList = new ArrayList<>();
        for (int i = 0; i < b.length; i++) {
            for (int k = 0; k < user.size(); k++) {
                if (b[i].equals(user.get(k).getPy())) {
                    list.add(user.get(k));
                }
            }
        }

        myUserAdapter = new MyUserAdapter(this, list);
        letterListView.setOnTouchingLetterChangedListener(new MyLetterListView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                if (myUserAdapter.getLetterIndex().get(s) != null)
                    recylerCityList.scrollToPosition(myUserAdapter.getLetterIndex().get(s));


            }
        });

        recylerCityList.setLayoutManager(new LinearLayoutManager(this));
        recylerCityList.setAdapter(myUserAdapter);
    }

    @OnClick(R.id.rl_search)
    public void onViewClicked() {
        startActivity(new Intent( this,SearchMyUserActivity.class));
    }
}
