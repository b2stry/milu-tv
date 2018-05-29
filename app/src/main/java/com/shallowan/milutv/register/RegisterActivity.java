package com.shallowan.milutv.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.shallowan.milutv.MiluApplication;
import com.shallowan.milutv.R;
import com.shallowan.milutv.main.MainActivity;
import com.shallowan.milutv.webview.WebViewActivity;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * Created by ShallowAn.
 */

public class RegisterActivity extends AppCompatActivity {
    private Toolbar mTitlebar;
    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private EditText mConfirmPasswordEt;
    private Button mRegisterBtn;
    private TextView mMiLu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findAllViews();
        setListeners();
        setTitleBar();
    }


    private void setTitleBar() {
        mTitlebar.setTitle("注册");
        mTitlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mTitlebar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void findAllViews() {
        mTitlebar = (Toolbar) findViewById(R.id.titlebar);
        mAccountEdt = (EditText) findViewById(R.id.account);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mConfirmPasswordEt = (EditText) findViewById(R.id.confirm_password);
        mRegisterBtn = (Button) findViewById(R.id.register);
        mMiLu = (TextView) findViewById(R.id.milu);
    }

    private void setListeners() {
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册
                register();
            }
        });

        mMiLu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();
        String confirmPswStr = mConfirmPasswordEt.getText().toString();

        if (TextUtils.isEmpty(accountStr) ||
                TextUtils.isEmpty(passwordStr) ||
                TextUtils.isEmpty(confirmPswStr)) {
            TastyToast.makeText(getApplicationContext(), "账号或密码不能为空", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return;
        }

        if (accountStr.length() < 8 || passwordStr.length() < 8 || confirmPswStr.length() < 8) {
            TastyToast.makeText(getApplicationContext(), "用户名或密码长度不够", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return;
        }

        if (!passwordStr.equals(confirmPswStr)) {
            TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return;
        }

        ILiveLoginManager.getInstance().tlsRegister(accountStr, passwordStr, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //注册成功
                TastyToast.makeText(getApplicationContext(), "注册成功", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                //登录一下
                login();


            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //注册失败
                TastyToast.makeText(getApplicationContext(), "注册失败：" + errMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    private void login() {
        final String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();

        //调用腾讯IM登录
        ILiveLoginManager.getInstance().tlsLogin(accountStr, passwordStr, new ILiveCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //登陆成功。
                loginLive(accountStr, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                TastyToast.makeText(getApplicationContext(), "tls登录失败：" + errMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    private void loginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {

            @Override
            public void onSuccess(Object data) {
                //最终登录成功
                //跳转到修改用户信息界面。
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

                getSelfInfo();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                TastyToast.makeText(getApplicationContext(), "iLive登录失败：" + errMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                TastyToast.makeText(getApplicationContext(), "获取信息失败：" + s, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                MiluApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }
}
