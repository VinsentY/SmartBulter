package com.example.vinsent_y.smartbutler.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.adapter.ChatListAdapter;
import com.example.vinsent_y.smartbutler.entity.ChatListData;
import com.example.vinsent_y.smartbutler.util.HttpUtil;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.ShareUtils;
import com.example.vinsent_y.smartbutler.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.vinsent_y.smartbutler.util.StaticClass.SMART_BUTLER_KEY;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.fragement
 * 文件名： ButlerFragment
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/27 21:28
 * 描述：    TODO
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private EditText et_input;
    private Button btn_send;
    private ImageView iv_user;

    private List<ChatListData> mList = new ArrayList<>();
    ChatListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_butler, null);
        findView(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //通知adapter刷新
        adapter.notifyDataSetChanged();
    }

    private void findView(View view) {
        mListView = view.findViewById(R.id.chat_list_view);
        et_input = view.findViewById(R.id.et_input);
        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        mList.add(new ChatListData(ChatListData.TYPE_LEFT_TEXT, "你好，我是智能管家！"));
        adapter = new ChatListAdapter(getActivity(), mList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String text = et_input.getText().toString();
                sendMessage(text);
                replyMessage(text);
                break;
        }
    }

    private void addLeftItem(String content) {
        HttpUtil.get(SMART_BUTLER_KEY + content, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("调用智能管家接口失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = parsingJson(response.body().string());

                mList.add(new ChatListData(ChatListData.TYPE_LEFT_TEXT, str));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //通知adapter刷新
                        adapter.notifyDataSetChanged();
                        //滚动到底部
                        mListView.setSelection(mListView.getBottom());
                    }
                });
            }
        });
    }

    private String parsingJson(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            return jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addRightItem(String content) {
        mList.add(new ChatListData(ChatListData.TYPE_RIGHT_TEXT, content));
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mListView.setSelection(mListView.getBottom());
    }

    private void sendMessage(String s) {
        if (!TextUtil.isEmpty(s)) {
            addRightItem(s);
            et_input.setText("");
        }
    }

    private void replyMessage(String s) {
        if (!TextUtil.isEmpty(s)) {
            addLeftItem(s);
        }
    }
}

