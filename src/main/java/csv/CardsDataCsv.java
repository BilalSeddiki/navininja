package csv;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

/**
 * Classe représentant les données contenues dans le fichier CSV des chemins du réseau de transport. Pour obtenir les données sous forme de liste, où chaque élément de la liste représente une ligne dudit fichier, {@code new CardsDataCsv().readCSVFile()}.
 */
public class CardsDataCsv extends CsvData<CardsDataCsv> {

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

    @CsvBindByPosition(position = 5)
    @CsvDate(value = "m:s")
    private Date duration;

    @CsvBindByPosition(position = 6)
    private double distance;

    public String getStationA() {
        return stationA.trim();
    }

    public String getStationB() {
        return stationB.trim();
    }

    public Point2D.Double getCoordinatesA() {
        return new Point2D.Double(coordinatesA.get(0), coordinatesA.get(1));
    }

    public Point2D.Double getCoordinatesB() {
        return new Point2D.Double(coordinatesB.get(0), coordinatesB.get(1));
    }

    public String getLine() {
        return line.get(0);
    }

    public char getLineVariant() {
        return line.get(1).charAt(0);
    }

    public Duration getDuration() {
        return Duration.between(LocalTime.MIDNIGHT, duration.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return stationA + "; " + coordinatesA.get(0) + "," + coordinatesA.get(1) + "; " + stationB + "; "
                + coordinatesB.get(0) + "," + coordinatesB.get(1) + "; " + distance + "; " + duration;
    }

    @Override
    public List<CardsDataCsv> readCSVFile() throws IOException {
        return readCSVFile(Path.of("src/main/resources/map_data.csv"));
    }
}