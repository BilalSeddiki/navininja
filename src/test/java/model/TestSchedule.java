package model;

import java.time.LocalTime;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSchedule {

    /* Constructeurs et getters */
    @Test
    public void testConstructorAndGetters() {
        Station direction = new Station("", new Double(0, 0));
        LocalTime passingTime = LocalTime.MIN;

        Schedule schedule = new Schedule(direction, passingTime);
        assertEquals(direction, schedule.getDirection(), "L'attribut direction est incorrect.");
        assertEquals(passingTime, schedule.getPassingTime(), "L'attribut passingTime est incorrect.");
    }
}