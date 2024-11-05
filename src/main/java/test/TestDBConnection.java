package test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static mysql_connection.MySqlConnection.getOracleConnection;


public class TestDBConnection {

    public static void main(String[] args)throws SQLException {

        String selectTableSQL = "SELECT * FROM test";

        Statement statement = null;

        try{
            Connection connection= getOracleConnection();

            statement = connection.createStatement();


            //get data from db

            ResultSet rs = statement.executeQuery(selectTableSQL);



            //fetch data

            while(rs.next()){
                String field = rs.getString("nom");

                System.out.println("field : "+field);
            }
            rs.close();




        }
        catch (SQLException e){
            System.out.println(e);
            System.out.println("1000000 dawa7");
        }

    }

}


