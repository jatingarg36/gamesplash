package database.dao;

import java.sql.Connection;
import database.models.Users;
import Helper.Encryption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

public class UserDao {

    private final Connection con;

    public UserDao(Connection con) {
        this.con = con;
    }

    public Pair<String, Users> registerUser(String username, String password) {
//        System.out.println("Starting Regsisteration");
        Users user;
        try {
            int salt = (int) (Math.random() * 10);
            String cipherPsd = new Encryption(password).performEncryption(salt);
            Timestamp timestamp = new Timestamp(new Date().getTime());
            String query = "insert into users(username,hashed_password,salt,joined_at) values ('" + username + "','" + cipherPsd + "','" + salt + "','" + timestamp + "')";
            PreparedStatement statement = con.prepareCall(query);
            statement.execute();
            user = new Users(username, cipherPsd, String.valueOf(salt), timestamp);
//            System.out.println("Registration Successfull");

        } catch (SQLException ex) {
//            System.out.println(ex.getLocalizedMessage());
            return new Pair<>("error: " + ex.getLocalizedMessage(), null);

        }
        return new Pair<>("success", user);
    }

    public Pair<String, Users> loginUser(String username, String password) {
//        System.out.println("Login Process begin");
        Users user = null;
        try {
            String query = "select * from users where username='" + username + "'";
            PreparedStatement statement = con.prepareCall(query);
            ResultSet out = statement.executeQuery();
            if (out.next()) {
                user = new Users();
                user.setId(out.getInt("user_id"));
                user.setUsername(out.getString("username"));
                user.setPassword(out.getString("hashed_password"));
                user.setSalt(out.getString("salt"));
                user.setJoined_at(out.getTimestamp("joined_at"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            return new Pair<>("error: " + ex.getLocalizedMessage(), null);
        }
        if (password.equals(new Encryption().performDecryption(user.getPassword(), Integer.parseInt(user.getSalt())))) {
//            System.out.println("Successfull Authenticated");
            return new Pair<>("success", user);
        } else {
            return new Pair<>("invalid password", null);
        }
    }

    public boolean checkUser(String username) {
//        System.out.println("Checking for user existence");
        boolean isUser = false;
        try {
            String query = "select * from users where username='" + username + "'";

            PreparedStatement stmt = con.prepareCall(query);
            ResultSet rst = stmt.executeQuery();
            if (rst.next()) {
//                System.out.println("User Already Exist");
                isUser = true;
            } else {
//                System.out.println("No Such User exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isUser;
    }

}
