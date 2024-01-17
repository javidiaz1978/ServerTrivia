package ServerTrivia;

import questionmodel.Deck;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Hello world!
 */
public class ServerMain {

    public static final int PORT = 2000;
    public static ThreadPoolExecutor threadExecutor;
    public static Deck deck;
    public static int TOTAL = 1;
    public static int player = 0;
    public static String name;
    public static String hostClient;
    public static String portClient;

    public static void main(String[] args) {
        DataInputStream dIn = null;
        DataOutputStream dOut = null;

        List<Socket> services = new ArrayList<>();
        List<ServerThread> threads = new ArrayList<>();
        ExecutorService threadExecutor= null;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("entramos en server");

            while (player < TOTAL) {
                Socket service = server.accept();
                services.add(service);
                System.out.println(1 + " connected");
                String line;
                System.out.println("h");

                try {
                    dIn = new DataInputStream(service.getInputStream());
                    dOut = new DataOutputStream(service.getOutputStream());
                    if (player == 0) {
                        dOut.writeUTF("noPlayer");
                        hostClient=dIn.readUTF();
                        portClient= dIn.readUTF();
                        TOTAL = dIn.readInt();
                        //System.out.println(TOTAL);
                        name=dIn.readUTF();

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player++;
                threadExecutor= Executors.newFixedThreadPool(TOTAL);
                if (player == TOTAL) {
                    System.out.println("Llamamos al thread");

                    for (Socket sock : services) {
                        System.out.println("crea el thread");
                        ServerThread thread = new ServerThread(sock,name);

                        threads.add(thread);
                        threadExecutor.execute(thread);
                    }

                    threadExecutor.shutdown();
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}