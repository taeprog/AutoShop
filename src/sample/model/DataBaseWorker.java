package sample.model;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class DataBaseWorker {
    private Connection connection;
    private Statement statement;
    private static boolean connected;
    private static DataBaseWorker context;


    public static void  init() {
        context = new DataBaseWorker();
    }

    public static DataBaseWorker context() {
        return context;
    }

    private void CreateDatabase(String path) {
        System.out.println("copy base");
        try {
            Files.copy(getClass().getResourceAsStream("/assets/base.sqlite"), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        if(connected)
            return true;
        try{
            connection = null;
            Class.forName("org.sqlite.JDBC");
            File folder = new File(System.getProperty("user.home")+File.separator+"AutoShop");
            if(!folder.exists() || !folder.isDirectory())
            {
                folder.mkdir();
                this.CreateDatabase(folder.getPath()+File.separator+"base.sqlite");
                connection = DriverManager.getConnection("jdbc:sqlite:"+folder.getAbsolutePath()+File.separator+"base.sqlite");

            }
            else if(!(new File(folder.getPath()+File.separator+"base.sqlite").exists())){
                this.CreateDatabase(folder.getPath()+File.separator+"base.sqlite");
                connection = DriverManager.getConnection("jdbc:sqlite:"+folder.getAbsolutePath()+File.separator+"base.sqlite");

            }
            else connection = DriverManager.getConnection("jdbc:sqlite:"+folder.getAbsolutePath()+File.separator+"base.sqlite");
            connected = true;
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            connected= false;
        } catch (SQLException e) {
            e.printStackTrace();
            connected= false;
        }
        return connected;
    }

    public ResultSet getList(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public void execQuery(String query) throws SQLException {
        statement.execute(query);
    }
}
