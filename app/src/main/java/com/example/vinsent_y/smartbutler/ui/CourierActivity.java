package com.example.vinsent_y.smartbutler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.adapter.CourierAdapter;
import com.example.vinsent_y.smartbutler.entity.CourierData;
import com.example.vinsent_y.smartbutler.util.HttpUtil;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.StaticClass;
import com.example.vinsent_y.smartbutler.util.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private Spinner et_company;
    private TextView et_id;
    private Button btn_search;

    private ListView lv_time_line;

    //数据
    private List<CourierData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_company = findViewById(R.id.et_company);
        et_id = findViewById(R.id.et_id);
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        lv_time_line = findViewById(R.id.lv_time_line);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                L.i((String) et_company.getSelectedItem());
                String company = getCompany((String) et_company.getSelectedItem());
                String id = et_id.getText().toString().trim();

                if (!TextUtil.isEmpty(company) && !TextUtil.isEmpty(id)) {
                    String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.COURIER_KEY +
                            "&com=" + company + "&no=" + id;
                    HttpUtil.get(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String t = response.body().string();
                            L.i(t);
                            parsingJson(t);
                        }
                    });


                    parsingJson(StaticClass.TEST_JSON_DATA);
                    CourierAdapter adapter = new CourierAdapter(this, mList);
                    lv_time_line.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "输入内容不能为空!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private String getCompany(String selectedItem) {
        String company = null;
        switch (selectedItem) {
            case "韵达":
                company = "yd";
                break;
            case "顺丰":
                company = "sf";
                break;
            case "中通":
                company = "zt";
                break;
            case "圆通":
                company = "yt";
                break;
        }
        return company;
    }

    //将Json数据解析后放入List中
    private void parsingJson(String t) {
        try {
            JSONArray jsonArray = new JSONObject(t).getJSONObject("result").getJSONArray("list");
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CourierData data = new CourierData();
                data.setDateTime(jsonObject.getString("datetime"));
                data.setRemark(jsonObject.getString("remark"));
                data.setZone(jsonObject.getString("zone"));
                mList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
