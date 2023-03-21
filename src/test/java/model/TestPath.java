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
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        assertEquals(lineName, path.getLineName(), "L'attribut lineName est incorrect.");
        assertEquals(variant, path.getVariant(), "L'attribut variant est incorrect.");
        assertEquals(schedule, path.getSchedule(), "L'attribut schedule est incorrect.");
        assertEquals(travelDuration, path.getTravelDuration(), "L'attribut travelDuration est incorrect.");
        assertEquals(travelDistance, path.getTravelDistance(), "L'attribut travelDistance est incorrect.");
        assertEquals(source, path.getSource(), "L'attribut source est incorrect.");
        assertEquals(destination, path.getDestination(), "L'attribut destination est incorrect.");

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals. */
    }

    //TODO (?): Factorisation des constructeurs.
    @Test
    public void testConstructorLineNameNotEmpty() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        //assertFalse(path.getLineName().isEmpty(), "L'attribut lineName ne peut pas être vide.");
    }

    @Test
    public void testConstructorScheduleNotEmpty() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        //assertFalse(path.getSchedule().isEmpty(), "L'attribut schedule ne peut pas être vide.");
    }

    @Test
    public void testConstructorTravelDurationNotZero() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        //assertNotEquals(path.getTravelDuration(), Duration.ZERO, "L'attribut travelDuration ne peut pas valoir zéro.");
    }

    @Test
    public void testConstructorTravelDistanceNotZero() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        //assertNotEquals(path.getTravelDistance(), 0, "L'attribut travelDistance ne peut pas valoir zéro.");
    }

    @Test
    public void testConstructorSourceNotNull() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        assertNotNull(path.getSource(), "L'attribut source ne peut pas être nul.");
    }

    @Test
    public void testConstructorNextStationNotNull() {
        String lineName = "";
        int variant = 0;
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));

        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);
        assertNotNull(path.getDestination(), "L'attribut destination ne peut pas être nul.");
    }
}