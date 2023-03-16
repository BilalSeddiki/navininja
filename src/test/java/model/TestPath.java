package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPath {

    @Test
    public void testConstructorAndGetters() {
        String lineName = "";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station nextStation = new Station("", new Double(0, 0), new ArrayList<Path>());

        Path path = new Path(lineName, schedule, travelDuration, travelDistance, nextStation);
        assertEquals(lineName, path.getLineName(), "L'attribut lineName est incorrect.");
        assertEquals(schedule, path.getSchedule(), "L'attribut schedule est incorrect.");
        assertEquals(travelDuration, path.getTravelDuration(), "L'attribut travelDuration est incorrect.");
        assertEquals(travelDistance, path.getTravelDistance(), "L'attribut travelDistance est incorrect.");
        assertEquals(nextStation, path.getNextStation(), "L'attribut nextStation est incorrect.");

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals.
         * TODO (2 ?): 
         * Conditions de création (nom non vide, liste d'horaires non vide, ...).
         * -> Plusieurs tests (constructeur correct, constructeur incorrect).
         */
    }
}