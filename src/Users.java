import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
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
                this.commands = "\tView performance by Agent ID - Enter 'Agent' <id>:\n\tView ascending or " +
                        "descending performance for all agents - Enter 'All Agents (<ascending> or <descending>)':" +
                        "\n\tView performance by Office ID - Enter 'Office' <id>:\n\tView ascending or descending" +
                        " performance for all offices - Enter 'All offices (<ascending> or <descending>)':\n\t" +
                        "View agents with a commission above, equal to, or below a number - Enter 'Commission " +
                        "(=, <=, or >=) <number> (optional: <ascending> or <descending>):\n";
                break;
            case "customer":
                this.password = "";
                this.role = "customer";
                this.commands = "\tFind properties by attributes:\nProperty attributes are:\n\t Property ID <id>, " +
                        "Owner <id>, Sale Price (=, <=, or >=) <price>, Square Footage (=, <=, or >=) <footage>, " +
                        "Bedrooms (=, <=, or >=) <bedroom number>, Bathrooms (=, <=, or >=) <bathroom number>, and " +
                        "Sale Status <true or false>.\nSpecify the attribute and attribute value, separated by a comma: " +
                        "(Ex: Bedrooms 3, Bathrooms 2)\n";
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

    /**
     * The submethods are hideous but i haven't slept in 2 days so cut me a break, please.
     */
    public void executeCommands(Connection conn, String command){
        String[] split = command.split(" ");
        String[] splitCommas = command.split(", ");
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
                customerCommands(conn, splitCommas);
                break;
            case "manager":
                managerCommands(conn, split);
                break;
            default:
                System.out.println("Unrecognized command. Please try again.");
                break;
        }
    }

    private boolean parseString(String[] split, ArrayList<String> whereClauses){
        switch(split[0].toLowerCase()){
            case "property":
                if (!split[1].toLowerCase().equals("id") || split.length != 3){
                    System.out.println("Not enough information. Include 'Property ID <id>'. Please try again.");
                    return false;
                }
                whereClauses.add("ID = \'"+ split[2] +"\'");
                return true;
            case "owner":
                if (split.length != 2){
                    System.out.println("Not enough information. Include 'Owner <id>'. Please try again.");
                    return false;
                }
                whereClauses.add("OWNERID = \'"+ split[1] +"\'");
                return true;
            case "sale":
                if (split[1].toLowerCase().equals("price") && split.length == 4){
                    whereClauses.add("PRICE " + split[2] +" \'"+ split[3] +"\'");

                }
                else if (split[1].toLowerCase().equals("status") && split.length == 3){
                    whereClauses.add("ISFORSALE = \'"+ split[2] +"\'");
                }
                else{
                    System.out.println("Not enough information. Please try again.");
                    return false;
                }
                return true;
            case "square":
                if (!split[1].toLowerCase().equals("footage") || split.length != 4){
                    System.out.println("Not enough information. Include 'Square Footage (=, >=, or <=) <footage>'. Please try again.");
                    return false;
                }
                whereClauses.add("HSIZE " + split[2] +" \'"+ split[3] +"\'");
                return true;
            case "bedrooms":
                if (split.length != 3){
                    System.out.println("Not enough information. Include 'Bedrooms (=, >=, or <=) <bedroom number>'. Please try again.");
                    return false;
                }
                whereClauses.add("BEDCOUNT " + split[1] +" \'"+ split[2] +"\'");
                return true;
            case "bathrooms":
                if (split.length != 3){
                    System.out.println("Not enough information. Include 'Bathrooms (=, >=, or <=) <bathroom number>'. Please try again.");
                    return false;
                }
                whereClauses.add("BATHCOUNT " + split[1] +" \'"+ split[2] +"\'");
                return true;
            default:
                return false;
        }
    }
    private void customerCommands(Connection conn, String[] split){
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> whereClauses = new ArrayList<String>();
        for (int i = 0; i < split.length; i++) {
            String[] splitAgain = split[i].split(" ");
            boolean valid = parseString(splitAgain, whereClauses);
            if(!valid){
                return;
            }
        }
        ResultSet result = LandWithHouseTable.queryLandWithHouseTable(conn, columns, whereClauses);
        try {
            while (result.next()) {
                LandWithHouseTable.printLandWithHouse(result.getInt(1),
                        result.getBoolean(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getString(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getInt(8),
                        result.getInt(9),
                        result.getInt(10));

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void marketingCommands(Connection conn, String[] split){
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
/*        else {
            System.out.println("Invalid command!");
        }*/
    }

    private void managerCommands(Connection conn, String[] split){
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> whereClauses = new ArrayList<String>();
        ArrayList<String> orderBy = new ArrayList<String>();

        if (split.length < 2 || split.length > 4){
            System.out.println("Invalid command!");
            return;
        }
        if (split[0].toLowerCase().equals("agent")){
            System.out.println("Performance for Agent " + split[1] + ":");
            whereClauses.add("id = \'"+ split[1] +"\'");
            ResultSet result = AgentTable.queryAgentTable(conn, columns, whereClauses, orderBy);
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
            whereClauses.add("id = \'"+ split[1] +"\'");
            ResultSet result = OfficeTable.queryOfficeTable(conn, columns, whereClauses, orderBy);
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
        if (split[0].toLowerCase().equals("all") && split.length == 3){
            if(split[2].toLowerCase().equals("ascending")){
                orderBy.add("ID asc");
            }
            if(split[2].toLowerCase().equals("descending")){
                orderBy.add("ID desc");
            }
            if (split[1].toLowerCase().equals("offices"))
            {
                ResultSet result = OfficeTable.queryOfficeTable(conn, columns, whereClauses, orderBy);
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
            if (split[1].toLowerCase().equals("agents"))
            {
                ResultSet result = AgentTable.queryAgentTable(conn, columns, whereClauses, orderBy);
                try {
                    while(result.next()){
                        AgentTable.printAgent(
                                result.getInt(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getInt(6),
                                result.getInt(7),
                                result.getInt(8),
                                result.getInt(9));
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        if(split[0].toLowerCase().equals("commission") && split.length >= 3) {
            whereClauses.add("COMMISSIONS " + split[1] +" \'"+ split[2] +"\'");
            if (split.length == 4) {
                if (split[3].toLowerCase().equals("ascending")) {
                    orderBy.add("COMMISSIONS asc");
                }
                if (split[3].toLowerCase().equals("descending")) {
                    orderBy.add("COMMISSIONS desc");
                }
            }
            ResultSet result = AgentTable.queryAgentTable(conn, columns, whereClauses, orderBy);
            try {
                while(result.next()){
                    AgentTable.printAgent(
                            result.getInt(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getInt(6),
                            result.getInt(7),
                            result.getInt(8),
                            result.getInt(9));
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
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
        switch (curr[0].toLowerCase()){
            case "sale":
                whereClauses.add("ISFORSALE = 'TRUE'");
                break;
            case "recent":
                String dateString = "";
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (month < 10){
                    dateString += "0";
                }
                dateString += String.valueOf(month);
                dateString += "/";
                if (day < 10){
                    dateString += "0";
                }
                dateString += day;
                dateString += "/";
                dateString += year%100;
                whereClauses.add("SALEDATE != 'null' AND SALEDATE LIKE '______"+ dateString.charAt(6) +
                        dateString.charAt(7) +"' AND SALEDATE >= \'"+ dateString +"\'");
                break;
            case "owner":
                if(curr.length != 2){
                    System.out.println("Invalid command!");
                    return;
                }
                whereClauses.add("OWNERID = \'"+ curr[1] +"\'");
                break;
            default:
                System.out.println("Invalid command!");
                return;
        }
        ResultSet result = LandWithHouseTable.queryLandWithHouseTable(conn, columns, whereClauses);
        try {
            while (result.next()) {
                    LandWithHouseTable.printLandWithHouse(result.getInt(1),
                            result.getBoolean(2),
                            result.getInt(3),
                            result.getString(4),
                            result.getString(5),
                            result.getInt(6),
                            result.getInt(7),
                            result.getInt(8),
                            result.getInt(9),
                            result.getInt(10));

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}