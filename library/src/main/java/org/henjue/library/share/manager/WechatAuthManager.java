package org.henjue.library.share.manager;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.henjue.library.share.AuthListener;
import org.henjue.library.share.R;
import org.henjue.library.share.ShareSDK;

/**
 * Created by echo on 5/19/15.
 */
public class WechatAuthManager implements IAuthManager {

    private static final String SCOPE = "snsapi_userinfo";

    private static final String STATE = "lls_engzo_wechat_login";


    private String mWeChatAppId;

    private static IWXAPI mIWXAPI;

    private static AuthListener mAuthListener;


    WechatAuthManager(Context context) {
        mWeChatAppId = ShareSDK.getInstance().getWechatAppId();
        if (!TextUtils.isEmpty(mWeChatAppId)) {
            mIWXAPI = WXAPIFactory.createWXAPI(context, mWeChatAppId, true);
            if (!mIWXAPI.isWXAppInstalled()) {
                Toast.makeText(context, R.string.share_install_wechat_tips, Toast.LENGTH_SHORT).show();
                return;
            } else {
                mIWXAPI.registerApp(mWeChatAppId);
            }
        }
    }


    public static IWXAPI getIWXAPI() {
        return mIWXAPI;
    }


    public static AuthListener getPlatformActionListener() {
        return mAuthListener;
    }

    @Override
    public void login(AuthListener authListener) {
        if (mIWXAPI != null) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = SCOPE;
            req.state = STATE;
            mIWXAPI.sendReq(req);
            mAuthListener = authListener;
        }
    }
}
