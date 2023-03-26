package csv;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;

public class CardsDataCsv extends CsvData<CardsDataCsv> {

    private final static Format FORMAT = new SimpleDateFormat("mm:ss");

    @CsvBindByPosition(position = 0)
    private String stationA;
    
    @CsvBindAndSplitByPosition(position = 1, splitOn = ", ", elementType = Double.class)
    private List<Double> coordinatesA;

    @CsvBindByPosition(position = 2)
    private String stationB;
    
    @CsvBindAndSplitByPosition(position = 3, splitOn = ", ", elementType = Double.class)
    private List<Double> coordinatesB;
    
    @CsvBindAndSplitByPosition(position = 4, splitOn = " variant ", elementType = String.class)
    private List<String> line;

    @CsvBindAndSplitByPosition(position = 5, splitOn = ":", elementType = Integer.class)
    private List<Integer> duration;

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

    public String getLine() {
        return line.get(0);
    }

    public int getLineVariant() {
        return Integer.parseInt(line.get(1));
    }

    public Duration getDuration() {
        return Duration.ofSeconds(duration.get(0) * 60 + duration.get(1));
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return stationA + "; " + coordinatesA.get(0) + "," + coordinatesA.get(1) + "; " + stationB + "; " + coordinatesB.get(0) + "," + coordinatesB.get(1) + "; " + distance + "; " + FORMAT.format(duration) + ";";
    }
}