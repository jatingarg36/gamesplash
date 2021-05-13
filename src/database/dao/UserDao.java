package database.dao;

import java.sql.Connection;

import database.models.Users;
import helper.Encryption;

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

    enum status {
        available,
        busy
    }

    public UserDao(Connection con) {
        this.con = con;
    }

    public Pair<String, Users> registerUser(String username, String password) {
        /* System.out.println("Starting Registration"); */
        Users user = null;
        try {
            int salt = (int) (Math.random() * 10);
            String cipherPsd = new Encryption().performEncryption(password, salt);
            Timestamp timestamp = new Timestamp(new Date().getTime());
            String query = "insert into users(username,hashed_password,salt,isActive,joined_at,last_visited) values ('" + username + "','" + cipherPsd + "','" + salt + "','" + 0 + "','" + timestamp + "','" + timestamp + "')";
            PreparedStatement statement = con.prepareCall(query);
            statement.execute();

            /*-- getting user info --*/
            query = "select * from users where username='" + username + "'";
            statement = con.prepareCall(query);
            ResultSet out = statement.executeQuery();
            if (out.next()) {
                user = new Users();
                user.setUser_id(out.getInt("user_id"));
                user.setUsername(out.getString("username"));
                user.setPassword(out.getString("hashed_password"));
                user.setSalt(out.getString("salt"));
                user.setActive(out.getBoolean("isActive"));
                user.setStatus(out.getString("status"));
                user.setScore(out.getInt("score"));
                user.setRank(out.getInt("rank"));
                user.setJoined_at(out.getTimestamp("joined_at"));
                user.setBadge(out.getString("badge"));
                user.setLast_visited(out.getTimestamp("last_visited"));
            }

//            System.out.println("Registration Successfull");

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());

            return new Pair<>("error: " + ex.getLocalizedMessage(), null);

        }
        return new Pair<>("success", user);
    }

    public Pair<String, Users> loginUser(String username, String password) {
//        System.out.println("Login Process begin");
        Users user = null;
        try {
            String query = "select * from users where username='" + username + "'";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet out = statement.executeQuery();
            if (out.next()) {
                user = new Users();
                user.setUser_id(out.getInt("user_id"));
                user.setUsername(out.getString("username"));
                user.setPassword(out.getString("hashed_password"));
                user.setSalt(out.getString("salt"));
                user.setActive(out.getBoolean("isActive"));
                user.setStatus(out.getString("status"));
                user.setScore(out.getInt("score"));
                user.setRank(out.getInt("rank"));
                user.setJoined_at(out.getTimestamp("joined_at"));
                user.setBadge(out.getString("badge"));
                user.setLast_visited(out.getTimestamp("last_visited"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            return new Pair<>("error: " + ex.getLocalizedMessage(), null);
        }
        if (password.equals(new Encryption().performDecryption(user.getPassword(), Integer.parseInt(user.getSalt())))) {
//            System.out.println("Successfull Authenticated");
            if(setUserOnline(username))
                user.setActive(true);
            return new Pair<>("success", user);
        } else {
            return new Pair<>("invalid password", null);
        }
    }

    private boolean setUserOnline(String username) {
        String query = "Update users Set isActive='1', status='1' where username='"+username+"'";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            if(statement.execute())
                return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean setUserStatus(int uid,int status){
        String query = "update users set status='"+status+"' where user_id='"+uid+"'";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            if(statement.execute()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
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

    public void logout(Users users){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query = "Update users Set isActive='0',last_visited='"+timestamp+"' where username='"+users.getUsername()+"'";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            statement.execute();
            System.out.println("logout performed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
