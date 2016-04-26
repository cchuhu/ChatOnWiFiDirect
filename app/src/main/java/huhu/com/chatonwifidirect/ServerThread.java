package huhu.com.chatonwifidirect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Huhu on 4/26/16.
 */
public class ServerThread extends Thread {
    //端口号
    private static int PORT;
    //服务器接收端
    private ServerSocket serverSocket;
    //客户端socket
    private Socket client;

    public ServerThread(int PORT) {
        this.PORT = PORT;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            client = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
