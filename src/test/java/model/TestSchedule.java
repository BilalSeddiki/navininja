package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSchedule {

    /**
     * Constructeurs et getters.
     */
    @Test
    public void testConstructorAndGetters() {

        String direction = "Olympiades";
        String passingTime = "14:04";
        Schedule schedule = new Schedule(direction, passingTime);
        assertEquals(direction, schedule.getDirectionAsSimpleString(),
            "L'attribut direction est incorrect.");
        assertEquals(passingTime, schedule.getPassingTimeAsSimpleString(),
            "L'attribut passingTime est incorrect.");
    }
}
