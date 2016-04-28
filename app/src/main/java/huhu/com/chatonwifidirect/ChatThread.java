package huhu.com.chatonwifidirect;

import java.io.IOException;

/**
 * Created by Huhu on 4/27/16.
 * 聊天线程基类
 */
public abstract class ChatThread extends Thread {
    //发送信息的方法
    public abstract void write(ChatEntity entity) throws IOException;
}
