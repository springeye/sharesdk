package org.henjue.library.share.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.henjue.library.share.Message;
import org.henjue.library.share.Type;

import java.io.IOException;
import java.net.URL;

/**
 * Created by echo on 5/18/15.
 * 分享图片模式
 */
public class MessagePic implements Message.Picture{

    private Bitmap mBitmap;

    public MessagePic(String imageUrl) {
        try {
            this.mBitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MessagePic(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    @Override
    public Type.Share getShareType() {
        return Type.Share.IMAGE;
    }

    @Override
    public Bitmap getImage() {
        return mBitmap;
    }
}
