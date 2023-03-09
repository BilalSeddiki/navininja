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
        Station source = new Station("", new Double(0, 0), new ArrayList<Path>());
        Station destination = new Station("", new Double(0, 0), new ArrayList<Path>());

        Path path = new Path(lineName, schedule, travelDuration, travelDistance, source, destination);
        assertEquals(lineName, path.getLineName(), "L'attribut lineName est incorrect.");
        assertEquals(schedule, path.getSchedule(), "L'attribut schedule est incorrect.");
        assertEquals(travelDuration, path.getTravelDuration(), "L'attribut travelDuration est incorrect.");
        assertEquals(travelDistance, path.getTravelDistance(), "L'attribut travelDistance est incorrect.");
        assertEquals(source, path.getSource(), "L'attribut source est incorrect.");
        assertEquals(destination, path.getDestination(), "L'attribut destination est incorrect.");

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals.
         * TODO (2 ?): 
         * Conditions de création (nom non vide, liste d'horaires non vide, ...).
         * -> Plusieurs tests (constructeur correct, constructeur incorrect).
         */
    }
}