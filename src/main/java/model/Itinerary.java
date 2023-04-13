package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/** Un itinéraire d'une station à une autre. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des chemins constituant l'itinéraire */
    private List<Path> paths;

    /**
     * Construit un itinéraire. 
     * @param departureTime l'heure de départ de l'itinéraire
     * @param paths l'ensemble des chemins constituant l'itinéraire
     */
    public Itinerary(LocalTime departureTime, List<Path> paths) {
        this.departureTime = departureTime;
        this.paths = paths;
    }

    /**
     * Renvoie l'ensemble des chemins constituant l'itinéraire
     * @return une liste de chemins
     */
    public List<Path> getPaths() {
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
    public LocalTime getArrivalTime() {
        Duration duration = this.getDuration();
        LocalTime arrivalTime = departureTime.plus(duration);
        return arrivalTime;
    }

    /**
     * Renvoie la durée totale de l'itinéraire
     * @return la durée de l'itinéraire
     */
    public Duration getDuration() {
        Duration total = Duration.ZERO;
        LocalTime newDeparture = departureTime;
        for(int i = 0; i < this.paths.size(); i++) {
            Duration pathDuration = paths.get(i).totalDuration(newDeparture);
            total = total.plus(pathDuration);
            newDeparture = newDeparture.plus(pathDuration);
        }
        return total;
    }
}
