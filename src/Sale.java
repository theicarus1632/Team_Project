import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sale {
    private int id;
    private Date date;
    private int price;
    private int buyerID;
    private int sellerID;
    private int officeID;
    private String s_location;

    public Sale(int id,
                String date,
                int price,
                int buyerID,
                int sellerID,
                int officeID,
                String s_location){
        this.id = id;
        this.price = price;
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.officeID = officeID;
        this.s_location = s_location;
        if (date.equals("N/A")){
            this.date = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("MM/DD/YYYY");
            try {
                this.date = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public Sale(String[] data){
        this(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]),
                Integer.parseInt(data[4]), Integer.parseInt(data[5]), data[6]+data[7]+data[8]);
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public int getOfficeID() {
        return officeID;
    }

    public String getS_location() {
        return s_location;
    }
}
