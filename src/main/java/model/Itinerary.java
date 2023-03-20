package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/** Un itinéraire d'une station à une autre. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des chemins constituant l'itinéraire */
    private ArrayList<Path> paths;

    /**
     * Construit un itinéraire. 
     * @param departureTime l'heure de départ de l'itinéraire
     * @param paths l'ensemble des chemins constituant l'itinéraire
     */
    public Itinerary(LocalTime departureTime, ArrayList<Path> paths) {
        this.departureTime = departureTime;
        this.paths = paths;
    }

    public ArrayList<Path> getPaths() {
        return paths;
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
