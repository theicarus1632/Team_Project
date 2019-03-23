public class Client extends People {

    public Client(int id,
                  String name,
                  String address,
                  String email,
                  String phone){
        super(id, name, address, email, phone);
    }

    public Client(String[] data){
        super(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4]);
    }

/*    public static void main(String[] args) {
        Client c = new Client(1, "s", "s", "s", "s");
        String s = c.getAddress();
        System.out.println(s);
    }*/
}
