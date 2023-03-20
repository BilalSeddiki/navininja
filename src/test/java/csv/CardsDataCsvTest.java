package csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CardsDataCsvTest {

    @Test
    public void readCSVTest(@TempDir Path tempDir) throws IOException {
        String csv = "Jeanne d'arc; 43.60887,1.44544; Jean Jaurès; 43.60573,1.44883; B; 1:42; 8.43;";
        Path file = tempDir.resolve("cardsdata_test.csv");

        Files.writeString(file, csv);
        List<CardsDataCsv> parsedData = new CardsDataCsv().readCSVFile(file);

        assertEquals(1, parsedData.size());
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