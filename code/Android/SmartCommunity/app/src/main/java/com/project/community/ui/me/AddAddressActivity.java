package com.project.community.ui.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.Event.AddAddressEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.bean.AddressListBean;
import com.project.community.util.FormValidationUtil;
import com.project.community.util.KeyBoardUtil;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.pickerView.GetJsonDataUtil;
import com.project.community.view.pickerView.JsonBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：fangkai on 2017/10/23 16:02
 * em：617716355@qq.com
 */
public class AddAddressActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.tv_address)
    TextView tvAddress;


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    private AddressListBean addressListBean;


    public static void startActivity(Context context, AddressListBean addressListBean) {

        Intent intent = new Intent(context, AddAddressActivity.class);

        intent.putExtra("addressListBean", addressListBean);

        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        setTitles();
    }

    private void setTitles() {
        addressListBean = (AddressListBean) getIntent().getSerializableExtra("addressListBean");

//        AddHouseEvent addhouseEvent1 = getIntent().getParcelableExtra("s");

        initToolBar(toolbar, tvTitle, true, getString(R.string.add_address_activity), R.mipmap.iv_back);


        if (addressListBean != null) {
            etName.setText(addressListBean.getConsignee());
            etPhone.setText(addressListBean.getContactPhone());
            tvAddress.setText(addressListBean.getUserArea());
            etAddress.setText(addressListBean.getAddress());
        }
    }

    @OnClick({R.id.ll_address, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                KeyBoardUtil.closeKeybord(this);

                Single.create(new Single.OnSubscribe<String>() {
                    @Override
                    public void call(SingleSubscriber<? super String> singleSubscriber) {
                        initJsonData();
                        singleSubscriber.onSuccess("");
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<String>() {
                            @Override
                            public void onSuccess(String value) {
                                setPickerCityData(options1Items, options2Items, options3Items);
                            }

                            @Override
                            public void onError(Throwable error) {

                            }
                        });
                break;
            case R.id.btn_register:

                register();

                break;
        }
    }


    private void register() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            showToast(getString(R.string.address_no_name));
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            showToast(getString(R.string.address_no_phone));
            return;
        }
        if (!FormValidationUtil.isMobile(etPhone.getText().toString().trim())) {

            showToast(getString(R.string.address_is_mobile));
            return;
        }


        if (TextUtils.isEmpty(tvAddress.getText().toString().trim())) {
            showToast(getString(R.string.address_change_address));
            return;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            showToast(getString(R.string.address_import_address));
            return;
        }

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_error));
            return;
        }
//        JsonCallback


        progressDialog.show();
        if (addressListBean == null) {

            serverDao.addAddress(getUser(this).id, etName.getText().toString().trim(),
                    etPhone.getText().toString().trim(), tvAddress.getText().toString().trim(),
                    "街道", etAddress.getText().toString().trim(), "", new JsonCallback<BaseResponse<List>>() {
                        @Override
                        public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {

                            progressDialog.dismiss();
                            ToastUtil.showToast(AddAddressActivity.this, baseResponse.message + "");
                            if (baseResponse.errNum.equals("0")) {
                                finish();
                                EventBus.getDefault().post(new AddAddressEvent(true));
                            } else {

                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            progressDialog.dismiss();
                            ToastUtil.showToast(AddAddressActivity.this, e.getMessage() + "");
                        }
                    });

        } else {

            serverDao.addAddress(getUser(this).id, etName.getText().toString().trim(),
                    etPhone.getText().toString().trim(), tvAddress.getText().toString().trim(),
                    "街道", etAddress.getText().toString().trim(), addressListBean.getId(), new JsonCallback<BaseResponse<List>>() {
                        @Override
                        public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {

                            progressDialog.dismiss();
                            ToastUtil.showToast(AddAddressActivity.this, baseResponse.message + "");
                            if (baseResponse.errNum.equals("0")) {
                                finish();
                                EventBus.getDefault().post(new AddAddressEvent(true));
                            } else {

                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            progressDialog.dismiss();
                            ToastUtil.showToast(AddAddressActivity.this, e.getMessage() + "");
                        }
                    });


        }


//        finish();


    }


    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /**
     * 设置城市
     *
     * @param options1Items
     * @param options2Items
     * @param options3Items
     */

    public void setPickerCityData(final ArrayList<JsonBean> options1Items,
                                  final ArrayList<ArrayList<String>> options2Items,
                                  final ArrayList<ArrayList<ArrayList<String>>> options3Items) {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvAddress.setTextColor(Color.parseColor("#333333"));
                tvAddress.setText(options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2)
                        + options3Items.get(options1).get(options2).get(options3));
//                province = options1Items.get(options1).getPickerViewText();
//                city = options2Items.get(options1).get(options2);
//                area = options3Items.get(options1).get(options2).get(options3);

            }
        })
                .setTitleText(getString(R.string.city))
                .setTitleColor(Color.parseColor("#19a595"))
                .setSubmitText(getString(R.string.confirm))
                .setSubmitColor(Color.parseColor("#19a595"))
                .setCancelColor(Color.parseColor("#19a595"))
                .setCancelText(getString(R.string.cancel))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//二级选择器
        pvOptions.show();
    }

}
