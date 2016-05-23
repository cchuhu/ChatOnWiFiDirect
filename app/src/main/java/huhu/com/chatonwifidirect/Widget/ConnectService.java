package huhu.com.chatonwifidirect.Widget;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.InetAddress;

import huhu.com.chatonwifidirect.Entity.ChatEntity;
import huhu.com.chatonwifidirect.Thread.ChatThread;
import huhu.com.chatonwifidirect.Thread.ClientThread;
import huhu.com.chatonwifidirect.Thread.ServerThread;
import huhu.com.chatonwifidirect.Util.Constants;

/**
 * 建立连接并且在后台接收消息的service
 */

public class ConnectService extends Service {
    //实例化binder类
    public final MyBinder myBinder = new MyBinder();
    //是否是组长的标志位
    public Boolean isOwner = false;
    //服务器和客户端线程
    private ChatThread chatThread;
    //广播接收
    private MessageReceiver messageReceiver = new MessageReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_MESSAGE_ACTION);
        registerReceiver(messageReceiver, filter);
        return myBinder;
    }

    /**
     * 初始化socket连接，要判定谁是组长
     *
     * @param isOwner
     */
    public void initSocket(Boolean isOwner, InetAddress inetAddress) {
        //如果是组长
        if (isOwner) {
            chatThread = new ServerThread(Constants.PORT, this);
        }
        //如果不是组长
        else {
            chatThread = new ClientThread(inetAddress, Constants.PORT, this);
        }
        chatThread.start();
    }

    /**
     * 将信息发送给对方
     *
     * @param chatEntity 消息体
     */
    public void sendMessage(ChatEntity chatEntity) throws IOException {

        chatThread.write(chatEntity);
    }

    /**
     * 接收用户发送消息的广播
     */
    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.NEW_MESSAGE_ACTION:
                    try {

                        sendMessage((ChatEntity) intent.getExtras().get("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }


    /**
     * 自定义Binder类
     */
    public class MyBinder extends Binder {
        public ConnectService getService() {
            return ConnectService.this;
        }
    }
}
