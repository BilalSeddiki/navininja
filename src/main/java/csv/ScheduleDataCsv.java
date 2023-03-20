package csv;

import java.io.StringReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

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

    public static void main(String[] args) {
        String csv = 
        "B; Borderouge; 7:00;\n" +
        "B; Borderouge; 8:00;\n" +
        "B; Borderouge; 9:00;\n" +
        "B; Borderouge; 10:00;\n" +
        "B; Borderouge; 11:00;\n" +
        "B; Borderouge; 12:00;\n" +
        "B; Borderouge; 13:00;\n" +
        "B; Borderouge; 14:00;\n" +
        "B; Borderouge; 15:00;\n" +
        "B; Borderouge; 16:00;\n" +
        "B; Borderouge; 17:00;\n" +
        "B; Borderouge; 18:00;\n" +
        "B; Borderouge; 19:00;\n" +
        "B; Borderouge; 20:00;\n" +
        "B; Borderouge; 21:00;\n" +
        "B; Borderouge; 22:00;\n" +
        "B; Borderouge; 23:00;";
        StringReader reader = new StringReader(csv);
        CsvToBean<ScheduleDataCsv> csvToBean = new CsvToBeanBuilder<ScheduleDataCsv>(new WhitespaceCSVReader(reader, ';')).withType(ScheduleDataCsv.class).withSeparator(';').withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();
        List<ScheduleDataCsv> list = csvToBean.parse();
        for (ScheduleDataCsv item : list) {
            System.out.println(item);
        }
    }
}