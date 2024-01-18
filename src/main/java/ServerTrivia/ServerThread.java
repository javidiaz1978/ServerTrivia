package ServerTrivia;

import questionmodel.Card;
import questionmodel.Deck;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {
    Socket service;
    private Deck deck;
    private Card card;
    private ObjectOutputStream sOut;
    private ObjectInputStream sIn;
    private int correct = 0;
    private int incorrect = 0;
    private String resp;
    private String namePlayer;
    private int points = 0;
    private boolean cont = true;


    public ServerThread(Socket s, String name) {
        service = s;
        this.namePlayer = name;
    }

    public void run() {

        try {
            deck = new Deck();
            sOut = new ObjectOutputStream(service.getOutputStream());
            sIn = new ObjectInputStream(service.getInputStream());
            System.out.println("Entra en hilo");
            String line;

            while (cont) {
                card = deck.getCard();

                sOut.writeObject(card);


                System.out.println("Envía tarjeta");
                System.out.println(card.getQuestion());

                line = sIn.readUTF();
                if (line.equals("stop")) {

                    break;
                }
                System.out.println(line);

                testAnswer(line, card.getCorrectAnswer());
                sOut.writeUTF("Correct: "+correct);
                sOut.writeUTF("Incorrect: "+incorrect);


            }


        } catch (IOException e) {
            System.out.println(e);
        } /*finally {
            try {
                if (sOut != null) {
                    sOut.close();
                }

            } catch (IOException ex) {
                System.out.println(ex);
            }
            try {
                if (sIn != null) {
                    sIn.close();
                }

            } catch (IOException ex) {
                System.out.println(ex);
            }
            try {
                if (service != null) {
                    service.close();
                }

            } catch (IOException ex) {
                System.out.println(ex);
            }
        }*/
    }

    private void testAnswer(String answer, String correctAnswer) {
        if (answer.equals(correctAnswer)) {
            correct++;
            points = points + 5;
            resp = "Correct";
        } else {
            incorrect++;
            resp = "Incorrect";
            points = points - 3;
        }
        System.out.println("Correct = " + correct + ", incorrect= " + incorrect);

    }

    public Socket getService() {
        return service;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void SetNamePlayer(String name) {
        this.namePlayer = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void playerResults(String totalResult) {
        try {
            sOut.writeUTF(totalResult);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}