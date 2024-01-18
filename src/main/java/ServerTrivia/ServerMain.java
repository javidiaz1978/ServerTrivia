package ServerTrivia;

import questionmodel.Deck;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class ServerMain {

    public static final int PORT = 2000;
    public static ThreadPoolExecutor threadExecutor;
    public static int TOTAL = 1;
    public static int player = 0;

    public static String hostClient;
    public static String portClient;
    public static Map<String, Integer> Results = new HashMap<>();

    public static void main(String[] args) {


        //List<Socket> services = new ArrayList<>();
        Map<ServerThread,String> threads = new HashMap<>();
        List<Player> playRes = new ArrayList<>();
        ExecutorService threadExecutor = null;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("entramos en server");

            while (player < TOTAL) {
                Socket service = server.accept();
                //services.add(service);
                System.out.println(player + " connected");
                String line;


                try {
                    String name;
                    ObjectInputStream dIn = new ObjectInputStream(service.getInputStream());
                    ObjectOutputStream dOut = new ObjectOutputStream(service.getOutputStream());
                    if (player == 0) {
                        dOut.writeUTF("noPlayer");
                        dOut.flush();
                        TOTAL = dIn.readInt();

                    } else {
                        dOut.writeUTF("ok");
                        dOut.flush();
                    }
                    player++;
                    System.out.println("mensaje nop");
                    hostClient = dIn.readUTF();
                    portClient = dIn.readUTF();

                    //System.out.println(TOTAL);
                    name = dIn.readUTF();
                    System.out.println(name);
                    threads.put(new ServerThread(service,name),name);

                    if (player == TOTAL) {
                        threadExecutor = Executors.newFixedThreadPool(TOTAL);
                        System.out.println("Llamamos al thread");

                        for (ServerThread t:threads.keySet()) {
                            System.out.println("crea el thread");

                            threadExecutor.execute(t);
                        }

                        threadExecutor.shutdown();
                        if (threadExecutor.awaitTermination(5, TimeUnit.MINUTES)) {
                            for (ServerThread t : threads.keySet()) {
                                System.out.println("primer for");
                                playRes.add(new Player(t.getNamePlayer(), t.getPoints(), t.getService()));
                            }

                            Collections.sort(playRes);
                            System.out.println("sale del for");
                            Player pWin = playRes.get(0);
                            System.out.println(pWin.getPoints());

                            for (Player p : playRes) {
                                try {
                                    String message;
                                    if (p.getPoints() == pWin.getPoints()) {
                                        message = "you win!\nName: " + p.getPlayer() + "\nPoints: " + p.getPoints();
                                    } else {
                                        message = "Good luck next time\nName: " + p.getPlayer() + "\nPoints: " + p.getPoints();
                                    }
                                    ObjectOutputStream newOut = new ObjectOutputStream(p.getSocket().getOutputStream());
                                    newOut.writeUTF(message);
                                    newOut.flush();
                                    newOut.close();
                                    System.out.println(message);
                                    // p.getSocket().close();
                                } catch (IOException e) {
                                    System.out.println(e);
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}