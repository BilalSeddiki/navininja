package model;

import java.time.Duration;
import java.time.LocalTime;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestWalk {

    /**
     * Teste le constructeur ainsi que les getters de la classe Walk.
     */
    @Test
    public void testConstructorAndGetters() {
        Double departure = new Double(0, 0);
        Double arrival = new Double(0, 0);

        Walk walk = new Walk(departure, arrival);
        assertSame(departure, walk.getDepartureCoordinates(),
            "L'attribut departureCoordinates est incorrect.");
        assertSame(arrival, walk.getArrivalCoordinates(),
            "L'attribut arrivalCoordinates est incorrect.");
    }

    /**
     * Teste la fonction getTravelDistance.
     */
    @Test
    public void testGetTravelDistance() {
        Double departure = new Double(48.857756, 2.294634);
        Double arrival = new Double(48.857689, 2.295063);
        Walk walk = new Walk(departure, arrival);

        double low = 30.0;
        double up = 40.0;
        double distance = walk.getTravelDistance();
        assertTrue(distance >= low && distance <= up,
            "La distance entre les deux points
            devrait être comprise entre 30 et 40 mètres. ");
    }

    /**
     * Teste la fonction getTravelDuration.
     */
    @Test
    public void testGetTravelDuration() {
        Double departure = new Double(48.857756, 2.294634);
        Double arrival = new Double(48.855960, 2.296236); 
        Walk walk = new Walk(departure, arrival);

        Duration duration = walk.getTravelDuration();
        Duration expected = Duration.ofMinutes(3);
        assertTrue(duration.compareTo(expected) == 0,
            "La durée du trajet à pied entre ces deux points devrait être de trois minutes. ");
    }

    /**
     * Teste la fonction getTransportDuration.
     */
    @Test
    public void testGetTransportDuration() {
        Double departure = new Double(48.857756, 2.294634);
        Double arrival = new Double(48.855960, 2.296236); 
        Walk walk = new Walk(departure, arrival);

        Duration durationTravel = walk.getTravelDuration();
        Duration durationTransport = walk.getTransportDuration(LocalTime.of(0, 0));
        assertTrue(durationTravel.compareTo(durationTransport) == 0,
            "La durée du trajet à pied entre ces deux points renvoyée par les deux fonctions doivent être égales. ");
    }

    /**
     * Teste la fonction equals dans le cas où les deux trajets ont les mêmes
     * coordonnées GPS.
     */
    @Test
    public void testEqualsTrue() {
        Walk walk1 = new Walk(new Double(0, 0), new Double(1, 1));
        Walk walk2 = new Walk(new Double(0, 0), new Double(1, 1));
        assertTrue(walk1.equals(walk2), 
            "Les deux trajets à pied doivent être égaux.");
    }

    /**
     * Teste la fonction equals dans le cas où les deux trajets n'ont pas 
     * les mêmes coordonnées GPS.
     */
    @Test
    public void testEqualsFalse() {
        Walk walk1 = new Walk(new Double(0, 0), new Double(1, 1));
        Walk walk2 = new Walk(new Double(0, 0), new Double(2, 2));
        Walk walk3 = new Walk(new Double(1, 2), new Double(3, 4));
        assertFalse(walk1.equals(walk2), 
            "Les deux trajets à pied ne doivent pas être égaux.");
        assertFalse(walk1.equals(walk3), 
            "Les deux trajets à pied ne doivent pas être égaux.");
    }
}
