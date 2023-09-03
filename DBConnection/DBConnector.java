package DBConnection;
import java.sql.*;
import java.util.*;

public class DBConnector{
    static Scanner sc = new Scanner(System.in);
    static final String url = "jdbc:mysql://localhost:3306/testDb";
    static final String username = "root";
    static final String password = "rootpass@123";
  
    public static boolean tableExists(String tableName){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Query to check if the table exists
            String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, connection.getCatalog()); // Get the current database name
                preparedStatement.setString(2, tableName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    if (count > 0) {
                        System.out.println("Table exists.");
                        return true;
                    } else {
                        System.out.println("Table does not exist.");
                    }
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void dbConnect() {
        try {
            // load driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, username, password);

            if(con.isClosed()){
                System.out.println("Connection is Closed!");
            }
            else{
                System.out.println("Connection is open...");
            }
            if(!tableExists("Tasks")){
            String query = "create table Tasks(taskId int(20) primary key auto_increment, title varchar(200) not null, description varchar(400) not null, status tinyint(1) not null default 0)";
            // String query = "drop table Tasks";
            // create statement
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Tasks Table Created in Database!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void markTaskCompleted(){
        try{
            Connection con = DriverManager.getConnection(url, username, password);

            String updateQuery = "update Tasks set status=1 where taskId=?";
            System.out.print("Enter Id of the task: ");
            int id = Integer.parseInt(sc.nextLine());

            PreparedStatement pstmt = con.prepareStatement(updateQuery);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Task Id: "+id+" is marked as completed");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private static void listTasks(){
        try {
            Connection con = DriverManager.getConnection(url, username, password);

            String fetchQuery = "select * from Tasks";
            Statement stmt = con.createStatement();
            ResultSet set = stmt.executeQuery(fetchQuery);
            int count = 0;
            while(set.next()){
                count++;
                int id = set.getInt(1);
                String title = set.getString(2);
                String desc = set.getString(3);
                int status = set.getInt(4);
                System.out.println("{"+id+", "+title+", "+desc+", "+(status==0?"not completed":"completed")+"}");
            }
            if(count == 0){
                System.out.println("No todos to display!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void addTask(){
        try {
            System.out.print("Enter task title: ");
            String title = sc.nextLine();
            System.out.print("Enter task description: ");
            String desc = sc.nextLine();

            Connection con = DriverManager.getConnection(url, username, password);

            String query = "insert into Tasks(title, description) values(?,?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, desc);

            pstmt.executeUpdate();
            System.out.println("Todo Added Successfully!");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    private static void deleteTask(){
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.print("Enter task id to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            String deleteQuery = "delete from Tasks where taskId=?";
            PreparedStatement pstmt = con.prepareStatement(deleteQuery);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            System.out.println("Task id: "+id+" deleted!");
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }
    public static void start(){
        System.out.println("You can now start giving input into the console for your todos...");
        
        int choice = 1;
        System.out.println("Press 1 to add new task");
        System.out.println("Press 2 to display all tasks");
        System.out.println("Press 3 to mark task as complete");
        System.out.println("Press 4 to delete task");
        System.out.println("Press 5 to exit menu program");
        while(choice!=0){
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:  
                    addTask();
                    break;
                case 2:
                    System.out.println("List of all tasks:");
                    listTasks();
                    break;
                case 3:
                    markTaskCompleted();
                    break;
                case 4: 
                    deleteTask();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    choice = 0;
                    break;
                default:
                    System.out.println("Invalid Input!");
                    choice = 6;
                    break;
            }
        }
    }
}