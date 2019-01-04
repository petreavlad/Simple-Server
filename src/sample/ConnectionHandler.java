package sample;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionHandler extends Thread {

    private int socketNumber;
    private MessageListener listener;

    ConnectionHandler(int socketNumber,MessageListener listener) {
        this.socketNumber = socketNumber;
        this.listener=listener;
    }

    @Override
    public void run() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(socketNumber);

            while (true) {
                new ServerHandler(socket.accept(),listener).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
