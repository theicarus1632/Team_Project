import java.util.Date;

public class LandWithoutHouse extends Property {

    private String landClass;

    public LandWithoutHouse(int id,
                         int price,
                         int l_size,
                         boolean isForSale,
                         String saleDate,
                         String landClass) {
        super(id, price, l_size, isForSale, saleDate);
        this.landClass = landClass;
    }

    public LandWithoutHouse(String[] data){
        this(Integer.parseInt(data[1]), Integer.parseInt(data[2]),Integer.parseInt(data[3]),
                Boolean.parseBoolean(data[4]), data[5], data[6]);
    }

    public String getLandClass() {
        return landClass;
    }
}
