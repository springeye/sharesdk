package org.henjue.library.share.manager;

import android.content.Context;

import org.henjue.library.share.Type;

/**
 * Created by android on 15-6-23.
 */
public class AuthFactory {
    public static IAuthManager create(Context context,Type.Platform type){
        if(type== Type.Platform.QQ){
            return new QQAuthManager(context);
        }else if(type== Type.Platform.WEIBO){
            return new WeiboAuthManager(context);
        }else if(type== Type.Platform.WEIXIN){
            return new WechatAuthManager(context);
        }else{
            throw new IllegalArgumentException("Error Type!");
        }
    }
}
