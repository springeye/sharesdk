package org.henjue.library.share.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.henjue.library.share.ShareListener;
import org.henjue.library.share.ShareSDK;
import org.henjue.library.share.Message;
import org.henjue.library.share.model.MessageWebpage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by echo on 5/18/15.
 */
public class QQShareManager implements IShareManager {
    public static final int SHARE_TYPE_QQ = 0;
    public static final int SHARE_TYPE_QZONE = 1;
    final File sharesdk = new File(Environment.getExternalStorageDirectory(), "sharesdk");
    private String mAppId;

    private Tencent mTencent;

    private QQShare mQQShare;
    private QzoneShare mQZoneShare;
    private Context mContext;
    private ShareListener mListener;


    QQShareManager(Context context) {
        mAppId = ShareSDK.getInstance().getQQAppId();
        mContext = context;
        if (!TextUtils.isEmpty(mAppId)) {
            mTencent = Tencent.createInstance(mAppId, context);
            mQQShare = new QQShare(context, mTencent.getQQToken());
            mQZoneShare = new QzoneShare(context, mTencent.getQQToken());
        }
    }


    private void shareWebPage(Activity activity, MessageWebpage message, int shareType) {
        Bundle params = new Bundle();
        if (shareType == SHARE_TYPE_QQ) {
            shareWebPageQQ(activity, message, params);
        } else if (shareType == SHARE_TYPE_QZONE) {
            shareWebPageQZone(activity, message, params);
        }
    }

    private void shareWebPageQZone(Activity activity, MessageWebpage message, Bundle params) {
        if (!sharesdk.exists()) {
            sharesdk.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            final File file = new File(sharesdk, System.currentTimeMillis() + ".png");
            fos = new FileOutputStream(file);
            message.getImage().compress(Bitmap.CompressFormat.PNG, 100, fos);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, message.getTitle());
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, message.getURL());
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message.getDescription());
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_APP);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, new ArrayList<String>() {{
                add(file.toString());
            }});
            doShareToQZone(activity, params);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void shareWebPageQQ(Activity activity, MessageWebpage message, Bundle params) {
        if (!sharesdk.exists()) {
            sharesdk.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            File file = new File(sharesdk, System.currentTimeMillis() + ".png");
            fos = new FileOutputStream(file);
            message.getImage().compress(Bitmap.CompressFormat.PNG, 100, fos);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, message.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, message.getURL());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, message.getDescription());
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, file.toString());
            doShareToQQ(activity, params);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 用异步方式启动分享
     *
     * @param params
     */
    private void doShareToQZone(final Activity activity, final Bundle params) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mQZoneShare != null) {
                    mQZoneShare.shareToQzone(activity, params, iUiListener);
                }
            }
        });
    }


    /**
     * 用异步方式启动分享
     *
     * @param params
     */
    private void doShareToQQ(final Activity activity, final Bundle params) {
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
            sharesdk.deleteOnExit();
            ThreadManager.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    mListener.onCancel();
                }
            });
        }

        @Override
        public void onComplete(Object response) {
            sharesdk.deleteOnExit();
            ThreadManager.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSuccess();
                }
            });
        }

        @Override
        public void onError(UiError e) {
            sharesdk.deleteOnExit();
            ThreadManager.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFaild();
                }
            });
        }
    };


    @Override
    public void share(Message message, int shareType, ShareListener listener) {
        this.mListener = listener == null ? ShareListener.DEFAULT : listener;
        shareWebPage((Activity) mContext, (MessageWebpage) message, shareType);
    }

    @Override
    public void share(Message message, int shareType) {
        share(message, shareType, ShareListener.DEFAULT);
    }
}
