package database.dao;

import database.models.Code;
import enums.Match_Status;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CodeDao {
    private Connection con;

    public CodeDao(Connection con) {
        this.con = con;
    }
    private String generateCode() {
        long time = System.currentTimeMillis();
        String code = Long.toString(time,36);
        return code;
    }
    public String generateCode(int match_id){
        String code = generateCode();
        String query = "insert into codes(match_id,code) values('"+match_id+"','"+code+"')";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            if(statement.executeUpdate() == 1){
                return code;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public Pair<Boolean, Code> validateCode(String code){
        String query = "Select * from codes where code='"+code+"'";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                Code model = new Code();
                model.setCode(code);
                model.setMatch_id(set.getInt("match_id"));
                model.setCode_id(set.getInt("code_id"));

                return new Pair<>(true,model);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Pair<>(false,null);
    }

    public boolean deleteCode(int code_id){
        String query = "delete from codes where code_id='"+code_id+"'";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            if (statement.execute()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
