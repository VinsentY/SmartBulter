package com.example.vinsent_y.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.adapter.GirlAdapter;
import com.example.vinsent_y.smartbutler.util.HttpUtil;
import com.example.vinsent_y.smartbutler.util.PicassoUtil;
import com.example.vinsent_y.smartbutler.util.StaticClass;
import com.example.vinsent_y.smartbutler.view.CustomDialog;
import com.github.chrisbanes.photoview.PhotoView;

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
 * 文件名： GirlFragment
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/27 21:27
 * 描述：    TODO
 */

public class GirlFragment extends Fragment {

    private GridView mGridView;
    private List<String> mList = new ArrayList<>();
    private GirlAdapter adapter;
    private CustomDialog dialog;
    private PhotoView photoView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        setData();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PicassoUtil.loadImageView(mList.get(i),photoView);
                dialog.show();
            }
        });

        return view;
    }

    private void setData() {
        HttpUtil.get(StaticClass.GIRL_URL, new Callback() {
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
            JSONArray array = new JSONObject(t).getJSONArray("results");
            for(int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                mList.add(jsonObject.getString("url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findView(View view) {
        mGridView = view.findViewById(R.id.mGridView);
        adapter = new GirlAdapter(getActivity(),mList);
        mGridView.setAdapter(adapter);
        dialog = new CustomDialog(getActivity(),
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.dialog_girl,R.style.Theme_Dialog,Gravity.CENTER,R.style.pop_anim_style);
        photoView = dialog.findViewById(R.id.pv_image);
    }
}
