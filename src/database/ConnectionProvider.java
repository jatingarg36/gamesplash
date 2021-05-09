package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    
    private static Connection con;
    
    public static Connection getConnection(){
        try{
            if(con == null){
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gamesplash","root","");
                System.out.println("Connected to sql");
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return con;
    }
    
}
