package huhu.com.chatonwifidirect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Huhu on 4/26/16.
 * 客户端运行的线程
 */
public class ClientThread extends Thread {
    //服务器IP地址
    private String STR_IP;
    //服务器端口号
    private int PORT;
    //客户端socket
    private Socket socket;

    public ClientThread(String IP, int PORT) {
        this.STR_IP = IP;
        this.PORT = PORT;
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(STR_IP,5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
