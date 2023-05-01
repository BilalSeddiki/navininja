package model;

import shortestpath.*;
import model.*;
import utils.IllegalTravelException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import javafx.util.Pair;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTravel {
    
    private Dijkstra dijkstra;
    private ArrayList<Station> stationList;
    private ArrayList<Path> pathList;
    private ArrayList<Double> coordinatesList;

    /**
     * Crée un réseau, algorithme de calcul du meilleur itinéraire
     * ainsi que des coordonnées.
     */
    @BeforeAll
    public void createDijkstra() {
        ArrayList<Station> stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(48.858093, 2.294694)));
        stationList.add(new Station("Station 1", new Double(48.858203, 2.295068)));
        stationList.add(new Station("Station 2", new Double(48.857126, 2.296561)));
        stationList.add(new Station("Station 3", new Double(48.857689, 2.295063)));
        stationList.add(new Station("Station 4", new Double(48.857756, 2.294634)));
        stationList.add(new Station("Station 5", new Double(48.858029, 2.294400)));
        this.stationList = stationList;

        ArrayList<Path> pathList = new ArrayList<Path>();
        /* (Ordre) Ligne n [Variant]: Station m -> Station o */
        /* (0) L0 [0]: S0 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(0), stationList.get(1)));
        
        /* (1) L0 [0]: S1 -> S2 */
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(1), stationList.get(2)));
        
        /* (2) L0 [1]: S1 -> S0 */
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(1), stationList.get(0)));
        
        /* (3) L0 [1]: S2 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(2), stationList.get(1)));
        
        /* (4) L1 [0]: S3 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(3), stationList.get(1)));
        
        /* (5) L1 [0]: S1 -> S4 */
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(1), stationList.get(4)));
        
        /* (6) L1 [1]: S1 -> S3 */
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(1), stationList.get(3)));
        
        /* (7) L1 [1]: S4 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(4), stationList.get(1)));

        /* (8) L2 [0]: S0 -> S5 */
        pathList.add(createPathWithDefaultValues("Line 2", "0", stationList.get(0), stationList.get(5)));
        
        /* (9) L2 [1]: S5 -> S0 */
        pathList.add(createPathWithDefaultValues("Line 2", "1", stationList.get(5), stationList.get(0)));
        this.pathList = pathList;

        /* 
         * Plan du réseau: (Sn -> Station n, Ln -> Ligne n)
         * S5 <-L2-> S0 <-L0-> S1
         *                      | <-L1-> S4
         *                      | <-L1-> S3
         *                      | <-L0-> S2
         */

        Network network = new Network(stationList, pathList);
        Dijkstra dijkstra = new Dijkstra(network);
        this.dijkstra = dijkstra;

        ArrayList<Double> coordinatesList = new ArrayList<Double>();
        coordinatesList.add(new Double(48.858370, 2.294532));
        coordinatesList.add(new Double(48.858841, 2.292953));
        coordinatesList.add(new Double(48.857946, 2.295365));
        coordinatesList.add(new Double(48.832994, 2.335764));
        coordinatesList.add(new Double(48.832068, 2.339215));
        this.coordinatesList = coordinatesList;
    }

    /**
     * Crée un réseau avec deux stations et  un algorithme de calcul du 
     * meilleur itinéraire
     */
    public Pair<Dijkstra, ArrayList<Station>> createDijkstraWithTwoStations() {
        ArrayList<Station> stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(4, 4)));
        stationList.add(new Station("Station 1", new Double(8, 8)));

        ArrayList<Path> pathList = new ArrayList<Path>();
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(0), stationList.get(1)));
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(1), stationList.get(0)));

        Network network = new Network(stationList, pathList);
        Dijkstra dijkstra = new Dijkstra(network);
        return new Pair<Dijkstra, ArrayList<Station>>(dijkstra, stationList);
    }

    /**
     * Crée un chemin avec des valeurs par defaut.
     * Les valeurs par défaut sont:
     * Durée du trajet: 5 minutes.
     * Distance entre deux stations: 5 mètres.
     * Horaires de passage des trains: 8h30, 8h40, 8h50.
     * @return chemin avec des valeurs par defaut.
     */
    public Path createPathWithDefaultValues(String lineName, String variant, Station source, Station destination) {
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30));
        schedule.add(LocalTime.of(8, 40));
        schedule.add(LocalTime.of(8, 50));
        Duration duration = Duration.ofMinutes(5);
        double distance = 5.0;

        Path path = new Path(lineName, variant, schedule, duration, distance, source, destination);

        return path;
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderValidStations() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station departure = stationList.get(0);
        Station arrival = stationList.get(1);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureStation(departure)
            .setArrivalStation(arrival)
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.dijkstra);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureStation(), departure);
        assertEquals(travel.getArrivalStation(), arrival);
        assertNull(travel.getDepartureCoordinates());
        assertNull(travel.getArrivalCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderValidCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.dijkstra);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinatesList.get(0));
        assertEquals(travel.getArrivalCoordinates(), coordinatesList.get(1));
        assertNull(travel.getDepartureStation());
        assertNull(travel.getArrivalStation());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderSetSearchParameters() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station arrival = stationList.get(0);
        int searchLimit = 2;
        double searchDistance = 50.0;
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalStation(arrival)
            .setSearchLimit(searchLimit)
            .setSearchDistance(searchDistance)
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.dijkstra);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinatesList.get(0));
        assertEquals(travel.getArrivalStation(), arrival);
        assertNull(travel.getDepartureStation());
        assertNull(travel.getArrivalCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderDefaultParameters() throws IllegalTravelException {
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = LocalTime.of(now.getHour(), now.getMinute());
        Station departure = stationList.get(0);
        int expectedSearchLimit = 1;
        double expectedSearchDistance = 10.0;
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureStation(departure)
            .setArrivalCoordinates(coordinatesList.get(0))
            .setSearchLimit(-1)
            .setSearchDistance(-1)
            .build();
        LocalTime departureTime = travel.getDepartureTime();
        assertEquals(travel.getShortestPathAlgorithm(), this.dijkstra);
        assertTrue(
            departureTime.isAfter(expectedTime.minusMinutes(1)) && 
            departureTime.isBefore(expectedTime.plusMinutes(1)));
        assertEquals(travel.getDepartureStation(), departure);
        assertEquals(travel.getArrivalCoordinates(), coordinatesList.get(0));
        assertNull(travel.getDepartureCoordinates());
        assertNull(travel.getArrivalStation());
        assertEquals(travel.getSearchLimit(), expectedSearchLimit);
        assertEquals(travel.getSearchDistance(), expectedSearchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderNullAlgorithm() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(null)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .setArrivalCoordinates(coordinatesList.get(1))
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderNoStationsNorCoordinates() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(dijkstra)
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderNoDeparture() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(dijkstra)
                    .setArrivalCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderNoArrival() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(dijkstra)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderTwoDepartures() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(dijkstra)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .setDepartureStation(stationList.get(0))
                    .setArrivalCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderTwoArrivals() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(dijkstra)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .setArrivalStation(stationList.get(0))
                    .setArrivalCoordinates(coordinatesList.get(1))
                    .build();
            }
        );
    }

    /**
     * TODO
     */
    @Test
    public void testCreateEmptyItinerary() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        Itinerary emptyItinerary = travel.createEmptyItinerary();
        assertEquals(emptyItinerary.getDepartureTime(), departureTime);
        assertTrue(emptyItinerary.getTransports().size() == 0);
    }

    /**
     * TODO
     */
    @Test
    public void testEmptyNetwork() throws IllegalTravelException {
        ArrayList<Path> emptyPathList = new ArrayList<Path>();
        ArrayList<Station> emptyStationList = new ArrayList<Station>();
        Network emptyNetwork = new Network(emptyStationList, emptyPathList);
        Dijkstra emptyDijkstra = new Dijkstra(emptyNetwork);

        Travel travelCoordinates = new Travel
            .Builder(emptyDijkstra)
            .setDepartureTime(LocalTime.of(0, 0))
            .setDepartureCoordinates(new Double(0, 0))
            .setArrivalCoordinates(new Double(0, 0))
            .build();
        Itinerary itineraryCoordinates = travelCoordinates.createItinerary();

        Travel travelStation = new Travel
            .Builder(emptyDijkstra)
            .setDepartureTime(LocalTime.of(0, 0))
            .setDepartureCoordinates(new Double(0, 0))
            .setArrivalStation(new Station("Station fantome", new Double(1, 1)))
            .build();
        Itinerary itineraryStation = travelStation.createItinerary();
        
        assertTrue(itineraryCoordinates.getTransports().size() == 0);
        assertTrue(itineraryStation.getTransports().size() == 0);
    }

    /**
     * TODO
     */
    @Test
    public void testSearchFromCoordWithTwoStations() throws IllegalTravelException {
        Pair<Dijkstra, ArrayList<Station>> pair = createDijkstraWithTwoStations();
        ArrayList<Station> stationList = pair.getValue();
        Station s0 = stationList.get(0);
        Station s1 = stationList.get(1);

        LocalTime departureTime = LocalTime.of(8, 20);

        Travel travel = new Travel
            .Builder(pair.getKey())
            .setDepartureTime(LocalTime.of(0, 0))
            .setDepartureCoordinates(new Double(0, 0))
            .setArrivalCoordinates(new Double(10, 10))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(new Double(0, 0), s0.getCoordinates()));
        transports.add(createPathWithDefaultValues("Line 0", "0", s0, s1));
        transports.add(new Walk(s1.getCoordinates(), new Double(10, 10)));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testSearchFromCoordAndStationWithTwoStations() throws IllegalTravelException {
        Pair<Dijkstra, ArrayList<Station>> pair = createDijkstraWithTwoStations();
        ArrayList<Station> stationList = pair.getValue();
        Station s0 = stationList.get(0);
        Station s1 = stationList.get(1);

        LocalTime departureTime = LocalTime.of(8, 20);

        Travel travel = new Travel
            .Builder(pair.getKey())
            .setDepartureTime(LocalTime.of(0, 0))
            .setDepartureCoordinates(new Double(0, 0))
            .setArrivalStation(s1)
            .setSearchDistance(1.0)
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(new Double(0, 0), s0.getCoordinates()));
        transports.add(createPathWithDefaultValues("Line 0", "0", s0, s1));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testFromStationToStation() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureStation(stationList.get(0))
            .setArrivalStation(stationList.get(2))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(pathList.get(0));
        transports.add(pathList.get(1));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testFromCoordinatesToStation() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalStation(stationList.get(2))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(coordinatesList.get(0), stationList.get(1).getCoordinates()));
        transports.add(pathList.get(1));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testFromStationToCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureStation(stationList.get(0))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(stationList.get(0).getCoordinates(), coordinatesList.get(1)));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testFromCoordinatesToCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(2))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(coordinatesList.get(2), stationList.get(0).getCoordinates()));
        transports.add(pathList.get(8));
        transports.add(new Walk(stationList.get(5).getCoordinates(), coordinatesList.get(1)));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * TODO
     */
    @Test
    public void testFromCoordinatesToCoordinatesOnlyWalk() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(dijkstra)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(3))
            .setArrivalCoordinates(coordinatesList.get(4))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(coordinatesList.get(3), coordinatesList.get(4)));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }
}