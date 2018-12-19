package com.example.vinsent_y.smartbutler.entity;

public class ChatListData {
    public static final int TYPE_LEFT_TEXT = 1;
    public static final int TYPE_RIGHT_TEXT = 2;

    private int type;
    private String content;

    public ChatListData() {
    }

    public ChatListData(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public static int getTypeLeftText() {
        return TYPE_LEFT_TEXT;
    }

    public static int getTypeRightText() {
        return TYPE_RIGHT_TEXT;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
