# Common Share SDK
## Build:
![build status](https://travis-ci.org/henjue/sharesdk.svg?branch=1.0.3_beta1)

Use Document to [See](http://www.j99.so/2015/06/24/Android-Share-Sdk-%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B/)!

目前发现一个BUG，下一版本会修复，临时解决方案，在你们的项目中添加如下string:
```xml
<string name="share_cancel">取消分享</string>
<string name="share_success">分享成功</string>
<string name="share_failed">分享失败</string>
<string name="share_install_wechat_tips">没有安装微信</string>
<string name="share_pic_empty">图片不能为空</string>
```

## 常见问题
1、微薄没有返回结果
在你的发起登录请求的Activity的onActivityResult方法中加入如下代码：
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);  
    mSsoHandler = WeiboAuthManager.getSsoHandler();  
    if (mSsoHandler != null) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);  
    }
}
```
2、WeChat刚被调用就返回
* 可能为一开始的一些原因（如一开始签名填写错了）导致WeChat错误缓存导致，可以清空WeChat数据试试


## changelog:
1.0.2_beta3
* 修复一些错误

1.0.1(2015-06-24)
* 更新新浪weibo sdk到v3.1.1 支持64位手机

1.0.0(2015-06-23)
* 提供微信、新浪weibo、QQ统登录和共享功能
