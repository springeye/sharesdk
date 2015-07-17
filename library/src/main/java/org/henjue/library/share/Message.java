package org.henjue.library.share;

import android.graphics.Bitmap;

/**
 * Created by echo on 5/18/15.
 */

public interface Message {
    Type.Share getShareType();
    interface Picture extends Message{
        Bitmap getImage();
    }
    interface Web extends Base{
        String getURL();
        String getDescription();
        Bitmap getImage();
    }
    interface Base extends Message{
        String getTitle();
    }
    interface Text extends Message{
        String getContent();

    }
    interface Music extends Web{
        String getMusicUrl();

    }
}
