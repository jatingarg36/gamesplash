package client.io_handler;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutputHandler{

    private ObjectOutputStream objectOutputStream;
    private Object object;

    public OutputHandler(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public void sendObject(Object object){
        try {
            System.out.println("sending object");
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
