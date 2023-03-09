package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/** Un itinéraire d'une station à une autre. */
public class Itinerary {
    private LocalTime departureTime;
    private ArrayList<Path> paths;

    public Itinerary(LocalTime departureTime, ArrayList<Path> paths) {
        this.departureTime = departureTime;
        this.paths = paths;
    }

    public Station getSource() {
        return null;
    }

    public Station getDestination() {
        return null;
    }

    public ArrayList<Station> getStations() {
        return new ArrayList<Station>();
    }

    public ArrayList<LocalTime> getTrainDepartureTimes() {
        return new ArrayList<>();
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivaTime() {
        return LocalTime.of(0, 0);
    }

    public Duration getDuration() {
        return Duration.ofSeconds(0);
    }


}
