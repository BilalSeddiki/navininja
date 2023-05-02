package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPath {

    /**
     * Cree un chemin avec des valeurs par defaut.
     * @return chemin avec des valeurs par defaut.
     */
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

    /**
     * Cree un chemin avec des valeurs par defaut et une liste
     * d'horaires de passage des trains non vide.
     * @return Chemin avec des valeurs par defaut et une liste d'horaire.
     */
    public Path createPathWithScheduleAndTravelDuration() {
        String lineName = "";
        String variant = "0";
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Duration travelDuration = Duration.ofMinutes(5);
        double travelDistance = 0;
        Station source = new Station("", new Double(0, 0));
        Station destination = new Station("", new Double(0, 0));
        Path path = new Path(lineName, variant, schedule, travelDuration, travelDistance, source, destination);

        return path;
    }

    /**
     * Teste le constructeur ainsi que les getters de la classe Path.
     */
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
        assertSame(lineName, path.getLineName(), "L'attribut lineName est incorrect.");
        assertSame(variant, path.getVariant(), "L'attribut variant est incorrect.");
        assertTrue(schedule.equals(path.getSchedule()), "L'attribut schedule est incorrect.");
        assertSame(travelDuration, path.getTravelDuration(), "L'attribut travelDuration est incorrect.");
        assertEquals(travelDistance, path.getTravelDistance(), "L'attribut travelDistance est incorrect.");
        assertSame(source, path.getSource(), "L'attribut source est incorrect.");
        assertSame(destination, path.getDestination(), "L'attribut destination est incorrect.");
    }

    /**
     * Teste la fonction nextTrainDeparture dans le cas ou le prochain train
     * arrive le jour même.
     */
    @Test
    public void testNextTrainDepartureSameDay() {
        Path path = createPathWithScheduleAndTravelDuration();
        LocalTime depart = LocalTime.of(8, 37, 00);
        assertEquals(path.getSchedule().get(1), path.nextDeparture(depart).get(),
                "Le calcul du prochain depart est incorrect.");
    }

    /**
     * Teste la fonction nextTrainDeparture dans le cas ou le prochain train
     * arrive le lendemain.
     */
    @Test
    public void testNextTrainDepartureNextDay() {
        Path path = createPathWithScheduleAndTravelDuration();
        LocalTime depart = LocalTime.of(23, 30, 00);
        assertEquals(path.getSchedule().get(0), path.nextDeparture(depart).get(),
                "Le calcul du prochain depart est incorrect.");
    }

    /**
     * Teste la fonction nextTrainDeparture dans le cas ou la liste des horaires
     * est vide.
     */
    @Test
    public void testNextTrainDepartureEmptySchedule() {
        Path path = createPathWithDefaultValues();
        LocalTime depart = LocalTime.of(14, 30, 00);
        assertEquals(Optional.empty(), path.nextDeparture(depart),
                "Le calcul du prochain départ est incorrect.");
    }

    /**
     * Teste la propagation.
     */
    @Test
    public void testPropagation() {
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        schedule.add(LocalTime.of(8, 40, 00));
        schedule.add(LocalTime.of(8, 50, 00));
        Station source = new Station("Source", new Double(0, 0));
        Station s1 = new Station("Station 1", new Double(0, 0));
        Station s2 = new Station("Station 2", new Double(0, 0));
        Station destination = new Station("Destination", new Double(0, 0));
        Duration duration = Duration.ofMinutes(1);

        Path path1 = new Path("Test", "0", schedule, duration, 0, source, s1);
        source.addOutPath(path1);
        s1.addInPath(path1);
        Path path2 = new Path("Test", "0", new ArrayList<LocalTime>(), duration, 0, s1, s2);
        s1.addOutPath(path2);
        s2.addInPath(path2);
        Path path3 = new Path("Test", "0", new ArrayList<LocalTime>(), duration, 0, s2, destination);
        s2.addOutPath(path3);
        destination.addInPath(path3);

        LocalTime depart = LocalTime.of(8, 30, 00);
        assertEquals(LocalTime.of(8, 32, 00), path3.nextDeparture(depart).get(),
                "Le calcul du prochain départ est incorrect.");
    }

    /**
     * Teste la fonction totalDuration dans le cas ou le temps d'attente
     * entre l'heure de depart et l'heure d'arrivee du prochain train
     * est positive.
     */
    @Test
    public void testTotalDurationPositive() {
        Path path = createPathWithScheduleAndTravelDuration();
        LocalTime depart = LocalTime.of(7, 30, 00);
        Duration travelDuration = path.getTravelDuration();
        Duration duration = (Duration.ofMinutes(60)).plus(travelDuration);
        assertEquals(duration, path.totalDuration(depart).get(),
                "Le calcul de la durée totale du trajet est incorrect.");
    }

    /**
     * Teste la fonction totalDuration dans le cas ou le temps d'attente
     * entre l'heure de depart et l'heure d'arrivee du prochain train
     * est negative.
     */
    @Test
    public void testTotalDurationNegative() {
        Path path = createPathWithScheduleAndTravelDuration();
        LocalTime depart = LocalTime.of(23, 30, 00);
        Duration travelDuration = path.getTravelDuration();
        Duration duration = (Duration.ofMinutes(540)).plus(travelDuration);
        assertEquals(duration, path.totalDuration(depart).get(),
                "Le calcul de la durée totale du trajet est incorrect.");
    }

    /**
     * Teste la fonction setTerminus dans le cas ou la liste des horaires
     * de passage des trains est vide.
     */
    @Test
    public void testSetTerminusWithEmptySchedule() {
        Path path = createPathWithDefaultValues();
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        path.setTerminus(schedule);
        assertFalse(path.getTerminus());
    }

    /**
     * Teste la fonction setTerminus dans le cas ou la liste des horaires
     * de passage des trains n'est pas vide.
     */
    @Test
    public void testSetTerminusWithNotEmptySchedule() {
        Path path = createPathWithDefaultValues();
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30, 00));
        path.setTerminus(schedule);
        assertTrue(path.getTerminus());
    }
}