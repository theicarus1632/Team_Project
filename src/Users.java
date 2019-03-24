import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Users {
    private final String role;
    private final String password;
    private final String commands;

    public Users(String role){
        switch (role.toLowerCase()){
            case "marketing":
                this.password = "marketingpwd";
                this.role = "marketing";
                this.commands = "\tEnter 'Average' for a list of commands to find averages.\n\tEnter " +
                        "'Current' for a list of commands to find data on currently available properties.\n";
                break;
            case "manager":
                this.password = "managerpwd";
                this.role = "manager";
                this.commands = "\tView performance by Agent ID - Enter 'Agent' <id>:\n\tView performance for " +
                        "all agents - Enter 'All Agents':\n\tView performance by Office ID - Enter 'Office' <id>:\n\tView " +
                        "performance for all offices - Enter 'All offices':\n";
                break;
            case "customer":
                this.password = "";
                this.role = "customer";
                this.commands = "\tFind properties by attributes:\nProperty attributes are:\n\t Property ID, Owner, " +
                        "Sale Price, Address, Square Footage, Bedrooms, Bathrooms, and Sale Status.\nSpecify the attribute " +
                        "and attribute value: (Ex: Bedrooms 3 Bathrooms 2)\n";
                break;
            case "database administrator":
                this.password = "dbpwd";
                this.role = "database administrator";
                this.commands = "\tEnter any valid SQL command:\n";
                break;
            default:
                this.password = "";
                this.role = "error";
                this.commands = "";
                break;
        }
    }

    public String getRole() {
        return role;
    }

    public boolean checkPassword(String password){
        Boolean passes = password.equals(this.password);
        if(!passes){
            System.out.println("Sorry, did not recognize that password. Exiting...");
        }
        return passes;
    }

    public String getCommands() {
        return commands;
    }
    public void executeCommands(Connection conn, String command){
        String[] split = command.split(" ");

        switch (role){
            case "marketing":
                marketingCommands(conn, split);
                break;
            case "database administrator":
                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute(command);
                } catch (SQLException e){
                    e.printStackTrace();
                }
                break;
            case "customer":
                customerCommands(conn, split);
                break;
            case "manager":
                managerCommands(conn, split);
                break;
            default:
                System.out.println("Unrecognized command. Please try again.");
                break;
        }
    }
    public void customerCommands(Connection conn, String[] split){

    }
    public void marketingCommands(Connection conn, String[] split){
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> whereClauses = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);

        if(split.length != 1){
            System.out.println("Invalid command!");
            return;
        }
        if(split[0].toLowerCase().equals("average")){
            System.out.println("Property attributes that can be averaged are:\n\tSale Price, Square Footage, Bedrooms, " +
                    "and Bathrooms:\nEnter one of the above attributes to average for all properties.");
            String attr = scanner.nextLine();
            split = attr.split(" ");

            if(split.length == 0 || split.length > 2){
                System.out.println("Invalid command!");
                return;
            }
            getAverage(conn, columns, whereClauses, split);
        }
        if(split[0].toLowerCase().equals("current")){
            System.out.println("\tFind listings for\nHomes for Sale, Homes sold Within Month, For Sale by Owner");
            System.out.println("Enter 'Sale' for current homes, 'Recent' for recently sold homes, and 'Owner <id>' for " +
                    "info about a specific owner.");
            String attr = scanner.nextLine();
            split = attr.split(" ");

            if(split.length == 0 || split.length > 2){
                System.out.println("Invalid command!");
                return;
            }
            getCurrent(conn, columns, whereClauses, split);
        }
        else {
            System.out.println("Invalid command!");
        }
    }

    public void managerCommands(Connection conn, String[] split){
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> whereClauses = new ArrayList<String>();

        if (split.length != 2){
            System.out.println("Invalid command!");
            return;
        }
        if (split[0].toLowerCase().equals("agent")){
            System.out.println("Performance for Agent " + split[1] + ":");
            columns.add("id, NAME, PHONE, EMAIL, ADDRESS, SALARY, COMMISSIONS, MANAGERID, OFFICEID");
            whereClauses.add("id = \'"+ split[1] +"\'");
            ResultSet result = AgentTable.queryAgentTable(conn, columns, whereClauses);
            try {
                while (result.next()) {
                    AgentTable.printAgent(
                            result.getInt(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getInt(6),
                            result.getInt(7),
                            result.getInt(8),
                            result.getInt(9));                            }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (split[0].toLowerCase().equals("office")){
            columns.add("id, LOCATION, ADDRESS, MANAGERID");
            whereClauses.add("id = \'"+ split[1] +"\'");
            ResultSet result = OfficeTable.queryOfficeTable(conn, columns, whereClauses);
            try {
                while(result.next()){
                    OfficeTable.printOffice(
                            result.getInt(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4));
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (split[0].toLowerCase().equals("all")){
            if (split[1].toLowerCase().equals("offices"))
            {
                OfficeTable.printOfficeTable(conn);
            }
            if (split[1].toLowerCase().equals("agents"))
            {
                AgentTable.printAgentTable(conn);
            }
        }
        else {
            System.out.println("Invalid command!");
        }
    }

    private void getAverage(Connection conn, ArrayList<String> columns, ArrayList<String> whereClauses, String[] avgOf){
        String toPrint = avgOf[0];
        String attr;
        switch (avgOf[0].toLowerCase()){
            case "bathrooms":
                attr = "BATHCOUNT";
                break;
            case "bedrooms":
                attr = "BEDCOUNT";
                break;
            case "square":
                if(!avgOf[1].toLowerCase().equals("footage")){
                    System.out.println("Invalid command!");
                    return;
                }
                toPrint += " " + avgOf[1];
                attr = "HSIZE";
                break;
            case "sale":
                if(!avgOf[1].toLowerCase().equals("price")){
                    System.out.println("Invalid command!");
                    return;
                }
                toPrint += " " + avgOf[1];
                attr = "PRICE";
                break;
            default:
                System.out.println("Invalid command!");
                return;
        }
        columns.add("avg(" + attr + ")");
        ResultSet result = LandWithHouseTable.queryLandWithHouseTable(conn, columns, whereClauses);
        try {
            while (result.next()) {
                System.out.printf("Average " + toPrint + " (rounded to the nearest integer): %d\n",
                        result.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void getCurrent(Connection conn, ArrayList<String> columns, ArrayList<String> whereClauses, String[] curr){
        String toPrint = curr[0];
        String attr;
        switch (curr[0].toLowerCase()){
            case "sale":
                attr = "ISFORSALE";
                break;
            case "recent":
                attr = "SALEDATE";
                break;
            case "owner":
                if(curr.length != 2){
                    System.out.println("Invalid command!");
                    return;
                }
                toPrint += " " + curr[1];
                attr = "id";
                break;
            default:
                System.out.println("Invalid command!");
                return;
        }
        //TODO:
        columns.add("avg(" + attr + ")");
        ResultSet result = LandWithHouseTable.queryLandWithHouseTable(conn, columns, whereClauses);
        try {
            while (result.next()) {
                System.out.printf("Average " + toPrint + " (rounded to the nearest integer): %d\n",
                        result.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}