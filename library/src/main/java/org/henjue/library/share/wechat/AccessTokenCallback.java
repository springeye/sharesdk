package org.henjue.library.share.wechat;

import org.henjue.library.hnet.Callback;
import org.henjue.library.hnet.Response;
import org.henjue.library.hnet.exception.HNetError;
import org.henjue.library.hnet.typed.TypedByteArray;
import org.henjue.library.share.AuthListener;
import org.henjue.library.share.api.WechatApiService;
import org.json.JSONObject;

/**
 * Created by android on 2015/6/30.
 */
class AccessTokenCallback implements Callback<Response> {
    private final AuthListener mAuthListener;
    private final WechatApiService mApi;

    AccessTokenCallback(AuthListener mAuthListener, WechatApiService api) {
        this.mAuthListener = mAuthListener;
        this.mApi = api;
    }

    @Override
    public void start() {

    }

    @Override
    public void success(Response response, Response response2) {
        try {
            String json = new String(
                    ((TypedByteArray) response.getBody())
                            .getBytes());

            JSONObject jsonObject = new JSONObject(json);
            final String accessToken = jsonObject.getString("access_token");
            final String openId = jsonObject.getString("openid");
            final long expiresTime = jsonObject.getLong("expires_in");
            mApi.getWechatUserInfo(accessToken, openId, new UserInfoCallback(mAuthListener, openId, accessToken, expiresTime));
        } catch (Exception e) {
            e.printStackTrace();
            if (mAuthListener != null) {
                mAuthListener.onError();
            }
        }
    }

    @Override
    public void failure(HNetError error) {
        if (mAuthListener != null) {
            mAuthListener.onError();
        }
    }

    @Override
    public void end() {

    }
}
