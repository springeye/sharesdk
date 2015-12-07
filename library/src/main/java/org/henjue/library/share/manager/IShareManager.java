package org.henjue.library.share.manager;

import org.henjue.library.share.ShareListener;
import org.henjue.library.share.Message;

/**
 * Share Manager Interface
 */
public interface IShareManager {
    /**
     * @param message   share message object
     * @param shareType share type for platfomr
     * @param listener  share callback listener
     */
    void share(Message message, int shareType, ShareListener listener);

    /**
     * @param message   share message object
     * @param shareType share type for platfomr
     */
    void share(Message message, int shareType);
}
