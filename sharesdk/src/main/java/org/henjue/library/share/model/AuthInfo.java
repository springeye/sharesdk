package org.henjue.library.share.model;

/**
 * Created by android on 15-6-23.
 */
public class AuthInfo {
    public final String data;//原始数据
    public final String nickname;//用户昵称
    public final String headimgurl;//用户头像
    public final String id;//用户唯一标识
    public final String token;
    public final long expiresTime;
    public AuthInfo(String data, String nickname, String headimgurl, String id, String token, long expiresTime) {
        this.data = data;
        this.nickname = nickname;
        this.headimgurl = headimgurl;
        this.id = id;
        this.token = token;
        this.expiresTime = expiresTime;
    }

}
