package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPath {

    /* Constructeurs et getters */
    public Path createPathWithDefaultValues() {
        String lineName = "";
        String variant = "0";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Duration travelDuration = Duration.ZERO;
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));
        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);

        return path;
    }

    @Test
    public void testConstructorAndGetters() {
        String lineName = "";
        String variant = "0";
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

    @Test
    public void testConstructorLineNameNotEmpty() {
        Path path = createPathWithDefaultValues();
        //assertFalse(path.getLineName().isEmpty(), "L'attribut lineName ne peut pas être vide.");
    }

    @Test
    public void testConstructorScheduleNotEmpty() {
        Path path = createPathWithDefaultValues();
        //assertFalse(path.getSchedule().isEmpty(), "L'attribut schedule ne peut pas être vide.");
    }

    @Test
    public void testConstructorTravelDurationNotZero() {
        Path path = createPathWithDefaultValues();
        //assertNotEquals(path.getTravelDuration(), Duration.ZERO, "L'attribut travelDuration ne peut pas valoir zéro.");
    }

    @Test
    public void testConstructorTravelDistanceNotZero() {
        Path path = createPathWithDefaultValues();
        //assertNotEquals(path.getTravelDistance(), 0, "L'attribut travelDistance ne peut pas valoir zéro.");
    }

    @Test
    public void testConstructorSourceNotNull() {
        Path path = createPathWithDefaultValues();
        assertNotNull(path.getSource(), "L'attribut source ne peut pas être nul.");
    }

    @Test
    public void testConstructorNextStationNotNull() {
        Path path = createPathWithDefaultValues();
        assertNotNull(path.getDestination(), "L'attribut destination ne peut pas être nul.");
    }

    /* Fonction nextTrainDeparture */
    @Test
    public void testNextTrainDepartureSameDay() {
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Station source = new Station("Station 1", new Double(0, 0));
        Station destination = new Station("Station 2", new Double(0, 0));

        Path path = new Path("Test", "0", schedule, Duration.ZERO, 0, source, destination);
        LocalTime depart = LocalTime.of(8, 37, 00);
        assertEquals(schedule.get(1), path.nextTrainDeparture(depart), "TODO");
    }

    @Test
    public void testNextTrainDepartureNextDay() {
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Station source = new Station("Station 1", new Double(0, 0));
        Station destination = new Station("Station 2", new Double(0, 0));

        Path path = new Path("Test", "0", schedule, Duration.ZERO, 0, source, destination);
        LocalTime depart = LocalTime.of(23, 30, 00);
        assertEquals(schedule.get(0), path.nextTrainDeparture(depart), "Le calcul du prochain départ est incorrect.");
    }
    @Test
    public void testPropagation(){
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Station source = new Station("Source", new Double(0, 0));
        Station s1 = new Station("Station 1", new Double(0, 0));
        Station s2 = new Station("Station 2", new Double(0,0));
        Station destination= new Station("Destination",new Double(0,0));
        Duration t=Duration.ofMinutes(1);

        Path path1 = new Path("Test", "0", schedule, t, 0, source, s1);
        source.addOutPath(path1);
        s1.addInPath(path1);
        Path path2 = new Path("Test", "0", new ArrayList<LocalTime>(), t, 0, s1, s2);
        s1.addOutPath(path2);
        s2.addInPath(path2);
        Path path3 = new Path("Test", "0", new ArrayList<LocalTime>(), t, 0, s2, destination);
        s2.addOutPath(path3);
        destination.addInPath(path3);


        LocalTime depart = LocalTime.of(8, 30, 00);
        assertEquals(LocalTime.of(8,32,00), path3.nextTrainDeparture(depart), "Le calcul du prochain départ est incorrect.");
    }

    @Test
    public void testNextTrainDepartureEmptySchedule() {
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        Station source = new Station("Station 1", new Double(0, 0));
        Station destination = new Station("Station 2", new Double(0, 0));

        Path path = new Path("Test", "0", schedule, Duration.ZERO, 0, source, destination);
        LocalTime depart = LocalTime.of(14, 30, 00);
        assertEquals(LocalTime.of(0, 0), path.nextTrainDeparture(depart), "Le calcul du prochain départ est incorrect.");
    }

    /* Fonction totalDuration */
    @Test
    public void testTotalDurationPositive() {
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Duration travelDuration = Duration.ofMinutes(5);
        Station source = new Station("Station 1", new Double(0, 0));
        Station destination = new Station("Station 2", new Double(0, 0));
        //TODO: Factoriser les constructeurs pour les Station ?
        
        Path path = new Path("Test", "0", schedule, travelDuration, 0, source, destination);
        LocalTime depart = LocalTime.of(7, 30, 00);
        Duration duration = (Duration.ofMinutes(60)).plus(travelDuration); 
        //TODO: Trouver une autre façon de préciser les données ?
        assertEquals(duration, path.totalDuration(depart), "Le calcul de la durée totale du trajet est incorrect.");
    }

    @Test
    public void testTotalDurationNegative() {
        String lineName = "Test";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        Duration travelDuration = Duration.ofMinutes(5);
        Station source = new Station("Station 1", new Double(0, 0));
        Station destination = new Station("Station 2", new Double(0, 0));
        //TODO: Factoriser les constructeurs pour les Station ?

        Path path = new Path("Test", "0", schedule, travelDuration, 0, source, destination);
        LocalTime depart = LocalTime.of(23, 30, 00);
        Duration duration = (Duration.ofMinutes(540)).plus(travelDuration); 
        //TODO: Trouver une autre façon de préciser les données ?
        assertEquals(duration, path.totalDuration(depart), "Le calcul de la durée totale du trajet est incorrect.");
    }
}