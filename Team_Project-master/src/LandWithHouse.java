import java.text.DateFormat;
import java.util.Date;

public class LandWithHouse extends Property {
    private int bedCount;
    private int bathCount;
    private int h_size;


    public LandWithHouse(int id,
                         int price,
                         int l_size,
                         boolean isForSale,
                         String saleDate,
                         String location,
                         int bedCount,
                         int bathCount,
                         int h_size){
        super(id, price, l_size, isForSale, saleDate, location);
        this.bedCount = bedCount;
        this.bathCount = bathCount;
        this.h_size = h_size;
    }
    public LandWithHouse(String[] data){
        this(Integer.parseInt(data[0]), Integer.parseInt(data[2]),Integer.parseInt(data[6]),
                Boolean.parseBoolean(data[1]), data[3], data[4]+data[5], Integer.parseInt(data[7]),
                Integer.parseInt(data[8]), Integer.parseInt(data[9]));
    }

    public int getBedCount(){
        return bedCount;
    }

    public int getBathCount() {
        return bathCount;
    }

    public int getH_size() {
        return h_size;
    }
}
