package client;

import java.io.IOException;
import java.net.Socket;

public class Client{
    private final int port;
    private final String address;
    private Socket clientSocket;
    public Client(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public Client(int port) throws IOException {
        this.port = port;
        this.address = "localhost";
        this.clientSocket = new Socket(address,port);
    }

    public Socket getSocket(){
        System.out.println(clientSocket.getLocalAddress());
        return clientSocket;
    }
}
