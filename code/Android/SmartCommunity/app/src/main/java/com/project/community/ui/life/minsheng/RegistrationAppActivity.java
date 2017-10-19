package com.project.community.ui.life.minsheng;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.library.okgo.view.loopview.LoopView;
import com.library.okgo.view.loopview.OnItemSelectedListener;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.DictionaryModel;
import com.project.community.util.DataUtilsa;
import com.project.community.util.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationAppActivity extends BaseActivity {

    @Bind(R.id.layout_root)
    LinearLayout mLayoutRoot;
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

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_app);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.registration_apply), R.mipmap.iv_back);
        initTimePicker(tvAppointmentTime);
    }
    List<String> list = new ArrayList<>();

    @OnClick({R.id.ll_registration_hospital, R.id.ll_registration_administrative, R.id.ll_registration_doctor, R.id.ll_registration_appointment_time, R.id.btn_appointment_summit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_registration_hospital:

                list.clear();
                list.add("第一人民医院");
                list.add("第二人民医院");
                list.add("第三人民医院");
                list.add("第四人民医院");
                showDialog("选择医院", tvHospitalArrow, list);
                break;
            case R.id.ll_registration_administrative:
                list.clear();
                list.add("一科室");
                list.add("二科室");
                list.add("三科室");
                list.add("四科室");
                showDialog("选择科室", tvHospitalAdministrative, list);
                break;
            case R.id.ll_registration_doctor:
                list.clear();
                list.add("何医生");
                list.add("小何医生");
                list.add("老何医生");
                list.add("何神医");
                showDialog("选择医生", tvHospitalDoctor, list);
                break;
            case R.id.ll_registration_appointment_time:
                pvTime.show();
                break;
            case R.id.btn_appointment_summit:
                finish();
                break;
        }
    }

    private PopupWindow mPopupWindow;
    private LoopView mLoopView;
    private int mCurrtindex=0;
    private void showDialog(String title, final TextView mTvTitle, final List<String> list) {
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_loopview, null);
        if (!TextUtils.isEmpty(mTvTitle.getText().toString())){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(mTvTitle.getText().toString())){
                    mCurrtindex=i;
                }
            }
        }


        //初始化控件
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTitle.setText(list.get(mCurrtindex));
                mPopupWindow.dismiss();
            }
        });

        mLoopView = (LoopView) inflate.findViewById(R.id.loopView);
        mLoopView.setItems(list);
        mLoopView.setInitPosition(mCurrtindex);
        mLoopView.setNotLoop();
        mLoopView.setCenterTextColor(getResources().getColor(R.color.txt_color));
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurrtindex=index;

            }
        });
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = new PopupWindow(this);
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
        mPopupWindow.showAtLocation(mLayoutRoot, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return false;
            }
        });
    }


    private void initTimePicker(final TextView tv) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(DataUtilsa.getYear(), DataUtilsa.getMonth(), DataUtilsa.getCurrentDayOfMonth());
//        startDate.set(1900, 12, 28);
        Calendar endDate = Calendar.getInstance();
        endDate.set(3000, 12, 31);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                /*btn_Time.setText(getTime(date));*/
                tv.setText(getTime(date));

            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "点", "分", "")
                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
                .setDividerColor(getResources().getColor(R.color.gray_line))
                .setCancelColor(getResources().getColor(R.color.gray_line))
                .setSubmitColor(getResources().getColor(R.color.colorPrimary))
                .setContentSize(21)
                .setSubCalSize(16)
                .setTitleSize(20)
                .setTitleText("选择预约时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
