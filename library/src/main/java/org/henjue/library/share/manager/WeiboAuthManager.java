package org.henjue.library.share.manager;

import android.app.Activity;
import android.content.Context;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.model.AuthInfo;
import org.henjue.library.share.weibo.UsersAPI;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


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
        mSsoHandler = new SsoHandler((Activity) mContext);
        mSsoHandler.authorize(new AuthListener());

    }

    /**
     * * 1. SSO 授权时，需要在 onActivityResult 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非SSO 授权时，当授权结束后，该回调就会被执行
     */
    private class AuthListener implements WbAuthListener, UsersAPI.UserRequestListener {

        @Override
        public void onSuccess(Oauth2AccessToken accessToken) {
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
                userAPI = new UsersAPI(mContext, mSinaAppKey, accessToken);
                userAPI.show(this);
            }
        }

        @Override
        public void cancel() {
            if (mAuthListener != null) {
                mAuthListener.onCancel();
            }
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if (mAuthListener != null) {
                mAuthListener.onError(new RuntimeException(String.format(Locale.getDefault(), "error_code:%d,error_msg:%s", wbConnectErrorMessage.getErrorCode(), wbConnectErrorMessage.getErrorMessage())));
            }
        }


        @Override
        public void onGetUserInfoSuccess(JSONObject info, String token, long expiresTime) {
            try {
                String nickname = info.getString("screen_name");
                String avatar = info.getString("profile_image_url");
                String id = info.getString("id");
                AuthInfo autoInfo = new AuthInfo(info.toString(), nickname, avatar, id, token, expiresTime);
                mAuthListener.onComplete(autoInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                mAuthListener.onError(e);
            }
        }

        @Override
        public void onError(Throwable e) {
            mAuthListener.onError(e);
        }
    }


}
