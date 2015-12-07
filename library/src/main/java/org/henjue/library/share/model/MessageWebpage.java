package org.henjue.library.share.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.henjue.library.share.Message;
import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 * 分享网页模式
 */
public class MessageWebpage implements Message.Web {

    private final String description;
    private Bitmap mBitmap;
    private String title;


    private String url;


    public MessageWebpage(String title, String description,
                          String url, String imageUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.mBitmap = BitmapFactory.decodeFile(imageUrl);
        if(this.mBitmap==null){
            throw new RuntimeException("Bitmap Not Decode!");
        }
    }
    public MessageWebpage(String title, String description,
                          String url, Bitmap bitmap) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.mBitmap=bitmap;
        if(this.mBitmap==null){
            throw new RuntimeException("Bitmap Not Null!");
        }
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
    public String getDescription() {
        return description;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.WEBPAGE;
    }
    @Override
    public Bitmap getImage() {
        return mBitmap;
    }
}
