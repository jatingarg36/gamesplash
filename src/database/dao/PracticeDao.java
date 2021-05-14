package database.dao;

import enums.Difficulty;
import database.models.Practice;
import database.models.Users;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class PracticeDao {

    private final Connection connection;
    private final Users users;

    public PracticeDao(Connection connection, Users users) {
        this.connection = connection;
        this.users = users;
    }

    public int startPractice(Timestamp startedAt, Difficulty difficulty) {

        String query = "insert into self_practice(player_id,start_time,difficulty) values ('" + users.getUser_id() + "','" + startedAt + "','" + difficulty + "')";
        int practice_id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                practice_id = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return practice_id;
    }

    public boolean cancelPractice(int practice_id){
        String query = "delete from self_practice where practice_id='"+practice_id+"'";
        try{
            PreparedStatement statement = connection.prepareCall(query);
            if(statement.execute())
                return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean submitPractice(int practice_id, Timestamp endedAt, int score){
        String query = "Update self_practice set end_time='"+endedAt+"', score='"+score+"' where practice_id='"+practice_id+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            if(statement.execute()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ArrayList<Practice> getPracticeSessions(){
        ArrayList<Practice> practices = new ArrayList<>();
        String query = "Select practice_id,start_time,end_time,difficulty,score from self_practice INNER JOIN users ON self_practice.practice_id=users.user_id AND users.username='"+users.getUsername()+"'";
        try{
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet set = statement.executeQuery();
            while(set.next()){
                Practice practice = new Practice();
                practice.setPractice_id(set.getInt("practice_id"));
                practice.setPlayer_id(users.getUser_id());
                practice.setStart_time(set.getTimestamp("start_time"));
                practice.setEnd_time(set.getTimestamp("end_time"));
                practice.setDifficulty(set.getString("difficulty"));
                practice.setScore(set.getInt("score"));
                practices.add(practice);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return practices;
    }

    public ArrayList<Practice> getPracticeSessions(String username){
        ArrayList<Practice> practices = new ArrayList<>();
        String query = "Select practice_id,start_time,end_time,difficulty,score from self_practice INNER JOIN users ON self_practice.practice_id=users.user_id AND users.username='"+username+"'";
        try{
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet set = statement.executeQuery();
            while(set.next()){
                Practice practice = new Practice();
                practice.setPractice_id(set.getInt("practice_id"));
                practice.setStart_time(set.getTimestamp("start_time"));
                practice.setEnd_time(set.getTimestamp("end_time"));
                practice.setDifficulty(set.getString("difficulty"));
                practice.setScore(set.getInt("score"));
                practices.add(practice);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return practices;
    }

}
