package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.ApplyStoryPicAdapter;
import com.project.community.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyStoreActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.apply_store_et_title)
    EditText applyStoreEtTitle;
    @Bind(R.id.apply_store_et_name)
    EditText applyStoreEtName;
    @Bind(R.id.apply_store_et_tel)
    EditText applyStoreEtTel;
    @Bind(R.id.apply_store_tv_address)
    TextView applyStoreTvAddress;
    @Bind(R.id.apply_store_tv_type)
    TextView applyStoreTvType;
    @Bind(R.id.apply_store_et_important)
    EditText applyStoreEtImportant;
    @Bind(R.id.apply_store_et_company_name)
    EditText applyStoreEtCompanyName;
    @Bind(R.id.apply_store_et_business_license)
    EditText applyStoreEtBusinessLicense;
    @Bind(R.id.apply_store_gv_business_license)
    MyGridView applyStoreGvBusinessLicense;
    @Bind(R.id.apply_store_et_legal_person)
    EditText applyStoreEtLegalPerson;
    @Bind(R.id.apply_store_gv_legal_person)
    MyGridView applyStoreGvLegalPerson;

    private ApplyStoryPicAdapter mApplyStoryPicAdapterBusinessLicense;
    private ApplyStoryPicAdapter mApplyStoryPicAdapterGvLegalPerson;
    private List<String> mImagsBusinessLicense = new ArrayList<>(); // 营业执照照片集合
    private List<String> mImagsGvLegalPersone = new ArrayList<>();//法人照片集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.store_Apply), R.mipmap.iv_back);

        /**
         * 营业执照照片
         */
        mApplyStoryPicAdapterBusinessLicense = new ApplyStoryPicAdapter(mImagsBusinessLicense,this);
        applyStoreGvBusinessLicense.setAdapter(mApplyStoryPicAdapterBusinessLicense);
        applyStoreGvBusinessLicense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImagsBusinessLicense.size()>=2){
                    return;
                }
                if (i == mApplyStoryPicAdapterBusinessLicense.getCount() - 1) {
                    mImagsBusinessLicense.add("");
                    if (mImagsBusinessLicense.size()>=2){
                        mApplyStoryPicAdapterBusinessLicense.setGoneAdd(0);
                    }else {
                        mApplyStoryPicAdapterBusinessLicense.setGoneAdd(1);
                    }
                    mApplyStoryPicAdapterBusinessLicense.notifyDataSetChanged();
                }

            }
        });

        /**
         * 法人身份照片
         */
        mApplyStoryPicAdapterGvLegalPerson = new ApplyStoryPicAdapter(mImagsGvLegalPersone,this);
        applyStoreGvLegalPerson.setAdapter(mApplyStoryPicAdapterGvLegalPerson);
        applyStoreGvLegalPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImagsGvLegalPersone.size()>=2){
                    return;
                }
                if (i == mApplyStoryPicAdapterGvLegalPerson.getCount() - 1) {
                    mImagsGvLegalPersone.add("");
                    if (mImagsGvLegalPersone.size()>=2){
                        mApplyStoryPicAdapterGvLegalPerson.setGoneAdd(0);
                    }else {
                        mApplyStoryPicAdapterGvLegalPerson.setGoneAdd(1);
                    }
                    mApplyStoryPicAdapterGvLegalPerson.notifyDataSetChanged();
                }

            }
        });

    }

    @OnClick({R.id.ll_apply_store_tv_address, R.id.ll_apply_store_tv_type, R.id.apply_store_btn_confire})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_apply_store_tv_address:
                Intent intent = new Intent(ApplyStoreActivity.this,AdrressActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.ll_apply_store_tv_type:
                break;
            case R.id.apply_store_btn_confire:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && requestCode == 100 && resultCode==100){
            applyStoreTvAddress.setText(data.getStringExtra("result"));
        }
    }
}
