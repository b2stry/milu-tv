package com.shallowan.milutv.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shallowan.milutv.R;
import com.shallowan.milutv.webview.WebViewActivity;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * Created by ShallowAn.
 */

public class PhoneRegisterActivity extends AppCompatActivity {
    private Toolbar mTitlebar;
    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private EditText mConfirmPasswordEt;
    private Button mRegisterBtn;
    private Button mCheckcodeBtn;
    private TextView mMiLu;
    private TimeCount mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);

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
                Intent intent = new Intent(PhoneRegisterActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        mTime = new TimeCount(3000, 1000);

        mCheckcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTime.start();
            }
        });
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCheckcodeBtn.setBackgroundColor(Color.parseColor("#B6B6D8"));
            mCheckcodeBtn.setClickable(false);
            mCheckcodeBtn.setBackgroundResource(R.drawable.btn_bkg_gray_rect_round);
            mCheckcodeBtn.setTextColor(Color.GRAY);
            mCheckcodeBtn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            mCheckcodeBtn.setText("重新获取验证码");
            mCheckcodeBtn.setClickable(true);
            mCheckcodeBtn.setBackgroundResource(R.drawable.btn_bkg_green_rect_round);
            mCheckcodeBtn.setTextColor(Color.rgb(92, 137, 0));
        }
    }

    private void register() {
        String userAccountStr = mAccountEdt.getText().toString();
        String userPasswordStr = mPasswordEdt.getText().toString();
        String userConfirmPswStr = mConfirmPasswordEt.getText().toString();

        //检验
        if (TextUtils.isEmpty(userAccountStr) || TextUtils.isEmpty(userPasswordStr) || TextUtils.isEmpty(userConfirmPswStr)) {
            Toast.makeText(this, "手机号码或者密码没有填写！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userAccountStr.length() < 8 || userPasswordStr.length() < 8 || userConfirmPswStr.length() < 8) {
            Toast.makeText(this, "手机号码或密码长度不够！", Toast.LENGTH_SHORT).show();
            return;
        }

        registerActurally(userAccountStr, userPasswordStr);
    }

    private void registerActurally(String userAccountStr, String userPasswordStr) {
        ILiveLoginManager.getInstance().tlsRegister(userAccountStr, userPasswordStr, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Toast.makeText(PhoneRegisterActivity.this, "注册成功！请登录！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(PhoneRegisterActivity.this, "注册失败!" + errMsg, Toast.LENGTH_SHORT).show();
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
        mCheckcodeBtn = (Button) findViewById(R.id.checkcodebtn);
    }
}
