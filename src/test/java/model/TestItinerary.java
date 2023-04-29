package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestItinerary {

    /* Constructeurs et getters */
    @Test
    public void testConstructorAndGetters() {
        LocalTime departureTime = LocalTime.of(0, 0);
        ArrayList<Transport> transports = new ArrayList<Transport>();

        Itinerary itinerary = new Itinerary(departureTime, transports);
        assertEquals(departureTime, itinerary.getDepartureTime(), "L'attribut departureTime est incorrect.");
        assertEquals(transports, itinerary.getTransports(), "L'attribut paths est incorrect.");
    }

    public Path helperPath(Duration duration) {
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Path path = new Path("", "0", schedule, duration, 0, source, destination);
        return path;
    }

    @Test
    public void testGetDuration() {
        LocalTime departureTime = LocalTime.of(8, 30, 00);
        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(helperPath(Duration.ofMinutes(5)));
        transports.add(helperPath(Duration.ofMinutes(10)));
        Itinerary itinerary = new Itinerary(departureTime, transports);

        Duration duration = itinerary.getDuration();
        Duration supposedDuration = Duration.ofMinutes(30);
        assertEquals(duration, supposedDuration, "Le calcul de la durée totale de l'itinéraire est incorrect.");
    }

    @Test
    public void testGetArrivalTime() {
        LocalTime departureTime = LocalTime.of(8, 30, 00);
        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(helperPath(Duration.ofMinutes(5)));
        transports.add(helperPath(Duration.ofMinutes(10)));
        Itinerary itinerary = new Itinerary(departureTime, transports);

        LocalTime arrivalTime = itinerary.getArrivalTime();
        LocalTime supposedArrivalTime = LocalTime.of(9, 00, 00);
        assertEquals(arrivalTime, supposedArrivalTime, "Le calcul de l'heure d'arrivée de l'itinéraire est incorrect.");
    }
}