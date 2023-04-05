package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.io.TempDir;

import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class TestNetwork {

    @Test
    public void testPathsAddedToStations() {
        var stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(0, 0)));
        stationList.add(new Station("Station 1", new Double(0, 0)));
        stationList.add(new Station("Station 2", new Double(0, 0)));
        stationList.add(new Station("Station 3", new Double(0, 0)));
        stationList.add(new Station("Station 4", new Double(0, 0)));
        stationList.add(new Station("Station 5", new Double(0, 0)));

        var schedule = new ArrayList<LocalTime>();
        var duration = Duration.ofMinutes(5);
        var distance = 5;

        var pathList = new ArrayList<Path>();
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", '0', schedule, duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", '1', schedule, duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = Arrays.asList(pathList.get(0), pathList.get(2), pathList.get(6), pathList.get(8));
        var actual = network.getStation("Station 1").getInPaths();
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected));

        expected = Arrays.asList(pathList.get(0), pathList.get(4));
        actual = network.getStation("Station 0").getOutPaths();
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testGetLineVariant() {
        var stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(0, 0)));
        stationList.add(new Station("Station 1", new Double(0, 0)));
        stationList.add(new Station("Station 2", new Double(0, 0)));
        stationList.add(new Station("Station 3", new Double(0, 0)));
        stationList.add(new Station("Station 4", new Double(0, 0)));
        stationList.add(new Station("Station 5", new Double(0, 0)));

        var schedule = new ArrayList<LocalTime>();
        var duration = Duration.ofMinutes(5);
        var distance = 5;

        var pathList = new ArrayList<Path>();
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", '0', schedule, duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", '1', schedule, duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = Arrays.asList(stationList.get(0), stationList.get(1), stationList.get(2));
        var actual = network.getLineVariant("Line 0", '0');
        assertEquals(expected, actual, "getting Line 0 variant 0");

        expected = Arrays.asList(stationList.get(5), stationList.get(0));
        actual = network.getLineVariant("Line 2", '1');
        assertEquals(expected, actual, "getting Line 2 variant 1");

        assertThrows(NoSuchElementException.class, () -> network.getLineVariant("Line 3", '0'));
        assertThrows(NoSuchElementException.class, () -> network.getLineVariant("Line 1", '2'));
    }

    @Test
    public void testGetLine() {
        var stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(0, 0)));
        stationList.add(new Station("Station 1", new Double(0, 0)));
        stationList.add(new Station("Station 2", new Double(0, 0)));
        stationList.add(new Station("Station 3", new Double(0, 0)));
        stationList.add(new Station("Station 4", new Double(0, 0)));
        stationList.add(new Station("Station 5", new Double(0, 0)));

        var schedule = new ArrayList<LocalTime>();
        var duration = Duration.ofMinutes(5);
        var distance = 5;

        var pathList = new ArrayList<Path>();
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", '0', schedule, duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", '0', schedule, duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", '0', schedule, duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", '1', schedule, duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", '1', schedule, duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", '1', schedule, duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = new HashMap<Character, List<Station>>();
        expected.put('0', Arrays.asList(stationList.get(3), stationList.get(1), stationList.get(4)));
        expected.put('1', Arrays.asList(stationList.get(4), stationList.get(1), stationList.get(3)));
        var actual = network.getLine("Line 1");
        assertEquals(expected, actual, "getting Line 1");

        assertThrows(NoSuchElementException.class, () -> network.getLine("Line 4"));
    }

    @Test
    public void testGetStation() {
        var stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(10.2, 33.6)));
        stationList.add(new Station("Station 1", new Double(6.7, 12.3)));
        stationList.add(new Station("Station 2", new Double(10.8, 8.6)));
        var network = new Network(stationList, new ArrayList<>());

        assertEquals(stationList.get(0), network.getStation("Station 0"));
        assertThrows(NoSuchElementException.class, () -> network.getStation("Stat 0"));
        assertEquals(stationList.get(1), network.getStation(new Double(6.7, 12.3)));
        assertThrows(NoSuchElementException.class, () -> network.getStation(new Double(6.7, 1.3)));
    }

    @Test
    public void testFromCSV(@TempDir java.nio.file.Path tempDir) {
        var csv = "Lourmel;2.2822419598550767, 48.83866086365992;Boucicaut;2.2879184311245595, 48.841024160993214;8 variant 1;4:14;15.93935780373747\n"
                +
                "Boucicaut;2.2879184311245595, 48.841024160993214;Félix Faure;2.2918472203679703, 48.84268433479664;8 variant 1;2:58;11.195691029379345\n"
                +
                "Bercy;2.3791909087742877, 48.84014763512746;Gare de Lyon;2.372519782814122, 48.8442498880687;14 variant 1;7:8;26.871494140096924\n"
                +
                "Gare de Lyon;2.372519782814122, 48.8442498880687;Châtelet;2.346411849769497, 48.85955653272677;14 variant 1;26:45;100.92811590723446";
        java.nio.file.Path file = tempDir.resolve("testfromcsv.csv");
        try {
            Files.writeString(file, csv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var stationList = new ArrayList<Station>();
        var pathList = new ArrayList<Path>();
        stationList.add(new Station("Lourmel", new Double(2.2822419598550767, 48.83866086365992)));
        stationList.add(new Station("Boucicaut", new Double(2.2879184311245595, 48.841024160993214)));
        pathList.add(new Path("8", '1', new ArrayList<LocalTime>(), Duration.ofSeconds(60 * 4 + 14), 15.93935780373747,
                stationList.get(0), stationList.get(1)));
        stationList.add(new Station("Félix Faure", new Double(2.2918472203679703, 48.84268433479664)));
        pathList.add(new Path("8", '1', new ArrayList<LocalTime>(), Duration.ofSeconds(2 * 60 + 58), 11.195691029379345,
                stationList.get(1), stationList.get(2)));
        stationList.add(new Station("Bercy", new Double(2.3791909087742877, 48.84014763512746)));
        stationList.add(new Station("Gare de Lyon", new Double(2.372519782814122, 48.8442498880687)));
        pathList.add(new Path("14", '1', new ArrayList<LocalTime>(), Duration.ofSeconds(7 * 60 + 8), 26.871494140096924,
                stationList.get(3), stationList.get(4)));
        stationList.add(new Station("Châtelet", new Double(2.346411849769497, 48.85955653272677)));
        pathList.add(new Path("14", '1', new ArrayList<LocalTime>(), Duration.ofSeconds(26 * 60 + 45),
                100.92811590723446, stationList.get(4), stationList.get(5)));
        Network actual;
        try {
            actual = Network.fromCSV(file.toString(), "");
            assertEquals(new Network(stationList, pathList), actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
