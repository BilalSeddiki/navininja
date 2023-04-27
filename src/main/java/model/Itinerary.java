package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import java.util.ArrayList;

/** Un itinéraire d'une station à une autre. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des chemins constituant l'itinéraire */
    private List<Path> paths;
    /** L'ensemble des chemins et trajets à pied constituant l'itinéraire (Temporaire) */
    private List<Transport> transports;

    /**
     * Construit un itinéraire. 
     * @param departureTime l'heure de départ de l'itinéraire
     * @param paths l'ensemble des chemins constituant l'itinéraire
     */
    public Itinerary(LocalTime departureTime, List<Path> paths) {
        this.departureTime = departureTime;
        this.paths = paths;
        //this.transports = transports;
        this.transports = new ArrayList<Transport>();
        for(int i = 0; i < paths.size(); i++) {
            transports.add(paths.get(i));
        }
    }

    /**
     * Renvoie l'ensemble des chemins constituant l'itinéraire
     * @return une liste de chemins
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     * Renvoie l'ensemble des chemins et trajets à pieds constituant l'itinéraire
     * @return une liste de chemins et trajets à pieds
     */
    public List<Transport> getTransports() {
        return transports;
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
        for (Path path : this.paths) {
            Duration pathDuration = path.totalDuration(newDeparture);
            total = total.plus(pathDuration);
            newDeparture = newDeparture.plus(pathDuration);
        }
        return total;
    }

    /** TODO */
    public void addToItineraryWithIndex(int index, Transport transport) {
        if(index >= 0 && index < this.transports.size()) {
            this.transports.add(transport);
        }
    }

    /** TODO */
    public void addToItinerary(Transport transport) {
        this.transports.add(transport);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(departureTime.toString());
        for (Path path : paths) {
            stringBuilder.append("\n");
            stringBuilder.append(path);
        }
        return stringBuilder.toString();
    }
}
