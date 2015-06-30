package org.henjue.library.share.model;


import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 * 分享网页模式
 */
public class MessageWebpage extends Message {

    private String title;

    private String content;

    private String url;

    private String imageUrl;

    public MessageWebpage(String title, String content,
                          String url, String imageUrl) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.WEBPAGE;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getMusicUrl() {
        return null;
    }
}
