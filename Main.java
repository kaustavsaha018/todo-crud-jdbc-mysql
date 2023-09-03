import java.util.*;
import java.io.*;
import DBConnection.*;

public class Main {
    static final String username = "kaustav";
    static final String password = "password";
    private static boolean authenticateUser(String userName, String pass){
        if(userName.equals(username) && pass.equals(password)) return true;
        return false;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to TODO List Java Application");
        System.out.print("Enter your username: ");
        String userName = sc.nextLine();
        System.out.print("Enter your password: ");
        String pass = sc.nextLine();
        boolean authUser = authenticateUser(userName, pass);
        if(!authUser){
            System.out.println("Incorrect user credentials!");
            System.exit(0);
        }
        else System.out.println("Login Successfull!");
        DBConnector db = new DBConnector();
        db.dbConnect();
        db.start();
        sc.close();
    }
}
