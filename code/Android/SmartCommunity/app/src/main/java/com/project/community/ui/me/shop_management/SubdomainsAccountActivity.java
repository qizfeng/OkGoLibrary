package com.project.community.ui.me.shop_management;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.ServiesComApdater;
import com.project.community.ui.adapter.SubdomainsAccountApdater;
import com.project.community.ui.me.OrderDetailActivity;
import com.project.community.view.MyButton;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 17/10/24.
 * 子账号
 */

public class SubdomainsAccountActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.add)
    MyButton add;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    List<CommentModel> list =new ArrayList<>();
    SubdomainsAccountApdater mAdapter;
    private Dialog mDialog;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,SubdomainsAccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subdomains_account);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.subdomains_account_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        mRecyclerView.addItemDecoration(decoration);
        for (int i = 0; i < 2; i++) {
            CommentModel commentModel =new CommentModel();
            commentModel.content="贝贝";
            list.add(commentModel);
        }
        mAdapter = new SubdomainsAccountApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.showLongToast(this,position);
              
            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()){
                    case R.id.item_edit:
                        break;     
                    case R.id.item_del:
                        showAlertDialog(position);
                        break;
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        AddSubdomainsAccountActivity.startActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && requestCode==100 && resultCode==100){
            CommentModel commentModel = (CommentModel) data.getSerializableExtra("save");
            list.add(commentModel);
            mAdapter.notifyItemInserted(list.size()-1);
        }
    }

    /**
     * 删除成员对话框
     *
     * @param position
     */
    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }
}
