package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Itinerary {
    private Station source;
    private Station destination;
    private LocalTime departureTime;
    private ArrayList<Path> paths;

    public Itinerary(Station source, Station destination, LocalTime departureTime, ArrayList<Path> paths) {
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.paths = paths;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
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
