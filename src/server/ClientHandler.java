package server;

import database.models.Matches;
import database.models.Participants;
import database.models.Users;
import javafx.util.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread{

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ConcurrentHashMap<String, Socket> activeUsersList;
    private ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList;
    private ConcurrentHashMap<Matches, ArrayList<Socket>> matchesList;

    public ClientHandler(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, ConcurrentHashMap<String, Socket> activeUsersList, ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList, ConcurrentHashMap<Matches, ArrayList<Socket>> matchesList) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.activeUsersList = activeUsersList;
        this.activeUserStreamList = activeUserStreamList;
        this.matchesList = matchesList;
    }

    @Override
    public void run() {
        Object obj = null;
        try {
            obj = ois.readObject();
            System.out.println(obj);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage() + ":user obj read error");
        }

        if (obj instanceof Users) {
            Users user = (Users) obj;
            System.out.println(user.getUsername());
            activeUsersList.keySet().forEach((uname) -> {
                System.out.println("> " + uname);
            });
            activeUsersList.put(user.getUsername(), socket);
            activeUserStreamList.put(socket,new Pair<>(ois, oos));

            while (true) {

                try {
                    obj = ois.readObject();
                    System.out.println(obj.getClass());

                    if(obj instanceof Matches){
                        Matches match = (Matches) obj;
                        ArrayList<Socket> player_list = new ArrayList<>();
                        player_list.add(socket);
                        matchesList.put(match,player_list);
                    }
                    else if(obj instanceof Participants){
                        Participants participants = (Participants) obj;
                        for (Matches matches : matchesList.keySet()) {
                            if (matches.getMatch_id() == participants.getMatch_id()) {
                                System.out.println(participants.getPlayer_id()+" participant add to "+matches.getMatch_id());
                                matchesList.get(matches).add(socket);
                                break;
                            }
                        }
                    }
                    else{

                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Notification execption " + ex.getLocalizedMessage());
                    break;
                }
            }

        }
        else{
            System.out.println(obj);
        }
    }
}
