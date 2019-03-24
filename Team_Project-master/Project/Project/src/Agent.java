import java.util.ArrayList;

public class Agent extends People {
    int salary;
    int commissions;

    // TODO: How to represent a manager - planning to make subclass, open to
    // suggestions though.
    int managerID;

    public Agent(int id,
                  String name,
                  String address,
                  String email,
                  String phone,
                 int salary,
                 int commissions,
                 int managerID){
        super(id, name, address, email, phone);
        this.salary = salary;
        this.commissions = commissions;
        this.managerID = managerID;
    }

    public Agent(String[] data){
        this(Integer.parseInt(data[0]), data[1], data[3]+data[4],
                data[5], data[6], Integer.parseInt(data[2]),Integer.parseInt(data[7]), Integer.parseInt(data[8]));
    }

    public int getSalary() {
        return salary;
    }

    public int getCommissions() {
        return commissions;
    }

    public int getManagerID() {
        return managerID;
    }
}
