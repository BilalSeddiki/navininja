package csv;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

public class ScheduleDataCsv extends CsvData<ScheduleDataCsv> {
 
    private final static Format FORMAT = new SimpleDateFormat("mm:ss");
    
    @CsvBindByPosition(position = 0)
    private char line;

    @CsvBindByPosition(position = 1)
    private String departStation;

    @CsvBindByPosition(position = 2) @CsvDate(value = "mm:ss")
    private Date departTime;

    public String getDepartStation() {
        return departStation.trim();
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
}