package org.henjue.library.share.wechat;

import org.henjue.library.hnet.Callback;
import org.henjue.library.hnet.Response;
import org.henjue.library.hnet.exception.HNetError;
import org.henjue.library.hnet.typed.TypedByteArray;
import org.henjue.library.share.AuthListener;
import org.henjue.library.share.model.AuthInfo;
import org.json.JSONObject;

/**
 * Created by android on 2015/6/30.
 */
class UserInfoCallback implements Callback<Response> {
    private final AuthListener mAuthListener;
    private final String openId;
    private final String accessToken;
    private final long expiresTime;

    UserInfoCallback(AuthListener mAuthListener, String openId, String accessToken, long expiresTime) {
        this.mAuthListener = mAuthListener;
        this.openId = openId;
        this.accessToken = accessToken;
        this.expiresTime = expiresTime;
    }

    @Override
    public void start() {

    }

    @Override
    public void success(Response response, Response response2) {
        String json = new String(((TypedByteArray) response.getBody()).getBytes());
        try {

            JSONObject jsonObject = new JSONObject(json);
            String nickname = jsonObject.getString("nickname");
            String headimg = jsonObject.getString("headimgurl");
            AuthInfo info = new AuthInfo(json, nickname, headimg, openId, accessToken, expiresTime);
            if (mAuthListener != null) {
                mAuthListener.onComplete(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (mAuthListener != null) {
                mAuthListener.onError(e);
            }
        }
    }

    @Override
    public void failure(HNetError error) {
        if (mAuthListener != null) {
            mAuthListener.onError(error);
        }
    }

    @Override
    public void end() {

    }
}
