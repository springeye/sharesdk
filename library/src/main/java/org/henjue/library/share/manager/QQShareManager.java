package org.henjue.library.share.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.henjue.library.share.R;
import org.henjue.library.share.ShareListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.model.Message;

/**
 * Created by echo on 5/18/15.
 */
public class QQShareManager implements IShareManager {

    public static final int QZONE_SHARE_TYPE = 0;


    private String mAppId;

    private Tencent mTencent;

    private QQShare mQQShare;

    private Context mContext;
    private ShareListener mListener;


    QQShareManager(Context context) {
        mAppId = ShareSDK.getInstance().getQQAppId();
        mContext = context;
        if (!TextUtils.isEmpty(mAppId)) {
            mTencent = Tencent.createInstance(mAppId, context);
            mQQShare = new QQShare(context, mTencent.getQQToken());
        }
    }


    private void shareWebPage(Activity activity, Message message) {
        Bundle params = new Bundle();
        shareWebPageQzone(activity, message, params);
    }


    private void shareWebPageQzone(Activity activity, Message message, Bundle params) {


        params.putString(QQShare.SHARE_TO_QQ_TITLE, message.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, message.getURL());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, message.getContent());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, message.getImageUrl());
        doShareToQQ(activity, params);

    }


    /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Activity activity,final Bundle params) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mQQShare != null) {
                    mQQShare.shareToQQ(activity, params, iUiListener);
                }
            }
        });
    }


    private final IUiListener iUiListener = new IUiListener() {
        @Override
        public void onCancel() {
            mListener.onCancel();
        }

        @Override
        public void onComplete(Object response) {
            mListener.onSuccess();
        }

        @Override
        public void onError(UiError e) {
            mListener.onFaild();
        }
    };


    @Override
    public void share(Message message, int shareType, ShareListener listener) {
        if(listener==null){
            share(message,shareType);
        }else{
            this.mListener=listener;
        }
    }

    @Override
    public void share(Message message, int shareType) {
        this.mListener=ShareListener.DEFAULT;
        shareWebPage((Activity) mContext, message);
    }
}
