package org.henjue.library.share.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import org.henjue.library.share.R;
import org.henjue.library.share.ShareListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.Type;
import org.henjue.library.share.Message;
import org.henjue.library.share.util.ShareUtil;

/**
 * Created by echo on 5/18/15.
 */
public class WechatShareManager implements IShareManager {


    /**
     * friends
     */
    public static final int WEIXIN_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;

    /**
     * friends TimeLine
     */
    public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;


    private static final int THUMB_SIZE = 150;


    private Context mContext;


    private IWXAPI mIWXAPI;


    private String mWeChatAppId;
    private static ShareListener mListener;


    WechatShareManager(Context context) {
        mContext = context;
        mWeChatAppId = ShareSDK.getInstance().getWechatAppId();
        if (!TextUtils.isEmpty(mWeChatAppId)) {
            initWeixinShare(context);
        }

    }


    private void initWeixinShare(Context context) {
        mIWXAPI = WXAPIFactory.createWXAPI(context, mWeChatAppId, true);
        if (!mIWXAPI.isWXAppInstalled()) {
            Toast.makeText(context, R.string.share_install_wechat_tips, Toast.LENGTH_SHORT).show();
            return;
        }else{
            mIWXAPI.registerApp(mWeChatAppId);
        }
    }


    private void shareText(int shareType, Message.Text message) {

        String text = message.getContent();
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = ShareUtil.buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        mIWXAPI.sendReq(req);
    }


    private void sharePicture(int shareType, Message.Picture message) {
        Bitmap bitmap = message.getImage();
        Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, false);
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        if(thumbImage!=null){
            msg.setThumbImage(thumbImage);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType;
        mIWXAPI.sendReq(req);

    }


    private void shareWebPage(int shareType, Message.Web message) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = message.getURL();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = message.getTitle();
        msg.description = message.getDescription();

        Bitmap bmp =Bitmap.createScaledBitmap(message.getImage(), THUMB_SIZE, THUMB_SIZE, true);
        if (bmp == null) {
            Toast.makeText(mContext, R.string.share_pic_empty,
                    Toast.LENGTH_SHORT).show();
        } else {
            msg.setThumbImage(bmp);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        mIWXAPI.sendReq(req);
    }


    private void shareMusic(int shareType,Message.Music message) {

        WXMusicObject music = new WXMusicObject();
        //Str1+"#wechat_music_url="+str2 ;str1是网页地址，str2是音乐地址。

        music.musicUrl = message.getURL()+ "#wechat_music_url="+ message.getMusicUrl();
        WXMediaMessage msg = new WXMediaMessage(music);
        msg.title = message.getTitle();
        msg.description = message.getDescription();

        Bitmap thumb =Bitmap.createScaledBitmap(message.getImage(),THUMB_SIZE,THUMB_SIZE,true);
        if (thumb == null) {
            Toast.makeText(mContext,R.string.share_pic_empty,
                    Toast.LENGTH_SHORT).show();
        } else {
            msg.setThumbImage(thumb);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtil.buildTransaction("music");
        req.message = msg;
        req.scene = shareType;
        mIWXAPI.sendReq(req);
    }

    @Override
    public void share(Message content, int shareType, ShareListener listener) {
        mListener=listener==null?ShareListener.DEFAULT:listener;
        if(content.getShareType()== Type.Share.TEXT){
            shareText(shareType, (Message.Text)content);
        }else if(content.getShareType()== Type.Share.IMAGE){
            sharePicture(shareType,(Message.Picture) content);
        }else if(content.getShareType()== Type.Share.WEBPAGE){
            shareWebPage(shareType,(Message.Web) content);
        }else if(content.getShareType()== Type.Share.MUSIC){
            shareMusic(shareType, ((Message.Music)content));
        }
    }

    @Override
    public void share(Message content,int shareType) {
        share(content,shareType,ShareListener.DEFAULT);
    }
    public static ShareListener getPlatformActionListener() {
        return mListener;
    }


}
