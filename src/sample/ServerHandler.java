package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

public class ServerHandler extends Thread {
    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final HashSet<String> names = new HashSet<>();
    private static HashSet<PrintWriter> writers = new HashSet<>();
    private MessageListener listener;

    public ServerHandler(Socket socket,MessageListener listener) {
        this.socket = socket;
        this.listener=listener;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                if (name == null) {
                    return;
                }
                synchronized (names) {
                    if (!names.contains(name)) {
                        listener.onMessageReceived(name+" authenticated");
                        names.add(name);
                        break;
                    }
                }
            }

            out.println("NAMEACCEPTED");
            writers.add(out);

            while (true) {
                String input = in.readLine();
                if (input == null) {
                    return;
                }
                for (PrintWriter writer : writers) {
                    System.out.println(input);
                    listener.onMessageReceived(name+":"+ input);
                    writer.println(input);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (name != null) {
                names.remove(name);
            }
            if (out != null) {
                writers.remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
