package csv;

import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Rule;

public class CardsDataCsvTest {

    @Rule
    TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void readCSVTest() throws IOException {
        String csv = "Jeanne d'arc; 43.60887,1.44544; Jean Jaurès; 43.60573,1.44883; B; 1:42; 8.43;";
        File file = tempFolder.newFile();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(csv);
        printWriter.close();
        List<CardsDataCsv> parsedData = CardsDataCsv.readCSV(file.getAbsolutePath());

        assertEquals("Jeanne d'arc", parsedData.get(0).getStationA());
        assertEquals(43.60887, parsedData.get(0).getCoordinatesA().get(0));
        assertEquals(1.44544, parsedData.get(0).getCoordinatesA().get(1));
        assertEquals("Jean Jaurès", parsedData.get(0).getStationB());
        assertEquals(43.60573, parsedData.get(0).getCoordinatesB().get(0));
        assertEquals(1.44883, parsedData.get(0).getCoordinatesB().get(1));
        assertEquals('B', parsedData.get(0).getLine());
        assertEquals(1, parsedData.get(0).getDuration().getMinutes());
        assertEquals(42, parsedData.get(0).getDuration().getSeconds());
        assertEquals(8.43, parsedData.get(0).getDistance());
    }
}