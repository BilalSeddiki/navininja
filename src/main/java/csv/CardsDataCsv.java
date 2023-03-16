package csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class CardsDataCsv {

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

    @CsvBindByPosition(position = 5) @CsvDate(value = "mm:ss")
    private Date duration;

    @CsvBindByPosition(position = 6)
    private double distance;

    public String getStationA() {
        return stationA;
    }

    public String getStationB() {
        return stationB;
    }

    public List<Double> getCoordinatesA() {
        return coordinatesA;
    }

    public List<Double> getCoordinatesB() {
        return coordinatesB;
    }

    public char getLine() {
        return line;
    }

    public Date getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public String toString() {
        return stationA + " (x = " + coordinatesA.get(0) + ", y = " + coordinatesA.get(1) + ") -> " + stationB + " (x = " + coordinatesB.get(0) + ", y = " + coordinatesB.get(1) + ") = " + distance + "km (" + FORMAT.format(duration) + ")";
    }

    /**
     * 
     * @param path Le chemin vers le fichier CSV à parser
     * @return La liste d'instance de {@code CardsDataCsv} décrit par le fichier CSV donné en argument
     * @throws FileNotFoundException si le fichier n'existe pas
     */
    public static List<CardsDataCsv> readCSV(String path) throws FileNotFoundException {
        Reader reader = new FileReader(path);
        CsvToBean<CardsDataCsv> csvToBean = new CsvToBeanBuilder<CardsDataCsv>(reader).withType(CardsDataCsv.class).withIgnoreEmptyLine(true).withSeparator(';').build();
        return csvToBean.parse();
    }
}