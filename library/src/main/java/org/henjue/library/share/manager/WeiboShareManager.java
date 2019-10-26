package org.henjue.library.share.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;


import org.henjue.library.share.Message;
import org.henjue.library.share.ShareListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.Type;

import java.util.UUID;

/**
 * Created by echo on 5/18/15.
 */
public class WeiboShareManager implements IShareManager, Application.ActivityLifecycleCallbacks {


    private static String mSinaAppKey;

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private final String mSinaRedirectUrl;
    private final String mSinaScope;


    private Context mContext;

    public static final int WEIBO_SHARE_TYPE = 0;


    /**
     * 微博分享的接口实例
     */
    private WbShareHandler mShareHandler;
    private ShareListener mListener;
    private Activity currentActivity;


    WeiboShareManager(Context context) {
        Application app = (Application) context.getApplicationContext();
        app.registerActivityLifecycleCallbacks(this);
        mContext = context;
        mSinaAppKey = ShareSDK.getInstance().getWeiboAppId();
        mSinaRedirectUrl = ShareSDK.getInstance().getSinaRedirectUrl();
        mSinaScope = ShareSDK.getInstance().getWeiboScope();
        if (!TextUtils.isEmpty(mSinaAppKey)) {
            // 创建微博 SDK 接口实例
            AuthInfo mAuthInfo = new AuthInfo(context, mSinaAppKey, mSinaRedirectUrl, mSinaScope);
            WbSdk.install(context, mAuthInfo);
        }
    }


    private void shareText(Message.Text message) {

        //初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(message.getContent());
//        //初始化从第三方到微博的消息请求
//        request.transaction = ShareUtil.buildTransaction("sinatext");
//        request.multiMessage = weiboMultiMessage;
        mShareHandler.shareMessage(weiboMultiMessage, false);

    }

    private void sharePicture(Message.Picture message) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.imageObject = getImageObj(message.getImage());
        //初始化从第三方到微博的消息请求
//        request.transaction = ShareUtil.buildTransaction("sinapic");
//        request.multiMessage = weiboMultiMessage;
        mShareHandler.shareMessage(weiboMultiMessage, false);
    }

    private void shareWebPage(Message.Web message) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(message.getDescription());
        weiboMultiMessage.imageObject = getImageObj(message.getImage());
        // 初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = ShareUtil.buildTransaction("sinawebpage");
//        request.multiMessage = weiboMultiMessage;
//        allInOneShare(mContext, request);
        mShareHandler.shareMessage(weiboMultiMessage, false);

    }


    private void shareMusic(Message.Music message) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.mediaObject = getMusicObj(message);
        //初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = ShareUtil.buildTransaction("sinamusic");
//        request.multiMessage = weiboMultiMessage;
        mShareHandler.shareMessage(weiboMultiMessage, false);
    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(Message.Web message) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = UUID.randomUUID().toString();
        mediaObject.title = message.getTitle();
        mediaObject.description = message.getDescription();
        mediaObject.setThumbImage(message.getImage());
        mediaObject.actionUrl = message.getURL();
        mediaObject.defaultText = message.getDescription();
        return mediaObject;
    }


    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private BaseMediaObject getMusicObj(Message.Music message) {
        // 创建媒体消息
        VideoSourceObject musicObject = new VideoSourceObject();
        musicObject.title = message.getTitle();
        musicObject.description = message.getDescription();
        // 设置 Bitmap 类型的图片到视频对象里
        musicObject.setThumbImage(message.getImage());
        musicObject.actionUrl = message.getURL();
        musicObject.videoPath = Uri.parse(message.getMusicUrl());
        return musicObject;
    }

    private void checkAndInitWBHandler() {
        if (currentActivity != null && mShareHandler == null) {
            mShareHandler = new WbShareHandler(currentActivity);
            mShareHandler.registerApp();
        }
    }


    @Override
    public void share(Message content, int shareType, ShareListener listener) {
        if (mShareHandler == null) {
            return;
        }
        checkAndInitWBHandler();
        this.mListener = listener == null ? ShareListener.DEFAULT : listener;
        if (content.getShareType() == Type.Share.TEXT) {
            shareText((Message.Text) content);
        } else if (content.getShareType() == Type.Share.IMAGE) {
            sharePicture((Message.Picture) content);
        } else if (content.getShareType() == Type.Share.WEBPAGE) {
            shareWebPage((Message.Web) content);
        } else if (content.getShareType() == Type.Share.MUSIC) {
            shareMusic((Message.Music) content);
        }
    }

    @Override
    public void share(Message content, int shareType) {
        share(content, shareType, ShareListener.DEFAULT);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
