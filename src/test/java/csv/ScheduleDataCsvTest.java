package csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScheduleDataCsvTest {

    @Test
    public void readCSVTest(@TempDir Path tempDir) throws IOException {
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
        Path file = tempDir.resolve("schedule_data.csv");
        Files.writeString(file, csv);
        assertTrue(Files.exists(file));
        assertEquals(csv, Files.readString(file));

        List<ScheduleDataCsv> parsedData = new ScheduleDataCsv().readCSVString(csv);

        assertEquals(17, parsedData.size());
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