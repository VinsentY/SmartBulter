package com.example.vinsent_y.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.adapter.WeChatAdapter;
import com.example.vinsent_y.smartbutler.entity.WeChatData;
import com.example.vinsent_y.smartbutler.util.HttpUtil;
import com.example.vinsent_y.smartbutler.ui.WebViewActivity;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.fragement
 * 文件名： WeChatFragment
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/27 21:29
 * 描述：    TODO
 */
public class WeChatFragment extends Fragment {

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    private WeChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat,null);

        setData();
        findVIew(view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position" + position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("wechat_data",mList.get(position));
                startActivity(intent);
            }
        });

        return view;
    }



    private void setData() {
        adapter = new WeChatAdapter(getActivity(),mList);
        initList();
    }

    private void initList() {
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY;
        HttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parsingJson(response.body().string());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONArray jsonArray = new JSONObject(t).getJSONObject("result").getJSONArray("list");
            for(int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                WeChatData data = new WeChatData();
                data.setTitle(jsonObject.getString("title"));
                data.setSource(jsonObject.getString("source"));
                data.setUrl(jsonObject.getString("url"));
                mList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findVIew(View view) {
        mListView = view.findViewById(R.id.wechat_list_view);

        mListView.setAdapter(adapter);
    }
}
