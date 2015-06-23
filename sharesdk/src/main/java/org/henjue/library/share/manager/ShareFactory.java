package org.henjue.library.share.manager;

import android.content.Context;

import org.henjue.library.share.Type;

/**
 * Created by android on 15-6-23.
 */
public class ShareFactory {
    public static IShareManager create(Context context,Type.Platform type){
        if(type== Type.Platform.QQ){
            return new QQShareManager(context);
        }else if(type== Type.Platform.WEIBO){
            return new WeiboShareManager(context);
        }else if(type== Type.Platform.WEIXIN){
            return new WechatShareManager(context);
        }else{
            return null;
        }
    }
}
