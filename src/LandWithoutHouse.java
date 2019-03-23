import java.util.Date;

public class LandWithoutHouse extends Property {

    private String landClass;

    public LandWithoutHouse(int id,
                         int price,
                         int l_size,
                         boolean isForSale,
                         String saleDate,
                         String location,
                         String landClass) {
        super(id, price, l_size, isForSale, saleDate, location);
        this.landClass = landClass;
    }

    public LandWithoutHouse(String[] data){
        this(Integer.parseInt(data[0]), Integer.parseInt(data[1]),Integer.parseInt(data[2]),
                Boolean.parseBoolean(data[3]), data[4], data[5], data[6]);
    }

    public String getLandClass() {
        return landClass;
    }
}
