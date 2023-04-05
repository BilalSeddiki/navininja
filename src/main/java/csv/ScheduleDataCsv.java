package csv;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

/**
 * Classe représentant les données contenues dans le fichier CSV des horaires du réseau de transport. Pour obtenir les données sous forme de liste, où chaque élément de la liste représente une ligne dudit fichier, {@code new ScheduleDataCsv().readCSVFile()}.
 */
public class ScheduleDataCsv extends CsvData<ScheduleDataCsv> {
 
    @CsvBindByPosition(position = 0)
    private String line;

    @CsvBindByPosition(position = 1)
    private String departStation;

    @CsvBindByPosition(position = 2) @CsvDate(value = "H:m")
    private LocalTime departTime;

    public String getDepartStation() {
        return departStation.trim();
    }

    public String getLine() {
        return line;
    }

    public LocalTime getDepartTime() {
        return departTime;
    }

    @Override
    public String toString() {
        return line + "; " + departStation + "; " + departTime;
    }
    public List<ScheduleDataCsv> readCSVFile() throws IOException {
        return readCSVFile(Path.of("src/main/resources/timetables.csv"));
    }
}