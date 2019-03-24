import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private Connection conn;

    private ClientTable clientTable;
    private AgentTable agentTable;
    private LandWithHouseTable landWithHouseTable;
    private LandWithoutHouseTable landWithoutHouseTable;
    private OfficeTable officeTable;
    private SalesTable salesTable;

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
    public void dropTable(Connection conn, String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName;
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void initializeUsers(Connection conn){
        /*        String query = "create user MANAGER password 'manager'; create user DATABASE_ADMIN password 'dbadmin';" +
                "create user CUSTOMER password ''; create user MARKETING password 'marketing';";
      */
        String query = "create role MANAGER;"
                + "create role DATABASE_ADMIN;"
                + "create role CUSTOMER;"
                + "create role MARKETING;"
                + "grant select on landWithHouse, landWithoutHouse to CUSTOMER;"
                + "grant select on landWithHouse, landWithoutHouse, sales to MARKETING;"
                + "grant select on agent, office to MANAGER;"
                + "grant alter any schema to DATABASE_ADMIN;"
                ;
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * This is hideous but i haven't slept in 2 days so cut me a break, please.
     * @param conn
     */
    public Users userInput(Connection conn){

        System.out.print("Enter your usertype (Ex: Manager, Database Administrator, Customer, Marketing): ");
        Scanner scanner = new Scanner(System.in);
        String usertype = scanner.nextLine();
        Users user = new Users(usertype.toLowerCase());

        if(user.getRole().equals("error")){
            System.out.println("Sorry, did not recognize that usertype. Exiting...");
            return null;
        }

        if(!user.getRole().equals("customer")) {
            System.out.println("Enter password associated with " + usertype);
            String pswd = scanner.next();
            if (!user.checkPassword(pswd)) {
                return null;
            }
        }
        System.out.println("Welcome, " + usertype + ". Enter QUIT at any time to exit the program. Here are the " +
                "available commands:");
        System.out.println(user.getCommands());
        return user;

    }

    public void reset(Connection conn){
        String query = "DROP ALL OBJECTS";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void initializeTables(Connection conn){
        try {
            ClientTable.createClientTable(conn);
            ClientTable.populateClientTableFromCSV(conn, "clients.csv");

            OfficeTable.createOfficeTable(conn);
            OfficeTable.populateOfficeTableFromCSV(conn,"offices.csv");

            AgentTable.createAgentTable(conn);
            AgentTable.populateAgentTableFromCSV(conn, "agents.csv");

            LandWithHouseTable.createLandWithHouseTable(conn);
            LandWithHouseTable.populateLandWithHouseTableFromCSV(conn,"landWithHouse.csv");


            LandWithoutHouseTable.createLandWithoutHouseTable(conn);
            LandWithoutHouseTable.populateLandWithoutHouseTableFromCSV(conn,"landWithoutHouse.csv");

            SalesTable.createSaleTable(conn);
            SalesTable.populateSalesTableFromCSV(conn,"sales.csv");


/*            ClientTable.printClientTable(conn);
            OfficeTable.printOfficeTable(conn);
            AgentTable.printAgentTable(conn);
            LandWithHouseTable.printLandWithHouseTable(conn);
            LandWithoutHouseTable.printLandWithoutHouseTable(conn);
            SalesTable.printSalesTable(conn);*/

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void inputLoop(Users userType){
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print(": ");
            String command = scanner.nextLine();

            if (command.equals("QUIT")){
                System.out.println("Exiting...");
                break;
            }

            userType.executeCommands(conn, command);
        }
    }

    /**
     * hideous but i haven't slept in 2 days, please cut me a break :(
     * @param args
     */
    public static void main(String[] args) {
        Main test = new Main();
        String location = "./h2demo/h2demo";
        String user = "me";
        String password = "password";

        test.createConnection(location, user, password);
        Connection conn = test.getConnection();
        test.reset(conn);

        test.initializeTables(conn);
        test.initializeUsers(conn);

        Users userType = test.userInput(conn);

        if(userType == null){             return;        }
        test.inputLoop(userType);

        test.reset(conn);
        test.closeConnection();
    }

}
