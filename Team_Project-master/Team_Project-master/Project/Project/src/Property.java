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
                    Date saleDate){
        this.id = id;
        this.price = price;
        this.l_size = l_size;
        this.isForSale = isForSale;
        this.saleDate = saleDate;
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
