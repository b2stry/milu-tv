package com.shallowan.milutv.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdsmdg.tastytoast.TastyToast;
import com.shallowan.milutv.MiluApplication;
import com.shallowan.milutv.R;
import com.shallowan.milutv.main.MainActivity;
import com.shallowan.milutv.register.RegisterActivity;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import net.lemonsoft.lemonbubble.LemonBubble;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by ShallowAn.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findAllViews();
        setListeners();

        StatusBarCompat.translucentStatusBar(this);
    }


    private void findAllViews() {
        mAccountEdt = (EditText) findViewById(R.id.account);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login);
        mRegisterBtn = (Button) findViewById(R.id.register);

    }

    private void setListeners() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "登录");
                //登录操作
                login();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册的操作
                register();
            }
        });

//        mForgetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                forget();
//            }
//        });
    }

//    private void forget() {
//        Intent intent = new Intent();
//        intent.setClass(this, ForgetActivity.class);
//        startActivity(intent);
//    }

    private void register() {
        //跳转到注册页面
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }


    private void login() {

        LemonBubble.getRoundProgressBubbleInfo()
                .setTitle("登录中...")
                .show(LoginActivity.this);

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
                Log.i("TAG", "登录失败");
                //ld.close();
                //登录失败
                LemonBubble.hide();
                TastyToast.makeText(getApplicationContext(), "登录失败：" + errMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }
        });

    }

    private void loginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {

            @Override
            public void onSuccess(Object data) {
                //最终登录成功
                LemonBubble.hide();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                getSelfInfo();

                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.i("TAG", "登录失败");
                //登录失败
                LemonBubble.hide();
                TastyToast.makeText(getApplicationContext(), "登录失败：" + errMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                LemonBubble.hide();
                TastyToast.makeText(getApplicationContext(), "获取信息失败：" + s, TastyToast.LENGTH_LONG, TastyToast.ERROR);

            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                MiluApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            TastyToast.makeText(getApplicationContext(), "再按一次退出", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
