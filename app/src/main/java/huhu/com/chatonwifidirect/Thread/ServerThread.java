package huhu.com.chatonwifidirect.Thread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import huhu.com.chatonwifidirect.Entity.ChatEntity;
import huhu.com.chatonwifidirect.Util.Constants;
import huhu.com.chatonwifidirect.Util.Record;

/**
 * Created by Huhu on 4/26/16.
 */
public class ServerThread extends ChatThread {
    //端口号
    private static int PORT;
    //服务器接收端
    private ServerSocket serverSocket;
    //客户端socket
    private Socket client;
    //输入输出流
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    //上下文对象
    private Context context;

    public ServerThread(int PORT, Context context) {
        this.PORT = PORT;
        this.context = context;

    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            client = serverSocket.accept();
            //获取数据输入输出流,注意跟客户端对应
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
            while (true) {
                try {
                    ChatEntity chatEntity = (ChatEntity) inputStream.readObject();
                    //将接收到的消息体加入消息记录列表
                    Record.CHAT_RECORD.add(chatEntity);
                    //发送广播通知列表刷新数据
                    Intent i = new Intent();
                    i.setAction(Constants.UPDATE_LIST_ACTION);
                    context.sendBroadcast(i);

                } catch (IOException e) {
                    Log.e("IOException", "disconnected", e);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void write(ChatEntity entity) throws IOException {
        outputStream.writeObject(entity);
        outputStream.flush();
    }

}

