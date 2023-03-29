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

    /**
     * Renvoie l'ensemble des chemins constituant l'itinéraire
     * @return une liste de chemins
     */
    public ArrayList<Path> getPaths() {
        return paths;
    }

    /**
     * Renvoie l'heure de départ de l'itinéraire
     * @return l'heure de départ de l'itinéraire
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Renvoie l'heure d'arrivé de l'itinéraire
     * @return l'heure d'arrivé de l'itinéraire
     */
    public LocalTime getArrivaTime() {
        return LocalTime.of(0, 0);
    }

    /**
     * Renvoie la durée totale de l'itinéraire
     * @return la durée de l'itinéraire
     */
    public Duration getDuration() {
        return Duration.ofSeconds(0);
    }

}
