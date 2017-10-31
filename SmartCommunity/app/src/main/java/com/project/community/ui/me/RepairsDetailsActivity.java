package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.HomeNumberAdapter;
import com.project.community.ui.adapter.OrderDetailShouliApdater;
import com.project.community.ui.adapter.RepairsDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/25 17:50
 * em：617716355@qq.com
 */
public class RepairsDetailsActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_evaluate)
    TextView tvEvaluate;
    @Bind(R.id.bbs_item_ll_three_img_1)
    ImageView bbsItemLlThreeImg1;
    @Bind(R.id.bbs_item_ll_three_img_2)
    ImageView bbsItemLlThreeImg2;
    @Bind(R.id.bbs_item_ll_three_img_3)
    ImageView bbsItemLlThreeImg3;
    @Bind(R.id.bbs_item_ll_three_img)
    LinearLayout bbsItemLlThreeImg;
    @Bind(R.id.rv_data)
    RecyclerView rvData;
    
    private RepairsDetailsAdapter repairsDetailsAdapter;

    private OrderDetailShouliApdater orderDetailShouliApdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_details);
        ButterKnife.bind(this);
        setTitles();
        steAdapter();
    }

    private void steAdapter() {

        List<CommentModel> mData=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
//            CommentModel c=new CommentModel();
            CommentModel commentModel =new CommentModel();
            if (i==6){
                commentModel.id="1";
            }else {
                commentModel.id="0";

            }
            mData.add(commentModel);
        }


        orderDetailShouliApdater = new OrderDetailShouliApdater(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(orderDetailShouliApdater);

    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "报修详情", R.mipmap.iv_back);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d70_dianhua1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                Intent intent = new Intent();
                intent.putExtra("type", "2");
                intent.setClass(this, PhoneDialogActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
