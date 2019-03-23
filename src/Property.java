import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Property {
    int id;
    int price;
    int l_size;

    boolean isForSale;
    Date saleDate;

    public Property(int id,
                    int price,
                    int l_size,
                    boolean isForSale,
                    String saleDate){
        this.id = id;
        this.price = price;
        this.l_size = l_size;
        this.isForSale = isForSale;
        if (saleDate.equals("N/A")){
            this.saleDate = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("MM/DD/YYYY");
            try {
                this.saleDate = df.parse(saleDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getL_size() {
        return l_size;
    }

    public boolean isForSale() {
        return isForSale;
    }

    public Date getSaleDate() {
        return saleDate;
    }
}
