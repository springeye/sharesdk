package org.henjue.library.share.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.henjue.library.share.AuthListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.model.AuthInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by echo on 5/19/15.
 */
public class QQAuthManager implements IAuthManager {


    private Context mContext;

    private String mAppId;

    private Tencent mTencent;

    protected AuthListener mAuthListener;


    QQAuthManager(Context context) {
        mContext = context;
        mAppId = ShareSDK.getInstance().getQQAppId();
        if (!TextUtils.isEmpty(mAppId)) {
            mTencent = Tencent.createInstance(mAppId, context);
        }
    }


    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void login(AuthListener authListener) {
        if (!mTencent.isSessionValid()) {

            mAuthListener = authListener;
            mTencent.login((Activity) mContext, "all", new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    initOpenidAndToken(jsonObject);
                    UserInfo info = new UserInfo(mContext, mTencent.getQQToken());
                    info.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object object) {
                            try {
                                JSONObject json = (JSONObject) object;
                                String nickname = json.getString("nickname");
                                int vip = json.getInt("vip");
                                String headimg = json.getString(vip == 0 ? "figureurl_2" : "figureurl_qq_2");
                                String id = mTencent.getOpenId();
                                String token = mTencent.getAccessToken();
                                long expiresTime = mTencent.getExpiresIn();
                                AuthInfo info = new AuthInfo(json.toString(), nickname, headimg, id, token, expiresTime);
                                if (mAuthListener != null) {
                                    mAuthListener.onComplete(info);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (mAuthListener != null) {
                                    mAuthListener.onError(e);
                                }
                            }


                        }

                        @Override
                        public void onError(UiError uiError) {
                            if (mAuthListener != null) {
                                mAuthListener.onError(new RuntimeException(uiError.toString()));
                            }
                        }

                        @Override
                        public void onCancel() {
                            if (mAuthListener != null) {
                                mAuthListener
                                        .onCancel();
                            }
                        }
                    });
                }

                @Override
                public void onError(UiError uiError) {
                    if (mAuthListener != null) {
                        mAuthListener
                                .onError(new RuntimeException(uiError.toString()));
                    }
                }

                @Override
                public void onCancel() {
                    if (mAuthListener != null) {
                        mAuthListener
                                .onCancel();
                    }
                }
            });

        } else {
            mTencent.logout(mContext);
        }
    }
}


