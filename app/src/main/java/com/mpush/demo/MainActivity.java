package com.mpush.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.letv.loginsdk.LoginSdk;
import com.letv.loginsdk.LoginSdkLogout;
import com.letv.loginsdk.bean.LetvBaseBean;
import com.letv.loginsdk.bean.UserBean;
import com.letv.loginsdk.callback.LoginSuccessCallBack;
import com.mpush.android.MPush;
import com.mpush.android.Notifications;
import com.mpush.android.R;
import com.mpush.java.BuildConfig;
import com.mpush.java.api.Constants;
import com.mpush.java.api.http.HttpCallback;
import com.mpush.java.api.http.HttpMethod;
import com.mpush.java.api.http.HttpRequest;
import com.mpush.java.api.http.HttpResponse;
import com.mpush.java.client.ClientConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private TextView mTvContent;

    private Button mBtnLogin;

    private UserBean mUserBean;// 登录返回的bean类
    private final int LOGINSUCCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = getString("mpush.cfg", "login");

        mTvContent = (TextView) findViewById(R.id.value);
        mBtnLogin = (Button) findViewById(R.id.login);
        if (!TextUtils.isEmpty(user)) {
            mUserBean = new Gson().fromJson(user, new TypeToken<UserBean>(){}.getType());
            mBtnLogin.setText("退出登录");
        } else {
            mBtnLogin.setText("请登录");
        }

        initSetting();
    }

    /**
     * 通知初始化设置
     */
    private void initSetting() {
        Notifications.I.init(this.getApplicationContext());
        Notifications.I.setSmallIcon(R.mipmap.ic_notification);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }

    /**
     * 推送初始化设置
     *
     * @param allocServer
     */
    private void initPush(String allocServer) {
        //公钥有服务端提供和私钥对应
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";

        ClientConfig cc = ClientConfig.build()
                .setPublicKey(publicKey)
                .setAllotServer(allocServer)
                .setDeviceId(getDeviceId())
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(new MyLog(this, (EditText) findViewById(R.id.log)))
                .setLogEnabled(BuildConfig.DEBUG)
                .setEnableHttpProxy(true)
                .setUserId("3123456");
        MPush.I.checkInit(getApplicationContext()).setClientConfig(cc);
    }

    /**
     * 通过长连接发送请求
     *
     * @param btn
     * @throws Exception
     */
    public void sendPush(View btn) throws Exception {
        JSONObject params = new JSONObject();
        params.put("userId", "123456789");
        params.put("hello", "987654321" + " say:hello");

        HttpRequest request = new HttpRequest(HttpMethod.GET, "https://pre-jr1.le.com/api/pay/bankCardLimits/v3/appkey");
        byte[] body = params.toString().getBytes(Constants.UTF_8);
        request.setBody(body);
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(new HttpCallback() {
            @Override
            public void onResponse(final HttpResponse httpResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (httpResponse.statusCode == 200) {
                            showToast(new String(httpResponse.body, Constants.UTF_8));
                            Log.e("vivi", new String(httpResponse.body, Constants.UTF_8));
                        } else {
                            showToast(httpResponse.reasonPhrase);
                            Log.e("vivi", httpResponse.reasonPhrase);
                        }
                    }
                });
            }

            @Override
            public void onCancelled() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("http请求取消");
                        Log.e("vivi", "http请求取消");
                    }
                });
            }
        });
        MPush.I.sendHttpProxy(request);
    }

    public void sendPost(View btn) throws Exception {
        String token = mUserBean== null ? "158bc03d12jm34lrm1tAxphEfzG78rIrGXzaLyANpkyDxwGzAG0xFvJyqwXPgIJGZvrWxm1ptHw" : mUserBean.getSsoTK();
//        JSONObject params1 = new JSONObject();
//        params1.put("version", "v1");
//        params1.put("appKey", "1");
//        params1.put("token", token);
//        params1.put("content", "这里是长连接的测试，请不要拦截接口，谢谢");

//        String params1 = "version=1&appkey=1&token=" + token + "&content=这里是长连接的测试，请不要拦截接口，谢谢";
        Map<String, String> params1 = new HashMap<>();
        params1.put("version", "v1");
        params1.put("appKey", "1");
        params1.put("token", token);
        params1.put("content", "这里是长连接的测试，请不要拦截接口，谢谢");

        Map<String, String> header = new HashMap<String, String>();
        header.put("User-agent", "LeFinanceTrade/" + "1.5.1");

        StringBuilder builder = new StringBuilder();
        builder.append("token=").append(token);
        builder.append("&uid=").append(mUserBean == null ? "176943483" : mUserBean.getUid());

        header.put("Extensions", builder.toString());

        HttpRequest request = new HttpRequest(HttpMethod.POST, "https://pre-jr1.le.com/api/content/feedback");
//        byte[] body = params1.toString().getBytes(Constants.UTF_8);
        request.setPostParam(params1);
        request.setHeaders(header);
        request.setTimeout((int) TimeUnit.SECONDS.toMillis(10));
        request.setCallback(new HttpCallback() {
            @Override
            public void onResponse(final HttpResponse httpResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (httpResponse.statusCode == 200) {
                            showToast(new String(httpResponse.body, Constants.UTF_8));
                            Log.e("vivi", new String(httpResponse.body, Constants.UTF_8));
                        } else {
                            showToast(httpResponse.reasonPhrase);
                            Log.e("vivi", httpResponse.reasonPhrase);
                        }
                    }
                });
            }

            @Override
            public void onCancelled() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("http请求取消");
                        Log.e("vivi", "http请求取消");
                    }
                });
            }
        });
        MPush.I.sendHttpProxy(request);
    }

    public void login(View login) {
        if ("退出登录".equals(mBtnLogin.getText().toString())) {
            mBtnLogin.setText("请登录");
            new LoginSdkLogout().logout(this);
            mUserBean = null;
            setString("mpush.cfg", "login", "");
        } else {
            new LoginSdk().login(MainActivity.this,new LoginSuccessCallBack(){
                @Override
                public void loginSuccessCallBack(
                        LoginSuccessState loginSuccessState,
                        LetvBaseBean bean) {

                    if (loginSuccessState == LoginSuccessState.LOGINSUCCESS) {
                        //登录成功后接收的值
                        mUserBean = (UserBean)bean;
                        mHandler.sendEmptyMessageDelayed(LOGINSUCCESS, 0);
                    }
                }
            });
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGINSUCCESS:
                    //登录成功后
                    mBtnLogin.setText("退出登录");
                    setString("mpush.cfg", "login", new Gson().toJson(mUserBean));
                    break;

                default:
                    break;
            }

        }

    };
    private final static String DEFAULT_STRING = "";
    private final static int MODE = Context.MODE_PRIVATE;

    public String getString(String name, String key) {
        SharedPreferences mSharedPreferences = getApplication().getSharedPreferences(name, MODE);
        return mSharedPreferences.getString(key, DEFAULT_STRING);
    }

    public boolean setString(String name, String key, String value) {
        SharedPreferences mSharedPreferences = getApplication().getSharedPreferences(name, MODE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    /**
     * 获取设备id
     *
     * @return
     */
    private String getDeviceId() {
        SharedPreferences sp = this.getSharedPreferences("mpush.cfg", Context.MODE_PRIVATE);
        String deviceId = sp.getString("deviceId", null);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("deviceId", deviceId);
            editor.apply();
        }
        return deviceId;
    }

    public void bindUser(View btn) {
        EditText et = (EditText) findViewById(R.id.from);
        String userId = et.getText().toString().trim();
        if (!TextUtils.isEmpty(userId)) {
            MPush.I.bindAccount(userId);
        }
    }

    public void startPush(View btn) {
        initPush("http://10.112.88.102:8662/foundation/message/alloc/getService");
        MPush.I.checkInit(this.getApplication()).startPush();
        showToast("start push");
    }

    public void stopPush(View btn) {
        MPush.I.stopPush();
        showToast("stop push");
    }

    public void pausePush(View btn) {
        MPush.I.pausePush();
        showToast("pause push");
    }

    public void resumePush(View btn) {
        MPush.I.resumePush();
        showToast("resume push");
    }

    public void unbindUser(View btn) {
        MPush.I.unbindAccount();
        showToast("unbind user");
    }

    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
