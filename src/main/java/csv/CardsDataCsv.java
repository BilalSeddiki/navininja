package csv;

import java.io.IOException;
import java.nio.file.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import model.Station;

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
    
    @CsvBindAndSplitByPosition(position = 4, splitOn = "variant", elementType = String.class)
    private List<String> line;

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

    public String getLine() {
        return line.get(0);
    }

    public String getLineVariant() {
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

    public static void main(String[] args) throws IOException {
        List<CardsDataCsv> list = new CardsDataCsv().readCSVFile(Path.of("src/main/resources/map_data.csv"));
        HashMap<String, Station> stations = new HashMap<>();
        for (CardsDataCsv item : list) {
            String stationNameA = item.getStationA();
            double x1 = item.getCoordinatesA().get(0);
            double y1 = item.getCoordinatesA().get(1);
            Station stationA = new Station(stationNameA, new java.awt.geom.Point2D.Double(x1, y1));

            String stationNameB = item.getStationB();
            double x2 = item.getCoordinatesB().get(0);
            double y2 = item.getCoordinatesB().get(1);
            Station stationB = new Station(stationNameB, new java.awt.geom.Point2D.Double(x2, y2));

            model.Path path = new model.Path(stationNameB, null, null, y2, stationA, stationB);

            long time = item.getDuration().getTime();
            System.out.println(time);
        }
    }
}