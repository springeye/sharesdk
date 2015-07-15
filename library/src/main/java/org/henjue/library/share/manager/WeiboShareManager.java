package org.henjue.library.share.manager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import org.henjue.library.share.R;
import org.henjue.library.share.ShareListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.Type;
import org.henjue.library.share.model.Message;
import org.henjue.library.share.util.ShareUtil;
import org.henjue.library.share.weibo.AccessTokenKeeper;

/**
 * Created by echo on 5/18/15.
 */
public class WeiboShareManager implements IShareManager {


    private static String mSinaAppKey;

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    private Context mContext;

    public static final int WEIBO_SHARE_TYPE = 0;


    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mSinaAPI;
    private ShareListener mListener;


    WeiboShareManager(Context context) {
        mContext = context;
        mSinaAppKey = ShareSDK.getInstance().getWeiboAppId();
        if (!TextUtils.isEmpty(mSinaAppKey)) {
            // 创建微博 SDK 接口实例
            mSinaAPI = WeiboShareSDK.createWeiboAPI(context, mSinaAppKey);
            mSinaAPI.registerApp();
        }
    }


    private void shareText(Message message) {

        //初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(message.getContent());
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinatext");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);

    }

    private void sharePicture(Message message) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.imageObject = getImageObj(message.getImageUrl());
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinapic");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
    }

    private void shareWebPage(Message message) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(message.getContent());
        weiboMultiMessage.imageObject = getImageObj(message.getImageUrl());
        // 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = ShareUtil.buildTransaction("sinawebpage");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);

    }


    private void shareMusic(Message message) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.mediaObject = getMusicObj(message);
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinamusic");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
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
    private ImageObject getImageObj(String imageUrl) {
        ImageObject imageObject = new ImageObject();
        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        imageObject.setImageObject(bmp);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(Message message) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = message.getTitle();
        mediaObject.description = message.getContent();

        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap bmp = ShareUtil.extractThumbNail(message.getImageUrl(), 150, 150, true);
        mediaObject.setThumbImage(bmp);
        mediaObject.actionUrl = message.getURL();
        mediaObject.defaultText = message.getContent();
        return mediaObject;
    }


    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj(Message message) {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = message.getTitle();
        musicObject.description = message.getContent();

        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap bmp = BitmapFactory.decodeFile(message.getImageUrl());
        musicObject.setThumbImage(bmp);
        musicObject.actionUrl = message.getURL();
        musicObject.dataUrl =  ShareSDK.getInstance().getSinaRedirectUrl();
        musicObject.dataHdUrl = ShareSDK.getInstance().getSinaRedirectUrl();
        musicObject.duration = 10;
        musicObject.defaultText = message.getContent();
        return musicObject;
    }


    private void allInOneShare(final Context context, SendMultiMessageToWeiboRequest request) {

        AuthInfo authInfo = new AuthInfo(context, mSinaAppKey, ShareSDK.getInstance().getSinaRedirectUrl(), SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }

        mSinaAPI.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                mListener.onFaild();
            }

            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);
                mListener.onSuccess();
            }

            @Override
            public void onCancel() {
                mListener.onCancel();

            }
        });

    }


    @Override
    public void share(Message message, int shareType, ShareListener listener) {
        if(listener==null){
            share(message,shareType);
        }else{
            this.mListener=listener;
        }
    }

    @Override
    public void share(Message content, int shareType) {

        if (mSinaAPI == null) {
            return;
        }
        this.mListener=ShareListener.DEFAULT;
        if(content.getShareType()== Type.Share.TEXT){
            shareText( content);
        }else if(content.getShareType()== Type.Share.IMAGE){
            sharePicture( content);
        }else if(content.getShareType()== Type.Share.WEBPAGE){
            shareWebPage( content);
        }else if(content.getShareType()== Type.Share.MUSIC){
            shareMusic( content);
        }


    }
}
