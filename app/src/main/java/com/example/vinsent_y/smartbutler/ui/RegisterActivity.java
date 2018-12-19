package com.example.vinsent_y.smartbutler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.MyUser;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.TextUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_name;
    private EditText ed_password;
    private EditText repeat_password;
    private RadioGroup mRadioGroup;
    private EditText ed_mail;
    private Button register;

    private Boolean isGender = true;
    private String name;
    private String password;
    private String repeat_pass;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        intView();
    }

    private void intView() {
        user_name = findViewById(R.id.user_name);
        ed_password = findViewById(R.id.ed_password);
        repeat_password = findViewById(R.id.repeat_password);
        mRadioGroup = findViewById(R.id.mRadioGroup);
        ed_mail = findViewById(R.id.ed_email);
        register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                name = user_name.getText().toString().trim();
                password = ed_password.getText().toString().trim();
                repeat_pass = repeat_password.getText().toString().trim();
                email = ed_mail.getText().toString().trim();
                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rbt_boy) {
                            isGender = true;
                        } else {
                            isGender = false;
                        }
                    }
                });
                //检测输入是否正常
                if (checkForm()) {
                    //开始注册
                    MyUser user = new MyUser();
                    user.setSex(isGender);
                    user.setEmail(email);
                    user.setUsername(name);
                    user.setPassword(password);
                    user.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                L.i(Integer.toString(e.getErrorCode()));
                            }
                        }
                    });
                }


                break;
        }
    }

    private boolean checkForm() {
        if (TextUtil.isEmpty(name)
                || TextUtil.isEmpty(password)
                || TextUtil.isEmpty(repeat_pass)
                || TextUtil.isEmpty(email)) {
            Toast.makeText(this, "所填内容不能为空", Toast.LENGTH_SHORT).show();
        } else if (password.length() <= 6) {
            Toast.makeText(this, "密码长度过短", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repeat_pass)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
}
