//this class is a representation of the client in the database 
public class Client extends People {

    private int referralNo;

    //this is the client object constructor 
    public Client(int id,
                  String name,
                  String address,
                  String email,
                  String phone,
                  int referralNo){
        super(id, name, address, email, phone);
        this.referralNo = referralNo;
    }

    //this method returns a client based on the data given 
    public Client(String[] data){
        this(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], Integer.parseInt(data[6]));
    }

    public int getReferralNo() {
        return referralNo;
    }

    //TODO: 
    /*    public static void main(String[] args) {
        Client c = new Client(1, "s", "s", "s", "s");
        String s = c.getAddress();
        System.out.println(s);
    }*/
}
