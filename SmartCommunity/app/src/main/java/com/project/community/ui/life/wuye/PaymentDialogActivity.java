package com.project.community.ui.life.wuye;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.aipay.OrderInfoUtil2_0;
import com.project.community.aipay.PayResult;
import com.project.community.base.BaseActivity;
import com.project.community.util.ScreenUtils;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/23.
 * 支付
 */

public class PaymentDialogActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.layout_alipay)
    RelativeLayout mLayoutAlipay;
    @Bind(R.id.layout_weipay)
    RelativeLayout mLayoutWeipay;
    @Bind(R.id.btn_alipay)
    RadioButton mBtnAlipay;
    @Bind(R.id.btn_weipay)
    RadioButton mBtnWeipay;
    private String paymentId;
    private String orderStr;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        // showToast("支付直白");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        // 其他状态值则为授权失败
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
//
//                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PaymentDialogActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_payment);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            paymentId = bundle.getString("paymentId");
        }
        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        // p.height = (int) (ScreenUtils.getScreenHeight(this) * 0.4); // 高度设置为屏幕的0.4
        p.width = (int) (ScreenUtils.getScreenWidth(this) * 0.7); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWeipay.setOnClickListener(this);
        mBtnAlipay.setEnabled(false);
        mBtnAlipay.setClickable(false);
        mBtnWeipay.setEnabled(false);
        mBtnWeipay.setClickable(false);
        mBtnAlipay.setVisibility(View.VISIBLE);
        mBtnWeipay.setVisibility(View.GONE);
        loadOrderInfo();
    }

    /**
     * 加載訂單數據
     */
    private void loadOrderInfo() {
        serverDao.getPayOrderInfo(getUser(this).id, paymentId, new DialogCallback<BaseResponse<String>>(this) {
            @Override
            public void onSuccess(BaseResponse<String> baseResponse, Call call, Response response) {
                orderStr=baseResponse.retData;
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }



    @OnClick({R.id.btn_cancel, R.id.iv_close})
    public void onCancelClick(View v) {
        this.finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm(View v) {
        if (mBtnWeipay.isChecked()) {
            showToast(getString(R.string.txt_wei_pay));
        } else if (mBtnAlipay.isChecked()) {
            payV2(v);
        }
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_alipay:
                mBtnAlipay.setChecked(true);
                mBtnWeipay.setChecked(false);
                mBtnAlipay.setVisibility(View.VISIBLE);
                mBtnWeipay.setVisibility(View.GONE);
                break;
            case R.id.layout_weipay:
                mBtnAlipay.setChecked(false);
                mBtnWeipay.setChecked(true);
                mBtnAlipay.setVisibility(View.GONE);
                mBtnWeipay.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        /**
         * orderInfo的获取必须来自服务端；
         */
        if(TextUtils.isEmpty(orderStr)){
            showToast(getString(R.string.toast_error_pay));
            finish();
            return;
        }
        final String orderInfo =orderStr;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentDialogActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PaymentDialogActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


//    /**
//     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
//     *
//     * @param v
//     */
//    public void h5Pay(View v) {
//        Intent intent = new Intent(this, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
//         *
//         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
//         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
//         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
//         * 进行测试。
//         *
//         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
//         * 可以参考它实现自定义的 URL 拦截逻辑。
//         */
//        String url = "http://m.taobao.com";
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
//    }

}
