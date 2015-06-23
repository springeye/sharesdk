package org.henjue.library.share.model;

import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 */

public abstract class Message {
    public abstract Type.Share getShareType();
    public abstract String getContent();
    public abstract String getTitle();
    public abstract String getURL();
    public abstract String getImageUrl();
    public abstract String getMusicUrl();

}
