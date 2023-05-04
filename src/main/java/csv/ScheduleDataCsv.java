package csv;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

/**
 * Classe représentant les données contenues dans le fichier CSV
 * des horaires du réseau de transport.
 * Pour obtenir les données sous forme de liste,
 * où chaque élément de la liste représente une ligne dudit fichier,
 * {@code new ScheduleDataCsv().readCSVFile()}.
 */
public class ScheduleDataCsv extends CsvData<ScheduleDataCsv> {

    /** Index de la ligne. */
    private static final int LINE_POSITION = 0;

    /** Index de la station de départ. */
    private static final int DEPART_STATION_POSITION = 1;

    /** Index de l'heure de départ. */
    private static final int DEPART_TIME_POSITION = 2;

    /** Index de la variante. */
    private static final int VARIANT_POSITION = 3;

    /** Le numéro de la ligne. */
    @CsvBindByPosition(position = LINE_POSITION)
    private String line;

    /** Le nom de la station de départ. */
    @CsvBindByPosition(position = DEPART_STATION_POSITION)
    private String departStation;

    /** L'heure d'arrivée. */
    @CsvBindByPosition(position = DEPART_TIME_POSITION) @CsvDate(value = "H:m")
    private LocalTime departTime;

    /** Le numéro de la variante. */
    @CsvBindByPosition(position = VARIANT_POSITION)
    private String variant;

    /**
     * Renvoie le nom de la station de départ.
     * @return le nom de la station de départ
     */
    public String getDepartStation() {
        return departStation.trim();
    }

    /**
     * Renvoie le numéro de la ligne.
     * @return le numéro de la ligne
     */
    public String getLine() {
        return line;
    }

    /**
     * Renvoie l'heure d'arrivée.
     * @return l'heure d'arrivée
     */
    public LocalTime getDepartTime() {
        return departTime;
    }

    /**
     * Renvoie le numéro de la variante.
     * @return le numéro de la variante
     */
    public String getVariant() {
        return variant;
    }

    @Override
    public final String toString() {
        return line + "; " + departStation + "; " + departTime;
    }
}
