import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OfficeTable {
    public static void populateOfficeTableFromCSV(Connection conn,
                                                  String fileName) throws SQLException {

        ArrayList<Office> offices = new ArrayList<Office>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split(",");
                offices.add(new Office(split));
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        String sql = createOfficeInsertSQL(offices);

        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public static void createOfficeTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS office("
                    + "ID INT PRIMARY KEY,"
                    + "LOCATION VARCHAR(255),"
                    + "ADDRESS VARCHAR(255),"
                    + "MANAGERID INT,"
/*
                    + "foreign key (MANAGERID) references agent"
*/
                    + ");" ;
                    //TODO: fix the circular references
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void addOffice(Connection conn,
                                 int id,
                                 String location,
                                 String address,
                                 int managerID) {
        String query = String.format("INSERT INTO office "
                        + "VALUES(%d,\'%s\',\'%s\',\'%d\');",
                id, location, address, managerID);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String createOfficeInsertSQL(ArrayList<Office> offices) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO office (id, LOCATION, ADDRESS, MANAGERID) VALUES");
        for (int i = 0; i < offices.size(); i++) {
            Office o = offices.get(i);
            sb.append(String.format("(%d,\'%s\',\'%s\',\'%d\')",
                    o.getId(), o.getLocation(), o.getAddress(), o.getManagerID()));
            if (i != offices.size() - 1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }
    public static ResultSet queryOfficeTable(Connection conn,
                                             ArrayList<String> columns,
                                             ArrayList<String> whereClauses){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if(columns.isEmpty()){
            sb.append("* ");
        }
        else {
            for (int i = 0; i <columns.size() ; i++) {
                if(i != columns.size() - 1){
                    sb.append(columns.get(i) + ", ");
                }
                else{
                    sb.append(columns.get(i) + " ");
                }

            }
        }
        sb.append("FROM office ");
        if(!whereClauses.isEmpty()){
            sb.append("WHERE ");
            for(int i = 0; i < whereClauses.size(); i++){
                if(i != whereClauses.size() -1){
                    sb.append(whereClauses.get(i) + " AND ");
                }
                else{
                    sb.append(whereClauses.get(i));
                }
            }
        }
        sb.append(";");

        //Print it out to verify it made it right
        System.out.println("Query: " + sb.toString());
        try {
            /**
             * Execute the query and return the result set
             */
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printOfficeTable(Connection conn) {
        String query = "SELECT * FROM office;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while(result.next()){
                printOffice(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printOffice(int id, String location, String address, int managerID){
        System.out.printf("Office %d:\n\tLocation: %s\n\tAddress: %s\n\tManagerID: %d\n", id, location, address, managerID);
    }
}
