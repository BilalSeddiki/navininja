package hello;

import java.io.Reader;
import java.io.StringReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class CardsData {

    private final static Format FORMAT = new SimpleDateFormat("mm:ss");

    @CsvBindByPosition(position = 0)
    private String stationA;
    
    @CsvBindAndSplitByPosition(position = 1, splitOn = ",", elementType = Double.class)
    private List<Double> coordinatesA;

    @CsvBindByPosition(position = 2)
    private String stationB;
    
    @CsvBindAndSplitByPosition(position = 3, splitOn = ",", elementType = Double.class)
    private List<Double> coordinatesB;
    
    @CsvBindByPosition(position = 4)
    private char line;

    @CsvDate(value = "mm:ss") @CsvBindByPosition(position = 5)
    private Date duration;

    @CsvBindByPosition(position = 6)
    private double distance;

    public String toString() {
        return stationA + " (x = " + coordinatesA.get(0) + ", y = " + coordinatesA.get(1) + ") -> " + stationB + " (x = " + coordinatesB.get(0) + ", y = " + coordinatesB.get(1) + ") = " + distance + "km (" + FORMAT.format(duration) + ")";
    }

    public static void main(String[] args) {
        String csv = "Jeanne d'arc; 43.60887,1.44544; Jean Jaurès; 43.60573,1.44883; B; 1:42; 8.43;";
        Reader reader = (new StringReader(csv));
        CsvToBean<CardsData> csvToBean = new CsvToBeanBuilder<CardsData>(reader).withType(CardsData.class).withIgnoreEmptyLine(true).withSeparator(';').build();
        List<CardsData> list = csvToBean.parse();
        
        for (CardsData data : list) {
            System.out.println(data);
        }
    }
}
