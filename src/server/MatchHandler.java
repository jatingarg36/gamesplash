package server;

import database.models.Participants;
import database.models.SystemNotification;
import enums.NotificationType;
import enums.Participant_State;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MatchHandler extends Thread {

    private ArrayList<Pair<Socket, Participants>> sockPartPair;
    private ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList;


    public MatchHandler(ArrayList<Pair<Socket, Participants>> sockPartPair, ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList) {
        this.sockPartPair = sockPartPair;
        this.activeUserStreamList = activeUserStreamList;
    }

    @Override
    public void run() {
        while (true) {
            boolean allReady = true;
            try {
                sleep(2000);

                for (Pair<Socket, Participants> pair : sockPartPair) {
                    System.out.println(pair.getValue().getPlayer_id() + " : player_id");
                    System.out.print(pair.getValue().getMatch_id() + " : in match");
                    if (pair.getValue().getState() != Participant_State.READY) {
                        allReady = false;
                        break;
                    }
                }
                if (allReady) {
                    for (Pair<Socket, Participants> sk : sockPartPair) {
                        Pair<ObjectInputStream, ObjectOutputStream> pos = activeUserStreamList.get(sk.getKey());
                        try {
                            pos.getValue().writeObject(new SystemNotification(NotificationType.START_MATCH));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("this match has been started");
    }
}
