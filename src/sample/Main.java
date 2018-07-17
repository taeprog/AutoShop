package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.DataBaseWorker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Stack;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        DataBaseWorker.init();
        DataBaseWorker.context().connect();
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Автозапчасти");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }

    public static String makeCode(String pass){
        String ret = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(pass.getBytes("UTF-8"));
            BigInteger hash = new BigInteger(1,digest.digest());
            ret = hash.toString(16);
            while (ret.length()<32) ret="0"+ret;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  ret;
    }

    public static boolean checkPassword(String password){
        String passMD = makeCode(password);
        String passFile = "";
        try {
            Scanner scanner = new Scanner(new File(System.getProperty("user.home")+ File.separator+"AutoShop"+File.separator+"config"));
            passFile= scanner.next();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(passMD+" "+passFile);
        if(passMD.equals(passFile.trim())) return true;
        else return false;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
