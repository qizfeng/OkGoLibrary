package com.project.community.ui.life.minsheng;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.ApplyStoryPicAdapter;
import com.project.community.ui.adapter.listener.SendMessageAdapter;
import com.project.community.view.MyButton;
import com.project.community.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMessageActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.send_message_tv_type)
    TextView sendMessageTvType;
    @Bind(R.id.send_message_ll_type)
    RelativeLayout sendMessageLlType;
    @Bind(R.id.send_message_et_title)
    EditText sendMessageEtTitle;
    @Bind(R.id.send_message_tv_title_num)
    TextView sendMessageTvTitleNum;
    @Bind(R.id.send_message_et_content)
    EditText sendMessageEtContent;
    @Bind(R.id.send_message_tv_content_num)
    TextView sendMessageTvContentNum;
    @Bind(R.id.send_message_gv)
    MyGridView sendMessageGv;
    @Bind(R.id.send_message_summit)
    MyButton sendMessageSummit;

    private SendMessageAdapter mApplyStoryPicAdapter;
    private List<String> mImags = new ArrayList<>(); //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        ininData();
    }

    private void ininData() {

        initToolBar(mToolBar, mTvTitle, true, getString(R.string.send_message), R.mipmap.iv_back);


        mApplyStoryPicAdapter = new SendMessageAdapter(mImags, this);
        sendMessageGv.setAdapter(mApplyStoryPicAdapter);
        sendMessageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImags.size() >= 9) {
                    return;
                }
                if (i == mApplyStoryPicAdapter.getCount() - 1) {
                    mImags.add("");
                    if (mImags.size() >= 9) {
                        mApplyStoryPicAdapter.setGoneAdd(0);
                    } else {
                        mApplyStoryPicAdapter.setGoneAdd(1);
                    }
                    mApplyStoryPicAdapter.notifyDataSetChanged();
                }

            }
        });

        sendMessageEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendMessageTvTitleNum.setText(editable.length()+"/35");
            }
        });
        sendMessageEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendMessageTvContentNum.setText(editable.length()+"/350");
            }
        });
    }

    @OnClick(R.id.send_message_ll_type)
    public void onViewClicked() {
    }
}
