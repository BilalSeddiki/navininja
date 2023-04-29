package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import java.util.ArrayList;

/** Un itinéraire d'une station à une autre. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des chemins et trajets à pied constituant l'itinéraire */
    private List<Transport> transports;

    /**
     * Construit un itinéraire. 
     * @param departureTime l'heure de départ de l'itinéraire
     * @param transports l'ensemble des chemins et trajets à pied constituant l'itinéraire
     */
    public Itinerary(LocalTime departureTime, List<Transport> transports) {
        this.departureTime = departureTime;
        this.transports = transports;
    }

    /**
     * Renvoie l'ensemble des chemins et trajets à pied constituant l'itinéraire
     * @return une liste de chemins et trajets à pied
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
        for(int i = 0; i < this.transports.size(); i++) {
            Duration pathDuration = transports.get(i).getTransportDuration(newDeparture);
            total = total.plus(pathDuration);
            newDeparture = newDeparture.plus(pathDuration);
        }
        return total;
    }

    /** TODO */
    public boolean isEmpty() {
        if(this.transports.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /** TODO */
    public void addToTransportsBeginning(Transport transport) {
        this.transports.add(0, transport);
    }

    /** TODO */
    public void addToTransportsEnding(Transport transport) {
        this.transports.add(transport);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(departureTime.toString());
        for (Transport transport : transports) {
            stringBuilder.append("\n");
            stringBuilder.append(transports);
        }
        return stringBuilder.toString();
    }
}
