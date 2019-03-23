import java.util.ArrayList;

public class Agent extends People {
    int salary;
    int commissions;
/*
    ArrayList<Sale> commissions;
*/
    // TODO: How to represent a manager - planning to make subclass, open to
    // suggestions though.

    public Agent(int id,
                  String name,
                  String address,
                  String email,
                  String phone,
                 int salary,
                 int commissions
                 /*ArrayList<Sale> commissions*/){
        super(id, name, address, email, phone);
        this.salary = salary;
        this.commissions = commissions;
    }

    public Agent(String[] data){
        super(Integer.parseInt(data[0]), data[1], data[3]+data[4], data[5], data[6]);
        this.salary = Integer.parseInt(data[2]);
        this.commissions = Integer.parseInt(data[7]);
        /* Not sure how data for commissions may be passed in*/
        // TODO:
/*
        this.commissions = new ArrayList<>();
*/
    }

    public int getSalary() {
        return salary;
    }

    public int getCommissions() {
        return commissions;
    }
}
