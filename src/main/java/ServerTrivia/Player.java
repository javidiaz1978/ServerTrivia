package ServerTrivia;

import java.net.Socket;
import java.util.Comparator;

public class Player implements Comparable<Player> {

    private String player;
    private int points;
    private Socket socket;

    public Player(String player, int points, Socket socket){
        this.player=player;
        this.points= points;
        this.socket=socket;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Socket getSocket(){
        return socket;
    }

    @Override
    public int compareTo(Player o) {
      /*  int resultado = 0;
        if(this.getPoints()>o.getPoints()){
            resultado =1;
        }else if(this.getPoints()<o.getPoints()){
            resultado= -1;
        }else {
            resultado= 0;
        }
        return resultado;*/
        return Integer.compare(o.getPoints(), this.getPoints());
    }
}
