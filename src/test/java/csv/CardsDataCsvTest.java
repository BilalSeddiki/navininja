package csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class CardsDataCsvTest {

    @Test
    public void readCSVTest(@TempDir Path tempDir) throws IOException {
        String csv = "Jeanne d'arc; 43.60887, 1.44544; Jean Jaurès; 43.60573, 1.44883;8 variant 6;1:42;8.43\n" +
            "Lourmel;2.2822419598550767, 48.83866086365992;Boucicaut;2.2879184311245595, 48.841024160993214;8 variant 1;4:14;15.93935780373747\n" +
            "Félix Faure;2.2918472203679703, 48.84268433479664;Commerce;2.293796842192864, 48.84461151236847;8 variant 1;3:18;12.415657251400846\n" +
            "La Motte-Picquet - Grenelle;2.298745444388579, 48.84950618174656;École Militaire;2.3064410394306907, 48.85486175512232;8 variant 1;9:16;34.97596299947988\n";
        Path file = tempDir.resolve("cardsdata_test.csv");
        Files.writeString(file, csv);
        List<CardsDataCsv> parsedData = new CardsDataCsv().readCSVFile(file);
        assertEquals(4, parsedData.size());
        assertEquals("Jeanne d'arc", parsedData.get(0).getStationA());
        assertEquals(43.60887, parsedData.get(0).getCoordinatesA().getX());
        assertEquals(1.44544, parsedData.get(0).getCoordinatesA().getY());
        assertEquals("Jean Jaurès", parsedData.get(0).getStationB());
        assertEquals(43.60573, parsedData.get(0).getCoordinatesB().getX());
        assertEquals(1.44883, parsedData.get(0).getCoordinatesB().getY());
        assertEquals("8", parsedData.get(0).getLine());
        assertEquals('6', parsedData.get(0).getLineVariant());
        assertEquals(1, parsedData.get(0).getDuration().toMinutes());
        assertEquals(42, parsedData.get(0).getDuration().toSecondsPart());
        assertEquals(8.43, parsedData.get(0).getDistance());

        assertEquals("Lourmel", parsedData.get(1).getStationA());
        assertEquals(2.2822419598550767, parsedData.get(1).getCoordinatesA().getX());
        assertEquals(48.83866086365992, parsedData.get(1).getCoordinatesA().getY());
        assertEquals("Boucicaut", parsedData.get(1).getStationB());
        assertEquals(2.2879184311245595, parsedData.get(1).getCoordinatesB().getX());
        assertEquals(48.841024160993214, parsedData.get(1).getCoordinatesB().getY());
        assertEquals("8", parsedData.get(1).getLine());
        assertEquals('1', parsedData.get(1).getLineVariant());
        assertEquals(4 * 60 + 14, parsedData.get(1).getDuration().toSeconds());
        assertEquals(15.93935780373747, parsedData.get(1).getDistance());

        assertEquals("Félix Faure", parsedData.get(2).getStationA());
        assertEquals(2.2918472203679703, parsedData.get(2).getCoordinatesA().getX());
        assertEquals(48.84268433479664, parsedData.get(2).getCoordinatesA().getY());
        assertEquals("Commerce", parsedData.get(2).getStationB());
        assertEquals(2.293796842192864, parsedData.get(2).getCoordinatesB().getX());
        assertEquals(48.84461151236847, parsedData.get(2).getCoordinatesB().getY());
        assertEquals("8", parsedData.get(2).getLine());
        assertEquals('1', parsedData.get(2).getLineVariant());
        assertEquals(3 * 60 + 18, parsedData.get(2).getDuration().toSeconds());
        assertEquals(12.415657251400846, parsedData.get(2).getDistance());

        assertEquals("La Motte-Picquet - Grenelle", parsedData.get(3).getStationA());
        assertEquals(2.298745444388579, parsedData.get(3).getCoordinatesA().getX());
        assertEquals(48.84950618174656, parsedData.get(3).getCoordinatesA().getY());
        assertEquals("École Militaire", parsedData.get(3).getStationB());
        assertEquals(2.3064410394306907, parsedData.get(3).getCoordinatesB().getX());
        assertEquals(48.85486175512232, parsedData.get(3).getCoordinatesB().getY());
        assertEquals("8", parsedData.get(3).getLine());
        assertEquals('1', parsedData.get(3).getLineVariant());
        assertEquals(9 * 60 + 16, parsedData.get(3).getDuration().toSeconds());
        assertEquals(34.97596299947988, parsedData.get(3).getDistance());
    }
}