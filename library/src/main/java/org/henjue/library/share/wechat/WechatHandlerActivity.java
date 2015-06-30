/*
 * Copyright (C) 2015 Henjue, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.henjue.library.share.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;

import org.henjue.library.hnet.Callback;
import org.henjue.library.hnet.HNet;
import org.henjue.library.hnet.Response;
import org.henjue.library.hnet.exception.HNetError;
import org.henjue.library.hnet.typed.TypedByteArray;
import org.henjue.library.share.AuthListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.api.WechatApiService;
import org.henjue.library.share.manager.WechatAuthManager;
import org.henjue.library.share.model.AuthInfo;
import org.henjue.library.share.util.ResUtils;
import org.json.JSONObject;


public class WechatHandlerActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    private AuthListener mAuthListener;

    public static final String API_URL = "https://api.weixin.qq.com";

    /**
     * BaseResp的getType函数获得的返回值，1:第三方授权， 2:分享
     */
    private static final int TYPE_LOGIN = 1;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WechatHandlerActivity.this;
        mIWXAPI = WechatAuthManager.getIWXAPI();
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(final BaseResp resp) {


        mAuthListener = WechatAuthManager
                .getPlatformActionListener();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (resp.getType() == TYPE_LOGIN) {
                    final String code = ((SendAuth.Resp) resp).token;
                    WechatApiService api = getApiService();
                    api.getAccessToken(ShareSDK.getInstance().getWechatAppId(), ShareSDK.getInstance().getWechatSecret(), code, "authorization_code", new AccessTokenCallback(mAuthListener,api));
                } else {
                    Toast.makeText(mContext, ResUtils.getString(mContext, "share_success"), Toast.LENGTH_SHORT).show();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (resp.getType() == TYPE_LOGIN) {
                    if (mAuthListener != null) {
                        mAuthListener.onCancel();
                    }
                } else {
                    Toast.makeText(mContext, ResUtils.getString(mContext, "share_cancel"), Toast.LENGTH_SHORT).show();
                }

                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                if (resp.getType() == TYPE_LOGIN) {
                    if (mAuthListener != null) {
                        mAuthListener.onError();
                    }
                } else {
                    Toast.makeText(mContext, ResUtils.getString(mContext, "share_failed"), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        finish();
    }

    private WechatApiService getApiService() {
        return new HNet.Builder()
                .setEndpoint(API_URL)
                .build().create(WechatApiService.class);
    }
}
