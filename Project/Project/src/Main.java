import java.sql.*;
import java.util.ArrayList;

public class Main {
    private Connection conn;

    /**
     * Create a database connection with the given params
     * @param location: path of where to place the database
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */
    public void createConnection(String location,
                                 String user,
                                 String password) {
        try {

            String url = "jdbc:h2:" + location;

            Class.forName("org.h2.Driver");

            //create the connection
            conn = DriverManager.getConnection(url,
                    user,
                    password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * just returns the connection
     * @return: returns class level connection
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * When your database program exits
     * you should close the connection
     */
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main test = new Main();
        String location = "./h2demo/h2demo";
        String user = "me";
        String password = "password";

        test.createConnection(location, user, password);

        try {
            ClientTable.createClientTable(test.getConnection());
            ClientTable.populateClientTableFromCSV(test.getConnection(),
                    "clients.csv");
            ClientTable.printClientTable(test.getConnection());

        } catch (SQLException e){
            e.printStackTrace();
        }


    }

}
