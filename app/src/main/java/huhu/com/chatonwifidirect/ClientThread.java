package huhu.com.chatonwifidirect;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Huhu on 4/26/16.
 * 客户端运行的线程
 */
public class ClientThread extends ChatThread {
    //上下文对象
    private Context context;

    //服务器IP地址
    private InetAddress ADDRESS;
    //服务器端口号
    private int PORT;
    //客户端socket
    private Socket socket;
    //输入输出流
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    //handler实例
    private Handler handler;

    public ClientThread(InetAddress IP, int PORT, Handler handler) {
        this.ADDRESS = IP;
        this.PORT = PORT;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.bind(null);
            //设置5秒超时时间
            socket.connect(new InetSocketAddress(ADDRESS, PORT), 5000);
            //获取数据输入输出流
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                try {
                    ChatEntity chatEntity = (ChatEntity) inputStream.readObject();
                    //发送message给chatActivity更新数据
                    Message msg = new Message();
                    msg.obj = chatEntity;
                    msg.what = 2;
                    handler.sendMessage(msg);

                    Log.e(chatEntity.getName() + "说：", chatEntity.getWord());
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
                socket.close();
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
