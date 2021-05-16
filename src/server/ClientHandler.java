package server;

import database.models.*;
import enums.Message_Type;
import enums.NotificationType;
import enums.Participant_State;
import javafx.util.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread{

    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private ConcurrentHashMap<Socket,Users> usersList;
    private ConcurrentHashMap<Integer, Socket> activeUsersList;
    private ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList;
    private ConcurrentHashMap<Integer, ArrayList<Pair<Socket,Participants>>> matchesList;
    private Pair<Socket,Participants> socketParticipantsPair;
    private Participants participant;
    public ClientHandler(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, ConcurrentHashMap<Integer, Socket> activeUsersList, ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList, ConcurrentHashMap<Integer, ArrayList<Pair<Socket,Participants>>> matchesList,ConcurrentHashMap<Socket,Users> usersList) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.activeUsersList = activeUsersList;
        this.activeUserStreamList = activeUserStreamList;
        this.matchesList = matchesList;
        this.usersList = usersList;
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
            usersList.put(socket,user);
            activeUsersList.put(user.getUser_id(), socket);
            activeUserStreamList.put(socket,new Pair<>(ois, oos));

            while (true) {
                try {
                    obj = ois.readObject();
                    System.out.println(obj.getClass());

                    if(obj instanceof Matches){
                        Matches match = (Matches) obj;
                        ArrayList<Pair<Socket,Participants>> player_list = new ArrayList<>();
                        matchesList.put(match.getMatch_id(),player_list);

                    }
                    else if(obj instanceof Participants){
                        Participants participants = (Participants) obj;
                        System.out.println(participants.getState());
                        if(participant == null){
                            System.out.println("Participant: "+participants.getPlayer_id()+" entered for match: "+participants.getMatch_id());
                            this.participant = participants;
                            this.socketParticipantsPair = new Pair<>(socket,participants);
                            matchesList.get(participants.getMatch_id()).add(socketParticipantsPair);

                            // sending notification to all other sockets in match that the following player has joined
                            ArrayList<Pair<Socket,Participants>> ar = matchesList.get(participants.getMatch_id());
                            for(Pair<Socket,Participants> pair:ar){
                                if(pair.getValue().getPlayer_id() != participant.getPlayer_id()){
                                    Pair<ObjectInputStream, ObjectOutputStream> pos = activeUserStreamList.get(pair.getKey());
                                    try {
                                        pos.getValue().reset();
                                        pos.getValue().flush();
                                        pos.getValue().writeObject(new SystemNotification(NotificationType.PLAYER_JOINED,usersList.get(socket).getUsername()));
                                        oos.reset();
                                        oos.flush();
                                        oos.writeObject(new SystemNotification(NotificationType.PLAYER_JOINED,usersList.get(pair.getKey()).getUsername()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            oos.reset();
                            oos.flush();
                            oos.writeObject(new SystemNotification(NotificationType.PLAYER_JOINED,usersList.get(socket).getUsername()));
                        }
                        else if(participants.getState() == Participant_State.READY){
                            System.out.println("Participant is ready: "+participants.getPlayer_id()+"; for match: "+participants.getMatch_id());
                            int index = matchesList.get(participants.getMatch_id()).indexOf(socketParticipantsPair);
                            participant.setState(participants.getState());
                            socketParticipantsPair = new Pair<>(socket,participants);
                            matchesList.get(participants.getMatch_id()).set(index,socketParticipantsPair);
                            ArrayList<Pair<Socket,Participants>> ar = matchesList.get(participants.getMatch_id());
                            boolean allReady=true;
                            for(Pair<Socket,Participants> pair:ar){
                                if(pair.getValue().getState()!=Participant_State.READY){
                                    allReady = false;
                                    break;
                                }
                            }
                            if(allReady){
                                for (Pair<Socket, Participants> sk : ar) {
                                    Pair<ObjectInputStream, ObjectOutputStream> pos = activeUserStreamList.get(sk.getKey());
                                    try {
                                        pos.getValue().writeObject(new SystemNotification(NotificationType.START_MATCH));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    else if(obj instanceof Message){
                        Message message = (Message) obj;
                        if(message.getMessage_type() == Message_Type.FOR_MATCH){
                            int match_id = message.getReceiver_id();
                            ArrayList<Pair<Socket,Participants>> ar  = matchesList.get(match_id);
                            for(Pair<Socket,Participants> pair: ar){
                                if(!usersList.get(pair.getKey()).getUsername().equals(message.getSender_username())){
                                    Pair<ObjectInputStream,ObjectOutputStream> pos = activeUserStreamList.get(pair.getKey());
                                    pos.getValue().reset();
                                    pos.getValue().flush();
                                    pos.getValue().writeObject(message);
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("unidentified instance of object");
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
