package org.henjue.library.share.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.henjue.library.share.Message;
import org.henjue.library.share.Type;

import java.io.IOException;
import java.net.URL;

/**
 * Created by echo on 5/18/15.
 * 音乐模式
 */
public class MessageMusic implements Message.Music{

    private final String description;
    private Bitmap mBitmap;
    private String title;

    private String content;

    private String url;


    private String musicUrl;
    public MessageMusic(String title, String description,
                        String url, Bitmap bitmap, String musicUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.mBitmap = bitmap;
        this.musicUrl = musicUrl ;
    }
    public MessageMusic(String title, String description,
                        String url, String imageUrl, String musicUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.musicUrl = musicUrl ;
        try {
            this.mBitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
        } catch (IOException e) {
            e.printStackTrace();
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
    public String getMusicUrl() {
        return musicUrl;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.MUSIC;
    }

    @Override
    public Bitmap getImage() {
        return mBitmap;
    }
}
