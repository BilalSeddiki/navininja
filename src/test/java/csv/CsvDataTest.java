package csv;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D.Double;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.Network;
import model.Path;
import model.Station;

class CsvDataTest {

    @Test
    void testmakeNetwork(final @TempDir java.nio.file.Path tempDir) {
        var csvPaths =
            "Lourmel;2.2822419598550767, 48.83866086365992;Boucicaut;"
                + "2.2879184311245595, 48.841024160993214;8 variant 1;4:14;"
                + "15.93935780373747\n"
            + "Boucicaut;2.2879184311245595, 48.841024160993214;Félix Faure;"
                + "2.2918472203679703, 48.84268433479664;8 variant 1;2:58;"
                + "11.195691029379345\n"
            + "Bercy;2.3791909087742877, 48.84014763512746;Gare de Lyon;"
                + "2.372519782814122, 48.8442498880687;14 variant 1;7:8;"
                + "26.871494140096924\n"
            + "Gare de Lyon;2.372519782814122, 48.8442498880687;Châtelet;"
                + "2.346411849769497, 48.85955653272677;14 variant 1;26:45;"
                + "100.92811590723446";
        java.nio.file.Path filePath =
            tempDir.resolve("testmakeNetworkpath.csv");
        try {
            Files.writeString(filePath, csvPaths);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var csvSchedules = "8;Lourmel;12:30;1\n"
            + "8;Lourmel;17:00;1\n"
            + "8;Lourmel;18:00;1\n"
            + "14;Bercy;12:30;1\n"
            + "14;Bercy;17:00;1\n"
            + "14;Bercy;18:00;1";
        java.nio.file.Path fileSchedule =
            tempDir.resolve("testmakeNetworkschedule.csv");
        try {
            Files.writeString(fileSchedule, csvSchedules);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var stationList = new ArrayList<Station>();
        var pathList = new ArrayList<Path>();
        stationList.add(new Station(
            "Lourmel", new Double(2.2822419598550767, 48.83866086365992)));
        stationList.add(new Station(
            "Boucicaut", new Double(2.2879184311245595, 48.841024160993214)));
        pathList.add(new Path("8", "1", List.of(
            LocalTime.of(12, 30), LocalTime.of(17, 0),
            LocalTime.of(18, 0)),
            Duration.ofSeconds(60 * 4 + 14), 15.93935780373747,
            stationList.get(0), stationList.get(1)));
        stationList.add(new Station(
            "Félix Faure", new Double(2.2918472203679703, 48.84268433479664)));
        pathList.add(new Path("8", "1", new ArrayList<LocalTime>(),
            Duration.ofSeconds(2 * 60 + 58), 11.195691029379345,
            stationList.get(1), stationList.get(2)));
        stationList.add(new Station(
            "Bercy", new Double(2.3791909087742877, 48.84014763512746)));
        stationList.add(new Station(
            "Gare de Lyon", new Double(2.372519782814122, 48.8442498880687)));
        pathList.add(new Path("14", "1", List.of(
            LocalTime.of(12, 30), LocalTime.of(17, 0), LocalTime.of(18, 0)),
            Duration.ofSeconds(7 * 60 + 8), 26.871494140096924,
            stationList.get(3), stationList.get(4)));
        stationList.add(new Station(
            "Châtelet", new Double(2.346411849769497, 48.85955653272677)));
        pathList.add(new Path("14", "1", new ArrayList<LocalTime>(),
            Duration.ofSeconds(26 * 60 + 45), 100.92811590723446,
            stationList.get(4), stationList.get(5)));
        Network actual;
        try {
            actual =
                CsvData.makeNetwork(filePath.toString(),
                    fileSchedule.toString());
            var expected = new Network(stationList, pathList);
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
