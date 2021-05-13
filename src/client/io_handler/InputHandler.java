package client.io_handler;

import database.models.Users;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputHandler extends Thread{

    private final ObjectInputStream objectInputStream;
    private Object object;

    public InputHandler(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(2000);
                System.out.println("working");
                object = objectInputStream.readObject();

                if(object instanceof Users){
                    System.out.println(((Users) object).getUsername());
                }else {
                    System.out.println("nothing");
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
//                e.printStackTrace();
                break;
            }
        }
    }
}
