package org.henjue.library.share.model;


import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 * 分享图片模式
 */
public class MessagePic extends Message {

    private String imageUrl;

    public MessagePic(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getContent() {
        return null;
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
        return imageUrl;
    }

    @Override
    public String getMusicUrl() {
        return null;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.IMAGE;
    }
}
