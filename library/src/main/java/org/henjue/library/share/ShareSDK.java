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
package org.henjue.library.share;

public class ShareSDK {


    private static ShareSDK mInstance;

    private ShareSDK() {
    }

    public static ShareSDK getInstance() {
        if (mInstance == null) {
            mInstance = new ShareSDK();
        }
        return mInstance;
    }

    private String mWechatAppId = "";
    private String mWeiboAppId = "";
    private String mWeiboScope = "";
    private String mQQAppId = "";
    private String mWechatSecret = "";

    private String mSinaRedirectUrl = "";


    /**
     * init all config
     *
     * @param wechatAppId
     * @param weiboAppId
     * @param qqAppId
     */
    public void initShare(String wechatAppId, String weiboAppId, String qqAppId, String wechatSecret, String sinaRedirectUrl) {
        mWechatAppId = wechatAppId;
        mWeiboAppId = weiboAppId;
        mQQAppId = qqAppId;
        mWechatSecret = wechatSecret;
        this.mSinaRedirectUrl = sinaRedirectUrl;

    }


    /**
     * init wechat config
     *
     * @param wechatAppId
     * @param wechatSecret
     */
    public void initWechat(String wechatAppId, String wechatSecret) {
        mWechatAppId = wechatAppId;
        mWechatSecret = wechatSecret;
    }


    /**
     * init weibo config
     *
     * @param weiboAppId
     */
    public void initWeibo(String weiboAppId, String sinaRedirectUrl, String scope) {

        mWeiboAppId = weiboAppId;
        this.mSinaRedirectUrl = sinaRedirectUrl;
        this.mWeiboScope = scope;
    }

    /**
     * init QQ config
     *
     * @param qqAppId
     */
    public void initQQ(String qqAppId) {

        mQQAppId = qqAppId;
    }


    public String getWechatAppId() {
        return mWechatAppId;
    }

    public String getWeiboScope() {
        return mWeiboScope;
    }

    public String getWeiboAppId() {
        return mWeiboAppId;
    }

    public String getQQAppId() {
        return mQQAppId;
    }

    public String getWechatSecret() {
        return mWechatSecret;
    }

    public String getSinaRedirectUrl() {
        return mSinaRedirectUrl;
    }

}
