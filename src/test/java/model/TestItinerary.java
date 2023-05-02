package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestItinerary {

    /**
     * Cree un chemin avec des valeurs par defaut.
     * @return chemin avec des valeurs par defaut.
     */
    public Path createPathWithDefaultValues(Duration duration) {
        Station source = new Station("Station source", new Double(0, 0));
        Station destination = new Station("Station destination", new Double(0, 0));
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Path path = new Path("", "0", schedule, duration, 0, source, destination);
        return path;
    }

    /**
     * Cree un itinéraire avec des chemins.
     * @return itinéraire avec des chemins.
     */
    public Itinerary createItinerary() {
        LocalTime departureTime = LocalTime.of(8, 30, 00);
        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(createPathWithDefaultValues(Duration.ofMinutes(5)));
        transports.add(createPathWithDefaultValues(Duration.ofMinutes(10)));
        Itinerary itinerary = new Itinerary(departureTime, transports);
        return itinerary;
    }

    /**
     * Teste le constructeur ainsi que les getters de la classe Itinerary.
     */
    @Test
    public void testConstructorAndGetters() {
        LocalTime departureTime = LocalTime.of(0, 0);
        ArrayList<Transport> transports = new ArrayList<Transport>();

        Itinerary itinerary = new Itinerary(departureTime, transports);
        assertEquals(departureTime, itinerary.getDepartureTime(), "L'attribut departureTime est incorrect.");
        assertEquals(transports, itinerary.getTransports(), "L'attribut paths est incorrect.");
    }

    /**
     * Teste la fonction getDuration avec un itinéraire non vide.
     */
    @Test
    public void testGetDuration() {
        Itinerary itinerary = createItinerary();

        Duration duration = itinerary.getDuration();
        Duration supposedDuration = Duration.ofMinutes(35);
        assertEquals(duration, supposedDuration, 
            "La durée totale de cet itinéraire devrait être 30 minutes.");
    }

    /**
     * Teste la fonction getArrivalTime avec un itinéraire non vide.
     */
    @Test
    public void testGetArrivalTime() {
        Itinerary itinerary = createItinerary();

        LocalTime arrivalTime = itinerary.getArrivalTime();
        LocalTime supposedArrivalTime = LocalTime.of(9, 05, 00);
        assertEquals(arrivalTime, supposedArrivalTime, 
            "L'heure d'arrivée de l'itinéraire devrait etre 9h05.");
    }

    /**
     * Teste la fonction isEmpty avec un itinéraire vide.
     */
    @Test
    public void testIsEmptyTrue() {
        Itinerary itinerary = new Itinerary(LocalTime.of(0, 0), new ArrayList<Transport>());
        assertTrue(itinerary.isEmpty(), "Le calcul de l'heure d'arrivée de l'itinéraire est incorrect.");
    }

    /**
     * Teste la fonction isEmpty avec un itinéraire non vide.
     */
    @Test
    public void testIsEmptyFalse() {
        Itinerary itinerary = createItinerary();
        assertFalse(itinerary.isEmpty(), "Le calcul de l'heure d'arrivée de l'itinéraire est incorrect.");
    }

    /**
     * Teste la fonction addToFirstPosition.
     */
    @Test
    public void testAddToFirstPosition() {
        Itinerary itinerary = createItinerary();
        Walk toBeAdded = new Walk(new Double(0, 0), new Double(0, 0));

        int sizeBefore = itinerary.getTransports().size();
        itinerary.addToFirstPosition(toBeAdded);
        int sizeAfter = itinerary.getTransports().size();
        Walk firstElement = (Walk) itinerary.getTransports().get(0);

        assertEquals(sizeBefore + 1, sizeAfter, 
            "La taille de la liste de modes de transport devrait être de 3.");
        assertEquals(firstElement, toBeAdded, 
            "Les deux trajets à pied ajoutés devraient être égaux.");
    }

    /**
     * Teste la fonction addToLastPosition.
     */
    @Test
    public void testAddToLastPosition() {
        Itinerary itinerary = createItinerary();
        Walk toBeAdded = new Walk(new Double(0, 0), new Double(0, 0));

        int sizeBefore = itinerary.getTransports().size();
        itinerary.addToLastPosition(toBeAdded);
        int sizeAfter = itinerary.getTransports().size();
        Walk lastElement = (Walk) itinerary.getTransports().get(sizeAfter - 1);

        assertEquals(sizeBefore + 1, sizeAfter, 
            "La taille de la liste de modes de transport devrait être de 3.");
        assertEquals(lastElement, toBeAdded, 
            "Les deux trajets à pied ajoutés devraient être égaux.");
    }

    /**
     * Teste la fonction toString.
     */
    @Test
    public void testToString() {
        ArrayList<Transport> transports = new ArrayList<Transport>();
        Walk walk = new Walk(new Double(0, 0), new Double(0, 0));
        Path path = createPathWithDefaultValues(Duration.ofMinutes(0));
        transports.add(path);
        transports.add(walk);
        Itinerary itinerary = new Itinerary(LocalTime.of(0, 0), transports);

        String itineraryString = 
            itinerary.getDepartureTime().toString() + "\n" 
            + path.toString() + "\n" + walk.toString();
        String resultString = itinerary.toString();
        assertEquals(itineraryString, resultString, 
            "Les deux chaînes de caractères devraient être égales.");
    }
}