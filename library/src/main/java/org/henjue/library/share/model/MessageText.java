package org.henjue.library.share.model;


import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 * 分享文本内容
 */
public class MessageText extends Message {

    private String content;

    public MessageText(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public String getMusicUrl() {
        return null;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.TEXT;
    }

}