package com.project.community.ui.life.minsheng;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationAppActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_hospital_arrow)
    TextView tvHospitalArrow;
    @Bind(R.id.tv_hospital_administrative)
    TextView tvHospitalAdministrative;
    @Bind(R.id.tv_hospital_doctor)
    TextView tvHospitalDoctor;
    @Bind(R.id.tv_appointment_time)
    TextView tvAppointmentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_app);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.registration_apply), R.mipmap.iv_back);
    }

    @OnClick({R.id.ll_registration_hospital, R.id.ll_registration_administrative, R.id.ll_registration_doctor, R.id.ll_registration_appointment_time, R.id.btn_appointment_summit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_registration_hospital:
                break;
            case R.id.ll_registration_administrative:
                break;
            case R.id.ll_registration_doctor:
                break;
            case R.id.ll_registration_appointment_time:
                break;
            case R.id.btn_appointment_summit:
                break;
        }
    }
}
