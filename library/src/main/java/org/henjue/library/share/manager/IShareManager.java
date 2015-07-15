package org.henjue.library.share.manager;

import org.henjue.library.share.ShareListener;
import org.henjue.library.share.model.Message;

/**
 * Created by echo on 5/21/15.
 */
public interface IShareManager {

    void share(Message message, int shareType,ShareListener listener);
    void share(Message message, int shareType);

}
