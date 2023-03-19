package csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class ScheduleDataCsv {
 
    private final static Format FORMAT = new SimpleDateFormat("mm:ss");
    
    @CsvBindByPosition(position = 0)
    private char line;

    @CsvBindByPosition(position = 1)
    private String departStation;

    @CsvBindByPosition(position = 2) @CsvDate(value = "mm:ss")
    private Date departTime;

    public String getDepartStation() {
        return departStation;
    }

    public char getLine() {
        return line;
    }

    public Date getDepartTime() {
        return departTime;
    }

    @Override
    public String toString() {
        return line + "; " + departStation + "; " + FORMAT.format(departTime) + ";";
    }

    /**
     * 
     * @param path Le chemin vers le fichier CSV à parser
     * @return La liste d'instance de {@code ScheduleDataCsv} décrit par le fichier CSV donné en argument
     * @throws FileNotFoundException si le fichier n'existe pas
     */
    public static List<ScheduleDataCsv> readCSV(String path) throws FileNotFoundException {
        Reader reader = new FileReader(path);
        CsvToBean<ScheduleDataCsv> csvToBean = new CsvToBeanBuilder<ScheduleDataCsv>(reader).withType(ScheduleDataCsv.class).withIgnoreEmptyLine(true).withSeparator(';').build();
        return csvToBean.parse();
    }
}