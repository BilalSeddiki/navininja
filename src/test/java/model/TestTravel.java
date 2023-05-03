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
    
    private Network network;
    private ArrayList<Station> stationList;
    private ArrayList<Path> pathList;
    private ArrayList<Double> coordinatesList;

    /**
     * Crée un réseau ainsi que des coordonnées.
     */
    @BeforeAll
    public void createNetwork() {
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

        this.network = new Network(stationList, pathList);

        ArrayList<Double> coordinatesList = new ArrayList<Double>();
        coordinatesList.add(new Double(48.858370, 2.294532));
        coordinatesList.add(new Double(48.858841, 2.292953));
        coordinatesList.add(new Double(48.857946, 2.295365));
        coordinatesList.add(new Double(48.832994, 2.335764));
        coordinatesList.add(new Double(48.832068, 2.339215));
        this.coordinatesList = coordinatesList;
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
     * Teste le monteur de la classe Travel dans le cas où les points de départ
     * et d'arrivée sont des stations.
     */
    @Test
    public void testBuilderValidStations() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station departure = stationList.get(0);
        Station arrival = stationList.get(1);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureStation(departure)
            .setArrivalStation(arrival)
            .build();
        assertEquals(travel.getNetwork(), this.network);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), departure.getCoordinates());
        assertEquals(travel.getArrivalCoordinates(), arrival.getCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où les points de départ
     * et d'arrivée sont des coordonnées GPS.
     */
    @Test
    public void testBuilderValidCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        assertEquals(travel.getNetwork(), this.network);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinatesList.get(0));
        assertEquals(travel.getArrivalCoordinates(), coordinatesList.get(1));
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où le point de départ
     * sont des coordonnées GPS, le point d'arrivée est une station.
     * Les limites de recherche et de distance sont explicitement données.
     */
    @Test
    public void testBuilderSetSearchParameters() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station arrival = stationList.get(0);
        int searchLimit = 2;
        double searchDistance = 50.0;
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalStation(arrival)
            .setSearchLimit(searchLimit)
            .setSearchDistance(searchDistance)
            .build();
        assertEquals(travel.getNetwork(), this.network);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinatesList.get(0));
        assertEquals(travel.getArrivalCoordinates(), arrival.getCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où l'heure de départ
     * n'est pas donnée et les limites de recherche et de distance sont
     * négatives.
     * Afin d'éviter les erreurs dues à l'utilisation de la fonction now()
     * de LocalTime, l'heure attendue est considérée valide dans
     * une fourchette de 1 minute.
     */
    @Test
    public void testBuilderDefaultParameters() throws IllegalTravelException {
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = LocalTime.of(now.getHour(), now.getMinute());
        Station departure = stationList.get(0);
        int expectedSearchLimit = 1;
        double expectedSearchDistance = 10.0;
        Travel travel = new Travel
            .Builder(network)
            .setDepartureStation(departure)
            .setArrivalCoordinates(coordinatesList.get(0))
            .setSearchLimit(-1)
            .setSearchDistance(-1)
            .build();
        LocalTime departureTime = travel.getDepartureTime();
        assertEquals(travel.getNetwork(), this.network);
        assertTrue(
            departureTime.isAfter(expectedTime.minusMinutes(1)) && 
            departureTime.isBefore(expectedTime.plusMinutes(1)));
        assertEquals(travel.getArrivalCoordinates(), coordinatesList.get(0));
        assertEquals(travel.getDepartureCoordinates(), departure.getCoordinates());
        assertEquals(travel.getSearchLimit(), expectedSearchLimit);
        assertEquals(travel.getSearchDistance(), expectedSearchDistance);
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où le réseau
     * n'a pas été défini, ce qui renvoie une exception.
     */
    @Test
    public void testBuilderNullNetwork() throws IllegalTravelException {
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
     * Teste le monteur de la classe Travel dans le cas où ni 
     * le point de départ et ni le point d'arrivée n'ont été donnés.
     */
    @Test
    public void testBuilderNoStationsNorCoordinates() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(network)
                    .build();
            }
        );
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où
     * aucun point de départ n'a été donné.
     */
    @Test
    public void testBuilderNoDeparture() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(network)
                    .setArrivalCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où
     * aucun point d'arrivée n'a été donné.
     */
    @Test
    public void testBuilderNoArrival() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(network)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où
     * le point de départ est à la fois des coordonnées GPS et une station.
     */
    @Test
    public void testBuilderTwoDepartures() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(network)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .setDepartureStation(stationList.get(0))
                    .setArrivalCoordinates(coordinatesList.get(0))
                    .build();
            }
        );
    }

    /**
     * Teste le monteur de la classe Travel dans le cas où
     * le point d'arrivée est à la fois des coordonnées GPS et une station.
     */
    @Test
    public void testBuilderTwoArrivals() throws IllegalTravelException {
        assertThrows(IllegalTravelException.class, () -> {
                Travel travel = new Travel
                    .Builder(network)
                    .setDepartureCoordinates(coordinatesList.get(0))
                    .setArrivalStation(stationList.get(0))
                    .setArrivalCoordinates(coordinatesList.get(1))
                    .build();
            }
        );
    }

    /**
     * Teste la fonction createEmptyItinerary.
     */
    @Test
    public void testCreateEmptyItinerary() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(0))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        Itinerary emptyItinerary = travel.createEmptyItinerary();
        assertEquals(emptyItinerary.getDepartureTime(), departureTime);
        assertTrue(emptyItinerary.getTransports().size() == 0);
    }

    /**
     * Teste la fonction createItinerary dans le cas où le réseau
     * sur lequel rechercher le meilleur itinéraire est vide.
     */
    @Test
    public void testEmptyNetwork() throws IllegalTravelException {
        ArrayList<Path> emptyPathList = new ArrayList<Path>();
        ArrayList<Station> emptyStationList = new ArrayList<Station>();
        Network emptyNetwork = new Network(emptyStationList, emptyPathList);

        Travel travelCoordinates = new Travel
            .Builder(emptyNetwork)
            .setDepartureTime(LocalTime.of(0, 0))
            .setDepartureCoordinates(new Double(0, 0))
            .setArrivalCoordinates(new Double(0, 0))
            .build();
        Itinerary itineraryCoordinates = travelCoordinates.createItinerary();
        
        assertTrue(itineraryCoordinates.getTransports().size() == 0);
    }

    /**
     * Teste la fonction fromStationToStation.
     */
    @Test
    public void testFromStationToStation() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
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
     * Teste la fonction fromCoordinatesToStation.
     */
    @Test
    public void testFromCoordinatesToStation() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
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
     * Teste la fonction fromStationToCoordinates.
     */
    @Test
    public void testFromStationToCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
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
     * Teste la fonction fromCoordinatesToCoordinates.
     */
    @Test
    public void testFromCoordinatesToCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinatesList.get(2))
            .setArrivalCoordinates(coordinatesList.get(1))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(new Walk(coordinatesList.get(2), stationList.get(3).getCoordinates()));
        transports.add(pathList.get(4));
        transports.add(new Walk(stationList.get(1).getCoordinates(), coordinatesList.get(1)));
        Itinerary expected = new Itinerary(departureTime, transports);

        assertEquals(itinerary.getDepartureTime(), expected.getDepartureTime());
        assertEquals(itinerary.getTransports(), expected.getTransports());
    }

    /**
     * Teste la fonction fromCoordinatesToCoordinates, dans le cas
     * où l'itinéraire n'est composé que d'un trajet à pied.
     */
    @Test
    public void testFromCoordinatesToCoordinatesOnlyWalk() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
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