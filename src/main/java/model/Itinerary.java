package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import java.util.ArrayList;

/** Un itinéraire composé d'un ensemble de modes de transport et d'une heure de départ. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des modes de transport constituant l'itinéraire */
    private List<Transport> transports;

    /**
     * Construit un itinéraire. 
     * @param departureTime l'heure de départ de l'itinéraire
     * @param transports l'ensemble des modes de transport constituant l'itinéraire
     */
    public Itinerary(LocalTime departureTime, List<Transport> transports) {
        this.departureTime = departureTime;
        this.transports = transports;
    }

    /**
     * Renvoie l'ensemble des modes de transport constituant l'itinéraire.
     * @return une liste de modes de transport
     */
    public List<Transport> getTransports() {
        return transports;
    }

    /**
     * Renvoie l'heure de départ de l'itinéraire.
     * @return l'heure de départ de l'itinéraire
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Renvoie l'heure d'arrivée de l'itinéraire.
     * @return l'heure d'arrivée de l'itinéraire
     */
    public LocalTime getArrivalTime() {
        Duration duration = this.getDuration();
        LocalTime arrivalTime = departureTime.plus(duration);
        return arrivalTime;
    }

    /**
     * Renvoie la durée totale de l'itinéraire.
     * @return la durée de l'itinéraire
     */
    public Duration getDuration() {
        Duration total = Duration.ZERO;
        LocalTime newDeparture = departureTime;
        for (Transport transport : this.transports) {
            Duration pathDuration = transport.getTransportDuration(newDeparture);
            total = total.plus(pathDuration);
        }
        return total;
    }

    /**
     * Vérifie si l'ensemble des modes de transport de l'itinéraire est vide.
     * @return true si la liste est vide, false sinon
     */
    public boolean isEmpty() {
        if(this.transports.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Ajoute un mode de transport au début de la liste.
     * @param transport Mode de transport à ajouter au début de la liste de l'itinéraire.
     */
    public void addToFirstPosition(Transport transport) {
        this.transports.add(0, transport);
    }

    /**
     * Ajoute un mode de transport à la fin de la liste.
     * @param transport Mode de transport à ajouter à la fin de la liste de l'itinéraire.
     */
    public void addToLastPosition(Transport transport) {
        this.transports.add(transport);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(departureTime.toString());
        for (Transport transport : transports) {
            stringBuilder.append("\n");
            stringBuilder.append(transport);
        }
        return stringBuilder.toString();
    }
}