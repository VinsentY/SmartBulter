package com.example.vinsent_y.smartbutler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.util.HttpUtil;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.StaticClass;
import com.example.vinsent_y.smartbutler.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PhoneActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone_number;
    private ImageView iv_company;
    private TextView tv_company;
    private TextView tv_city;
    private TextView tv_province;
    private TextView tv_areacode;
    private TextView tv_zip;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btn_del;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_phone_number.setText("");
                return true;
            }
        });
    }

    private void initView() {
        et_phone_number = findViewById(R.id.et_phone_number);
        iv_company = findViewById(R.id.iv_company);
        tv_province = findViewById(R.id.tv_province);
        tv_city = findViewById(R.id.tv_city);
        tv_areacode = findViewById(R.id.tv_areaCode);
        tv_company = findViewById(R.id.tv_company);
        tv_zip = findViewById(R.id.tv_zip);

        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_del = findViewById(R.id.btn_del);
        btn_submit = findViewById(R.id.btn_submit);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String  cache = et_phone_number.getText().toString();
        switch (v.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                et_phone_number.append(((Button) v).getText());
                et_phone_number.setSelection(cache.length() + 1);
                break;
            case R.id.btn_del:
                if (!TextUtil.isEmpty(cache)) {
                    et_phone_number.setText(cache.substring(0, cache.length() - 1));
                    //移动光标
                    et_phone_number.setSelection(cache.length() - 1);
                }
                break;
            case R.id.btn_submit:
                phoneSearch(et_phone_number.getText().toString().trim());
                break;
        }
    }

    class PhoneData {
        private String province;
        private String city;
        private String areaCode;
        private String zip;
        private String company;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }


    private void phoneSearch(String phoneNumber) {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + phoneNumber + "&key=" + StaticClass.PHONE_KEY;

        HttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String t = response.body().string();
                L.i(t);
                setText(parsingJson(t));
            }
        });

    }

    private void setText(final PhoneData phoneData) {
        if(phoneData == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_province.setText("省会: " + phoneData.getProvince());
                tv_city.setText("城市: " + phoneData.getCity());
                tv_areacode.setText("区号: " + phoneData.getAreaCode());
                tv_zip.setText("邮编: " + phoneData.getZip());
                tv_company.setText("公司: " + phoneData.getCompany());
                switch (phoneData.getCompany()) {
                    case "联通":
                        iv_company.setImageResource(R.drawable.china_unicom);
                        break;
                    case "移动":
                        iv_company.setImageResource(R.drawable.china_mobile);
                        break;
                    case "电信":
                        iv_company.setImageResource(R.drawable.china_telecom);
                        break;
                }
            }
        });
    }

    private PhoneData parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t).getJSONObject("result");

            PhoneData phoneData = new PhoneData();
            phoneData.setAreaCode(jsonObject.getString("areacode"));
            phoneData.setCity(jsonObject.getString("city"));
            phoneData.setCompany(jsonObject.getString("company"));
            phoneData.setZip(jsonObject.getString("zip"));
            phoneData.setProvince(jsonObject.getString("province"));
            return phoneData;
        } catch (JSONException e) {
            Toast.makeText(this,"错误的电话号码!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
}
