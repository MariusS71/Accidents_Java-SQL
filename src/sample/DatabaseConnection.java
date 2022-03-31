package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
//DESKTOP-KKPJO92
        String databaseUser="sa";
        String databasePassword="0765694805";
        String url = "jdbc:jtds:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=Accidente; allowMultipleQueries=true";


        try{
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }

}
