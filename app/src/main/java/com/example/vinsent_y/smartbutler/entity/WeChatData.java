package com.example.vinsent_y.smartbutler.entity;

import java.io.Serializable;

public class WeChatData implements Serializable {
    private String title;
    private String source;
    private String url;

    public WeChatData() {
    }

    public WeChatData(String title, String source, String url) {
        this.title = title;
        this.source = source;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
