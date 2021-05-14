package database.dao;

import database.models.Matches;
import enums.Difficulty;
import enums.Match_Status;

import java.sql.*;
import java.util.ArrayList;

public class MatchDao {
    private Connection connection;

    public MatchDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param player_started         -> player who created the match
     * @param difficulty        -> difficulty level {easy,medium,hard,extreme}
     * @param max_participation -> max can be 16
     * @param createdAt         -> the time of match creation
     * @return match_id
     */
    public int createMatch(int player_started, Difficulty difficulty, int max_participation, Timestamp createdAt) {
        int match_id = 0;
        String query = "insert into matches(player_started,difficulty,status,max_participation,created_at) values('" + player_started + "','" + difficulty + "','" + Match_Status.NOT_STARTED + "','" + max_participation + "','" + createdAt + "')";
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            if (set.next()) {
                match_id = set.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return match_id;
    }

    /**
     * @param match_id
     * @param startedAt
     * @return
     */
    public boolean startMatch(int match_id, Timestamp startedAt) {
        String query = "update matches set start_time='" + startedAt + "', canJoin='0',status='" + Match_Status.STARTED + "' where match_id='" + match_id + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            if (statement.execute())
                return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * It return null if match does not exists
     * otherwise returns the status of match
     *
     * @param match_id
     * @return
     */
    public Match_Status checkMatchStatus(int match_id) {
        Match_Status match_status = null;
        String query = "select status from matches where match_id='" + match_id + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                match_status = Match_Status.valueOf(set.getString("status"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return match_status;
    }

    public boolean canJoin(int match_id) {
        String query = "select canJoin from matches where match_id='" + match_id + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            if (statement.execute()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean terminateMatch(int match_id) {
        String query = "update matches set isLive='0',canJoin='0',status='" + Match_Status.TERMINATED + "' where match_id='" + match_id + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            if (statement.execute()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean endMatch(int match_id, int winner, Timestamp endedAt) {
        String query = "update matches set winner='" + winner + "',isLive='0',canJoin='0',status='" + Match_Status.ENDED + "',end_time='" + endedAt + "' where match_id='" + match_id + "'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            if (statement.execute()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ArrayList<Matches> getAllLiveMatches() {
        String query = "select * from matches where isLive='1'";
        ArrayList<Matches> matchList = new ArrayList<Matches>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Matches matches = new Matches();
                matches.setDifficulty(Difficulty.valueOf(set.getString("difficulty")));
                matches.setPlayer_started(set.getInt("player_started"));
                matches.setMatch_id(set.getInt("match_id"));
                matches.setLive(set.getBoolean("isLive"));
                matches.setCanJoin(set.getBoolean("canJoin"));
                matches.setMatch_status(Match_Status.valueOf(set.getString("status")));
                matches.setCreated_at(set.getTimestamp("created_at"));
                matches.setStart_time(set.getTimestamp("start_time"));
                matches.setEnd_time(set.getTimestamp("end_time"));
                matches.setMax_participation(set.getInt("max_participation"));
                matchList.add(matches);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matchList;
    }

    public ArrayList<Matches> getAllMatches(int player_started) {
        String query = "select * from matches where player_started='" + player_started + "'";
        ArrayList<Matches> matchList = new ArrayList<Matches>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Matches matches = new Matches();
                matches.setDifficulty(Difficulty.valueOf(set.getString("difficulty")));
                matches.setPlayer_started(set.getInt("player_started"));
                matches.setMatch_id(set.getInt("match_id"));
                matches.setLive(set.getBoolean("isLive"));
                matches.setCanJoin(set.getBoolean("canJoin"));
                matches.setMatch_status(Match_Status.valueOf(set.getString("status")));
                matches.setCreated_at(set.getTimestamp("created_at"));
                matches.setStart_time(set.getTimestamp("start_time"));
                matches.setEnd_time(set.getTimestamp("end_time"));
                matches.setMax_participation(set.getInt("max_participation"));
                matchList.add(matches);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matchList;
    }
    public Matches getMatchDetails(int match_id){
        String query = "select * from matches where match_id='" + match_id + "'";
        Matches matches = new Matches();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                matches.setDifficulty(Difficulty.valueOf(set.getString("difficulty")));
                matches.setPlayer_started(set.getInt("player_started"));
                matches.setMatch_id(set.getInt("match_id"));
                matches.setLive(set.getBoolean("isLive"));
                matches.setCanJoin(set.getBoolean("canJoin"));
                matches.setMatch_status(Match_Status.valueOf(set.getString("status")));
                matches.setCreated_at(set.getTimestamp("created_at"));
                matches.setStart_time(set.getTimestamp("start_time"));
                matches.setEnd_time(set.getTimestamp("end_time"));
                matches.setMax_participation(set.getInt("max_participation"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matches;
    }
}
