package com.example.vinsent_y.smartbutler.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.ChatListData;
import com.example.vinsent_y.smartbutler.util.ShareUtils;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ChatListData> mList;
    private ChatListData data;

    public ChatListAdapter(Context mContext, List<ChatListData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case ChatListData.TYPE_LEFT_TEXT:
                    viewHolderLeft = new ViewHolderLeft();
                    convertView = inflater.inflate(R.layout.chat_left_item, null);
                    viewHolderLeft.iv_butler = convertView.findViewById(R.id.iv_butler);
                    viewHolderLeft.tv_left_text = convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeft);
                    break;
                case ChatListData.TYPE_RIGHT_TEXT:
                    viewHolderRight = new ViewHolderRight();
                    convertView = inflater.inflate(R.layout.chat_right_item, null);
                    viewHolderRight.iv_user = convertView.findViewById(R.id.iv_user);
                    viewHolderRight.tv_right_text = convertView.findViewById(R.id.tv_right);
                    convertView.setTag(viewHolderRight);
                    break;
            }
        } else {
            switch (type) {
                case ChatListData.TYPE_LEFT_TEXT:
                    viewHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case ChatListData.TYPE_RIGHT_TEXT:
                    viewHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }

        //设置数据
        data = mList.get(position);
        switch (type) {
            case ChatListData.TYPE_LEFT_TEXT:
                viewHolderLeft.tv_left_text.setText(data.getContent());
                viewHolderLeft.iv_butler.setImageResource(R.drawable.assistant);
                break;
            case ChatListData.TYPE_RIGHT_TEXT:
                viewHolderRight.tv_right_text.setText(data.getContent());
                Bitmap bitmap = ShareUtils.getBitMap(mContext,"image_title",null);
                if (bitmap != null) {
                    viewHolderRight.iv_user.setImageBitmap(bitmap);
                }
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    class ViewHolderLeft {
        ImageView iv_butler;
        TextView tv_left_text;
    }

    class ViewHolderRight {
        ImageView iv_user;
        TextView tv_right_text;
    }
}
