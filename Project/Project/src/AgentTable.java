import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AgentTable {

    /**
     * Reads a cvs file for data and adds them to the agent table
     *
     * Does not create the table. It must already be created
     *
     * @param conn: database connection to work with
     * @param fileName
     * @throws SQLException
     */
    public static void populateAgentTableFromCSV(Connection conn,
                                                  String fileName)
            throws SQLException{
        /**
         * Structure to store the data as you read it in
         * Will be used later to populate the table
         */
        ArrayList<Agent> agents = new ArrayList<Agent>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                agents.add(new Agent(split));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Creates the SQL query to do a bulk add of all people
         * that were read in. This is more efficent then adding one
         * at a time
         */
        String sql = createAgentInsertSQL(agents);

        /**
         * Create and execute an SQL statement
         *
         * execute only returns if it was successful
         */
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }
    /**
     * Create the agent table with the given attributes
     *
     * @param conn: the database connection to work with
     */
    public static void createAgentTable(Connection conn){
        try {
            String query = "CREATE TABLE IF NOT EXISTS agent("
                    + "ID INT PRIMARY KEY,"
                    + "NAME VARCHAR(255),"
                    + "PHONE VARCHAR(255),"
                    + "EMAIL VARCHAR(255),"
                    + "ADDRESS VARCHAR(255),"
                    + "SALARY INT,"
                    + "COMMISSIONS INT"
                    + ");" ;
            // TODO: figure out what commissions is - int or list?
            /**
             * Create a query and execute
             */
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a single agent to the database
     *
     * @param conn
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param address
     * @param salary
     */
    public static void addAgent(Connection conn,
                                int id,
                                String name,
                                String phone,
                                String email,
                                String address,
                                int salary,
                                int commissions){

        /**
         * SQL insert statement
         */
        String query = String.format("INSERT INTO agent " +
                        "VALUES(%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%d\',\'%d\');",
                id, name, phone, email, address, salary, commissions);
        try {
            /**
             * create and execute the query
             */
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This creates an sql statement to do a bulk add of agents
     *
     * @param agents: list of Agent objects to add
     *
     * @return
     */
    public static String createAgentInsertSQL(ArrayList<Agent> agents){
        StringBuilder sb = new StringBuilder();

        /**
         * The start of the statement,
         * tells it the table to add it to
         * the order of the data in reference
         * to the columns to add it to
         */
        sb.append("INSERT INTO agent (id, NAME, PHONE, EMAIL, ADDRESS, SALARY, COMMISSIONS) VALUES");


        for(int i = 0; i < agents.size(); i++){
            Agent a = agents.get(i);
            sb.append(String.format("(%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%d\',\'%d\')",
                   a.getId(), a.getName(), a.getPhone(), a.getEmail(), a.getAddress(), a.getSalary(), a.getCommissions()));
            if( i != agents.size()-1){
                sb.append(",");
            }
            else{
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * Makes a query to the agent table
     * with given columns and conditions
     *
     * @param conn
     * @param columns: columns to return
     * @param whereClauses: conditions to limit query by
     * @return
     */
    public static ResultSet queryAgentTable(Connection conn,
                                             ArrayList<String> columns,
                                             ArrayList<String> whereClauses){
        StringBuilder sb = new StringBuilder();

        /**
         * Start the select query
         */
        sb.append("SELECT ");

        /**
         * If we gave no columns just give them all to us
         *
         * other wise add the columns to the query
         * adding a comma top seperate
         */
        if(columns.isEmpty()){
            sb.append("* ");
        }
        else{
            for(int i = 0; i < columns.size(); i++){
                if(i != columns.size() - 1){
                    sb.append(columns.get(i) + ", ");
                }
                else{
                    sb.append(columns.get(i) + " ");
                }
            }
        }

        /**
         * Tells it which table to get the data from
         */
        sb.append("FROM agent ");

        /**
         * If we gave it conditions append them
         * place an AND between them
         */
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

        /**
         * close with semi-colon
         */
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

    /**
     * Queries and print the table
     * @param conn
     */
    public static void printAgentTable(Connection conn){
        String query = "SELECT * FROM agent;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("agent %d: %s %s %s %s %d %d\n",
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getInt(6),
                        result.getInt(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
