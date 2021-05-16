package server;

import database.models.Participants;
import database.models.Users;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread{
    private final ConcurrentHashMap<Integer, Socket> socketsList;
    private final ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> socketStreamList;
    private final ConcurrentHashMap<Integer, ArrayList<Pair<Socket, Participants>>> matchesList;
    private ConcurrentHashMap<Socket, Users> usersList;


    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private boolean isAlive;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        this.socketsList = new ConcurrentHashMap<>();
        this.socketStreamList = new ConcurrentHashMap<>();
        this.matchesList = new ConcurrentHashMap<>();
        this.usersList = new ConcurrentHashMap<>();
        this.isAlive = false;
    }

    public void Stop(){
        try{
            matchesList.clear();
            socketStreamList.clear();
            socketsList.clear();
            serverSocket.close();
            isAlive = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            isAlive = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(isAlive){
            try{
                clientSocket = serverSocket.accept();
                System.out.println("Client added to server"+clientSocket.getRemoteSocketAddress());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
                new ClientHandler(clientSocket, oos, ois, socketsList, socketStreamList,matchesList,usersList).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
