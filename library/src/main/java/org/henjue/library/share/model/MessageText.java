package org.henjue.library.share.model;


import org.henjue.library.share.Message;
import org.henjue.library.share.Type;

/**
 * Created by echo on 5/18/15.
 * 分享文本内容
 */
public class MessageText  implements Message.Text{

    private String content;

    public MessageText(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }


    @Override
    public Type.Share getShareType() {
        return Type.Share.TEXT;
    }

}