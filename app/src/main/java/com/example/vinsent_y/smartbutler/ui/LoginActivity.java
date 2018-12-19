package com.example.vinsent_y.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.MainActivity;
import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.MyUser;
import com.example.vinsent_y.smartbutler.util.ShareUtils;
import com.example.vinsent_y.smartbutler.util.TextUtil;
import com.example.vinsent_y.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_user_name;
    private EditText ed_password;
    private Button btn_login;

    private TextView btn_register;

    private String name;
    private String password;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initView();
    }

    private void initData() {
        name = ShareUtils.getString(this,"name","");
        password = ShareUtils.getString(this,"password","");
    }

    private void initView() {
        ed_user_name = findViewById(R.id.user_name);
        ed_password = findViewById(R.id.password);
        ed_user_name.setText(name);
        ed_password.setText(password);
        dialog = new CustomDialog(this,100,100,R.layout.dialog_loading,R.style.Theme_Dialog,Gravity.CENTER,R.style.pop_anim_style);
        //设置触摸无法取消
        dialog.setCancelable(false);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                name = ed_user_name.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (!TextUtil.isEmpty(name) && !TextUtil.isEmpty(password)) {
                    dialog.show();
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {

                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                rememberPassword();
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                }



                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void rememberPassword() {
        ShareUtils.putString(this,"name",name);
        ShareUtils.putString(this,"password",password);
    }
}
