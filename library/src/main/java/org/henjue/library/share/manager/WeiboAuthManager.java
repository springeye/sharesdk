package org.henjue.library.share.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.model.AuthInfo;
import org.henjue.library.share.weibo.AccessTokenKeeper;
import org.henjue.library.share.weibo.model.User;
import org.henjue.library.share.weibo.model.UsersAPI;

/**
 * Created by echo on 5/19/15.
 */
public class WeiboAuthManager implements IAuthManager {



    private static final String SCOPE =
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog";

    private Context mContext;

    private static String mSinaAppKey;

    private com.sina.weibo.sdk.auth.AuthInfo mAuthInfo = null;

    private UsersAPI userAPI;

    private org.henjue.library.share.AuthListener mAuthListener;


    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private static SsoHandler mSsoHandler;


     WeiboAuthManager(Context context) {

        mContext = context;
        mSinaAppKey = ShareSDK.getInstance().getWeiboAppId();

    }


    public static SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    @Override
    public void login(org.henjue.library.share.AuthListener authListener) {
        mAuthListener = authListener;
        AccessTokenKeeper.clear(mContext);
        mAuthInfo = new com.sina.weibo.sdk.auth.AuthInfo(mContext, mSinaAppKey, ShareSDK.getInstance().getSinaRedirectUrl(), SCOPE);
        mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

    }

    /**
     * * 1. SSO 授权时，需要在 onActivityResult 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非SSO 授权时，当授权结束后，该回调就会被执行
     *
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
                userAPI = new UsersAPI(mContext, mSinaAppKey, accessToken);
                userAPI.show(Long.parseLong(accessToken.getUid()), mListener);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mAuthListener != null) {
                mAuthListener.onError();
            }
        }

        @Override
        public void onCancel() {
            if (mAuthListener != null) {
                mAuthListener.onCancel();
            }
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(mContext);
                    String nickname = user.name;
                    String headimgurl = user.avatar_large;
                    String id = user.id;
                    String token = oauth2AccessToken.getToken();
                    long expiresTime = oauth2AccessToken.getExpiresTime();
                    AuthInfo info=new AuthInfo(response,nickname, headimgurl, id, token, expiresTime);
                    if (mAuthListener != null) {
                        mAuthListener.onComplete(info);
                    }
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mAuthListener != null) {
                mAuthListener.onError();
            }
        }
    };


}
