package ServerTrivia;

import questionmodel.Card;
import questionmodel.Deck;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{
    Socket service;
    private Deck deck;
    private Card card;

    public ServerThread(Socket s, Deck deck) {
        service = s;
        this.deck=deck;
    }

    public void run() {
        DataOutputStream sOut = null;
        DataInputStream sIn = null;
        ObjectOutputStream oOut = null;

        try {
            sIn = new DataInputStream(service.getInputStream());
            sOut = new DataOutputStream(service.getOutputStream());
            oOut = new ObjectOutputStream(service.getOutputStream());

            String line;

            while (true) {
                card = deck.getCard();
                oOut.writeObject(card);
                line= sIn.readUTF();
                System.out.println(line);
                System.out.println(card.getCorrectAnswer());

                sOut.writeUTF("Recibido");

              if(line.equals("bye")){
                    break;
                }
            }


        } catch (IOException e) {
            System.out.println(e);
        } finally {
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
        }
    }
}