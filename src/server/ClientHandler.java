package server;

import database.models.Users;
import javafx.util.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread{

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ConcurrentHashMap<String, Socket> activeUsersList;
    private ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList;


    public ClientHandler(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, ConcurrentHashMap<String, Socket> activeUsersList, ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> activeUserStreamList) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.activeUsersList = activeUsersList;
        this.activeUserStreamList = activeUserStreamList;
    }

    @Override
    public void run() {
        Object obj = null;
        System.out.println("hele");
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
//                Socket tmpSocket = activeUsersList.get(uname);
//                System.out.println(uname+" : "+tmpSocket);
//                try {
//                    ObjectOutputStream tmpObj = activeUserStreamList.get(tmpSocket).getValue();
//                    tmpObj.writeObject(new Notification(user.getUsername(), 2 | 1));
//                    tmpObj.flush();
//                    obs.writeObject(new Notification(uname, 2 | 1));
//                    obs.flush();
//                } catch (IOException ex) {
//                }
            });
            activeUsersList.put(user.getUsername(), socket);
            activeUserStreamList.put(socket,new Pair<>(ois, oos));

            while (true) {

                try {
                    obj = ois.readObject();
                    System.out.println(obj);
//                    if (obj instanceof Notification) {
//                        Notification notice = (Notification) obj;
//                        System.out.println(notice.getUsername() + " : " + notice.getStatus());
//                    }

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
