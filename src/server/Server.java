package server;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread{
    private ConcurrentHashMap<String, Socket> socketsList;
    private ConcurrentHashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> socketStreamList;

    private ObjectOutputStream obs;
    private ObjectInputStream ois;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private boolean isAlive;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        this.socketsList = new ConcurrentHashMap<>();
        this.socketStreamList = new ConcurrentHashMap<>();
        this.isAlive = false;
    }

    public void Stop(){
        try{
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
                obs = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
                socketStreamList.put(clientSocket,new Pair<>(ois,obs));
                new ClientHandler(clientSocket, obs, ois, socketsList, socketStreamList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
