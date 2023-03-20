package csv;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

public class CardsDataCsv extends CsvData<CardsDataCsv> {

    private final static Format FORMAT = new SimpleDateFormat("mm:ss");

    @CsvBindByPosition(position = 0)
    private String stationA;
    
    @CsvBindAndSplitByPosition(position = 1, splitOn = ",", elementType = Double.class)
    private List<Double> coordinatesA;

    @CsvBindByPosition(position = 2)
    private String stationB;
    
    @CsvBindAndSplitByPosition(position = 3, splitOn = ",", elementType = Double.class)
    private List<Double> coordinatesB;
    
    @CsvBindAndSplitByPosition(position = 4, splitOn = "variant", elementType = Integer.class)
    private List<Integer> line;

    @CsvBindByPosition(position = 5) @CsvDate(value = "mm:ss")
    private Date duration;

    @CsvBindByPosition(position = 6)
    private double distance;

    public String getStationA() {
        return stationA.trim();
    }

    public String getStationB() {
        return stationB.trim();
    }

    public List<Double> getCoordinatesA() {
        return coordinatesA;
    }

    public List<Double> getCoordinatesB() {
        return coordinatesB;
    }

    public int getLine() {
        return line.get(0);
    }

    public int getLineVariant() {
        return line.get(1);
    }

    public Date getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return stationA + "; " + coordinatesA.get(0) + "," + coordinatesA.get(1) + "; " + stationB + "; " + coordinatesB.get(0) + "," + coordinatesB.get(1) + "; " + distance + "; " + FORMAT.format(duration) + ";";
    }
}