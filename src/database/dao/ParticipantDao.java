package database.dao;

import database.models.Participants;
import enums.Participant_State;

import java.sql.*;

public class ParticipantDao {
    private Connection connection;

    public ParticipantDao(Connection connection) {
        this.connection = connection;
    }

    public Participants participate(int match_id, int player_id){
        Participants participants = new Participants();
        String query = "insert into participants(match_id,player_id) values ('"+match_id+"','"+player_id+"')";
        try{
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            if(set.next()){
                participants.setParticipation_id(set.getInt(1));
                participants.setDuration(new Time(0));
                participants.setHasLeft(false);
                participants.setMatch_id(match_id);
                participants.setPlayer_id(player_id);
                participants.setState(Participant_State.NOT_READY);
                participants.setScore(0);
                participants.setRank(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return participants;
    }
    public boolean setState(int participation_id, Participant_State state){
        String query = "Update participants set state='"+state+"' where participation_id='"+participation_id+"'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            if( statement.executeUpdate()==1)
                return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
