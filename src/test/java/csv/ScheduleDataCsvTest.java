package csv;

import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Rule;

public class ScheduleDataCsvTest {

    @Rule
    TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void readCSVTest() throws IOException {
        String csv = "B; Borderouge; 7:00;\n" +
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
        File file = tempFolder.newFile();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(csv);
        printWriter.close();
        List<ScheduleDataCsv> parsedData = ScheduleDataCsv.readCSV(file.getAbsolutePath());

        int i = 7;
        for (ScheduleDataCsv scheduleDataCsvItem : parsedData) {
            assertEquals('B', scheduleDataCsvItem.getLine());
            assertEquals("Borderouge", scheduleDataCsvItem.getDepartStation());
            assertEquals(i, scheduleDataCsvItem.getDepartTime().getMinutes());
            assertEquals(0, scheduleDataCsvItem.getDepartTime().getSeconds());
            i++;
        }
    }
}