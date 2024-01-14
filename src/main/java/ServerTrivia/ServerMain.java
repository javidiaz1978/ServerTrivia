package ServerTrivia;

import questionmodel.Deck;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Hello world!
 */
public class ServerMain {

    public static final int PORT = 2000;
    public static ThreadPoolExecutor threadExecutor;
    public static Deck deck;

    public static void main(String[] args) {
    threadExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    deck = new Deck();
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Listening....");


            while (true) {
                Socket service = server.accept();
                System.out.println("Connection ok");

                ServerThread st = new ServerThread(service,deck);
                threadExecutor.execute(st);

            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}


