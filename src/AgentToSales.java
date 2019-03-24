//this class associates an agent with a specific sale
public class AgentToSales {
    private int agentID;
    private int salesID;

    //this is the agent-sales constructor 
    public AgentToSales(int agentID, int salesID){
        this.agentID = agentID;
        this.salesID = salesID;
    }

    // returns an AgentToSales based on the data given 
    public AgentToSales(String[] data){
        this(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    public int getAgentID(){
        return agentID;
    }

    public int getSalesID() {
        return salesID;
    }
}
