package csv;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

/**
 * Classe représentant les données contenues dans le fichier CSV des chemins
 * du réseau de transport. Pour obtenir les données sous forme de liste, où
 * chaque élément de la liste représente une ligne dudit fichier,
 * {@code new CardsDataCsv().readCSVFile()}.
 */
public class CardsDataCsv extends CsvData<CardsDataCsv> {

    /** Index de la station de départ A. */
    private static final int STATION_A_POSITION = 0;

    /** Index des coordonnées de la station A. */
    private static final int COORDINATES_A_POSITION = 1;

    /** Index de la station de départ B. */
    private static final int STATION_B_POSITION = 2;

    /** Index des coordonnées de la station B. */
    private static final int COORDINATES_B_POSITION = 3;

    /** Index de la ligne et variante. */
    private static final int LINE_POSITION = 4;

    /** Index de la durée du trajet. */
    private static final int DURATION_POSITION = 5;

    /** Index de la distance entre les deux stations. */
    private static final int DISTANCE_POSITION = 6;

    /** Séparateur entre les coordonnées. */
    private static final String COORDINATES_SEPARATOR = ", ";

    /** Séparateur entre la ligne et la variante. */
    private static final String LINE_SEPARATOR = " variant ";

    /** Nom de la station de départ A. */
    @CsvBindByPosition(position = STATION_A_POSITION)
    private String stationA;

    /** Coordonnées de la station A. */
    @CsvBindAndSplitByPosition(position = COORDINATES_A_POSITION,
        splitOn = COORDINATES_SEPARATOR, elementType = Double.class)
    private List<Double> coordinatesA;

    /** Nom de la station de départ B. */
    @CsvBindByPosition(position = STATION_B_POSITION)
    private String stationB;

    /** Coordonnées de la station B. */
    @CsvBindAndSplitByPosition(position = COORDINATES_B_POSITION,
        splitOn = COORDINATES_SEPARATOR, elementType = Double.class)
    private List<Double> coordinatesB;

    /** Ligne et variante. */
    @CsvBindAndSplitByPosition(position = LINE_POSITION,
        splitOn = LINE_SEPARATOR, elementType = String.class)
    private List<String> line;

    /** Durée du trajet. */
    @CsvBindByPosition(position = DURATION_POSITION)
    @CsvDate(value = "m:s")
    private Date duration;

    /** Distance entre les deux stations. */
    @CsvBindByPosition(position = DISTANCE_POSITION)
    private double distance;

    /**
     * Renvoie le nom de la station de départ A.
     * @return le nom de la station de départ A
     */
    public String getStationA() {
        return stationA.trim();
    }

    /**
     * Renvoie le nom de la station de départ B.
     * @return le nom de la station de départ B
     */
    public String getStationB() {
        return stationB.trim();
    }

    /**
     * Renvoie les coordonnées de la station A.
     * @return les coordonnées de la station A
     */
    public Point2D.Double getCoordinatesA() {
        return new Point2D.Double(coordinatesA.get(0), coordinatesA.get(1));
    }

    /**
     * Renvoie les coordonnées de la station B.
     * @return les coordonnées de la station B
     */
    public Point2D.Double getCoordinatesB() {
        return new Point2D.Double(coordinatesB.get(0), coordinatesB.get(1));
    }

    /**
     * Renvoie le numéro de la ligne.
     * @return le numéro de la ligne
     */
    public String getLine() {
        return line.get(0);
    }

    /**
     * Renvoie le numéro de la variante.
     * @return le numéro de la variante
     */
    public String getLineVariant() {
        return line.get(1);
    }

    /**
     * Renvoie la durée du trajet.
     * @return la durée du trajet
     */
    public Duration getDuration() {
        return Duration.between(LocalTime.MIDNIGHT,
            duration.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
    }

    /**
     * Renvoie la distance entre les deux stations.
     * @return la distance entre les deux stations
     */
    public double getDistance() {
        return distance;
    }

    @Override
    public final String toString() {
        return stationA + "; " + coordinatesA.get(0) + "," + coordinatesA.get(1)
        + "; " + stationB + "; " + coordinatesB.get(0) + ","
        + coordinatesB.get(1) + "; " + distance + "; " + duration;
    }
}
