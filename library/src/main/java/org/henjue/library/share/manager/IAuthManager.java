package org.henjue.library.share.manager;

import org.henjue.library.share.AuthListener;

/**
 * Auth Manager Interface
 */
public interface IAuthManager {
    /**
     * auth login
     *
     * @param authListener auth callback listener
     */
    void login(AuthListener authListener);
}
