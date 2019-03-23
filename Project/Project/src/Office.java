public class Office {
    int id;

    String location;
    String address;

    int managerID;

    public Office(int id,
                  String location,
                  String address,
                  int managerID) {
        this.id = id;
        this.location = location;
        this.address = address;
        this.managerID = managerID;
    }

    public Office(String[] data){
        // TODO: figure out managerID
        this(Integer.parseInt(data[0]), data[1], data[2]+data[3], Integer.parseInt(data[4])/*new Agent(new String[7])*/);
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public int getManagerID() {
        return managerID;
    }
}
