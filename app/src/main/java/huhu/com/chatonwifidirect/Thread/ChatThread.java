package huhu.com.chatonwifidirect.Thread;

import java.io.IOException;

import huhu.com.chatonwifidirect.Entity.ChatEntity;

/**
 * Created by Huhu on 4/27/16.
 * 聊天线程基类
 */
public abstract class ChatThread extends Thread {
    //发送信息的方法
    public abstract void write(ChatEntity entity) throws IOException;

    //结束socket的方法
    public abstract void disconnection() throws IOException;
}
