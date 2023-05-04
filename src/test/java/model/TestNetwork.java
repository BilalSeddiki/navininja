package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D.Double;
import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class TestNetwork {

    /**
     * Teste les paths ajoutés aux stations.
     */
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
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", "1", schedule,
            duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = Arrays.asList(pathList.get(0), pathList.get(2),
            pathList.get(6), pathList.get(8));
        var actual = network.getStation("Station 1").getInPaths();
        assertTrue(expected.size() == actual.size()
            && expected.containsAll(actual)
            && actual.containsAll(expected));

        expected = Arrays.asList(pathList.get(0), pathList.get(4));
        actual = network.getStation("Station 0").getOutPaths();
        assertTrue(expected.size() == actual.size()
        && expected.containsAll(actual)
        && actual.containsAll(expected));
    }

    /**
     * Teste le getter des variants de ligne.
     */
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
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", "1", schedule,
            duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = Arrays.asList(stationList.get(0),
            stationList.get(1), stationList.get(2));
        var actual = network.getLineVariant("Line 0", "0");
        assertEquals(expected, actual, "getting Line 0 variant 0");

        expected = Arrays.asList(stationList.get(5),
            stationList.get(0));
        actual = network.getLineVariant("Line 2", "1");
        assertEquals(expected, actual, "getting Line 2 variant 1");

        assertThrows(NoSuchElementException.class, () ->
            network.getLineVariant("Line 3", "0"));
        assertThrows(NoSuchElementException.class, () ->
            network.getLineVariant("Line 1", "2"));
    }

    /**
     * Teste le getter des lignes.
     */
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
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", "0", schedule,
            duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", "0", schedule,
            duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", "1", schedule,
            duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", "1", schedule,
            duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", "1", schedule,
            duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);

        var expected = new HashMap<String, List<Station>>();
        expected.put("0", Arrays.asList(stationList.get(3),
            stationList.get(1), stationList.get(4)));
        expected.put("1", Arrays.asList(stationList.get(4),
            stationList.get(1), stationList.get(3)));
        var actual = network.getLine("Line 1");
        assertEquals(expected, actual, "getting Line 1");

        assertThrows(NoSuchElementException.class, () ->
            network.getLine("Line 4"));
    }

    /**
     * Teste le getter des stations.
     */
    @Test
    public void testGetStation() {
        var stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(10.2, 33.6)));
        stationList.add(new Station("Station 1", new Double(6.7, 12.3)));
        stationList.add(new Station("Station 2", new Double(10.8, 8.6)));
        var network = new Network(stationList, new ArrayList<>());

        assertEquals(stationList.get(0), network.getStation("Station 0"));
        assertThrows(NoSuchElementException.class, () ->
            network.getStation("Stat 0"));
        assertEquals(stationList.get(1),
            network.getStation(new Double(6.7, 12.3)));
        assertThrows(NoSuchElementException.class, () ->
            network.getStation(new Double(6.7, 1.3)));
    }
}
