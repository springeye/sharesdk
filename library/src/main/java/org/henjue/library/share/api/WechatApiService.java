package org.henjue.library.share.api;
import org.henjue.library.hnet.anntoation.*;
public interface WechatApiService {


    /**
     * 通过code获取access_token
     * @return
     */
    @Get("/sns/oauth2/access_token")
    void getAccessToken(@Query("appid")String appid,@Query("secret")String secret,@Query("code")String code,@Query("grant_type")String grant_type,Callback<Response> callback);


    /**
     * 获取用户个人信息（UnionID机制）
     * @return
     */
    @Get("/sns/userinfo")
    void getWechatUserInfo(@Query("access_token")String access_token,@Query("openid")String openid,Callback<Response> callback);

}
