package huhu.com.chatonwifidirect;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Huhu on 4/26/16.
 */
public class ServerThread extends ChatThread {
    //上下文对象
    private Context context;
    //端口号
    private static int PORT;
    //服务器接收端
    private ServerSocket serverSocket;
    //客户端socket
    private Socket client;
    //输入输出流
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    //handler实例
    private Handler handler;

    public ServerThread(int PORT, Handler handler) {
        this.PORT = PORT;
        this.handler = handler;
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
                    //如果收到结束信号，则跳出循环
                    if (chatEntity.getWord().equals(Constants.endSignal) ) {
                        Message msg = new Message();
                        msg.what=3;
                        handler.sendMessage(msg);
                        break;
                    }
                    Message msg = new Message();
                    msg.obj = chatEntity;
                    msg.what = 2;
                    handler.sendMessage(msg);
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
