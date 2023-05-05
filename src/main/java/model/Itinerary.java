package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/** Un itinéraire composé d'un ensemble de modes de transport
 * et d'une heure de départ. */
public class Itinerary {
    /** Heure de départ de l'itinéraire. */
    private LocalTime departureTime;
    /** L'ensemble des modes de transport constituant l'itinéraire. */
    private List<Transport> transports;

    /**
     * Construit un itinéraire.
     * @param departureTime l'heure de départ de l'itinéraire
     * @param transports l'ensemble des modes de transport constituant
     * l'itinéraire
     */
    public Itinerary(final LocalTime departureTime,
            final List<Transport> transports) {
        this.departureTime = departureTime;
        this.transports = transports;
    }

    /**
     * Renvoie l'ensemble des modes de transport constituant l'itinéraire.
     *
     * @return une liste de modes de transport
     */
    public List<Transport> getTransports() {
        return transports;
    }

    /**
     * Renvoie l'heure de départ de l'itinéraire.
     *
     * @return l'heure de départ de l'itinéraire
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Renvoie l'heure d'arrivée de l'itinéraire.
     *
     * @return l'heure d'arrivée de l'itinéraire
     */
    public LocalTime getArrivalTime() {
        Duration duration = this.getDuration();
        return departureTime.plus(duration);
    }

    /**
     * Renvoie la durée totale de l'itinéraire.
     *
     * @return la durée de l'itinéraire
     */
    public Duration getDuration() {
        Duration total = Duration.ZERO;
        LocalTime newDeparture = departureTime;
        for (int i = 0; i < this.transports.size(); i++) {
            Duration pathDuration = transports.get(i)
                    .totalDuration(newDeparture)
                    .orElseThrow(() ->
                    new IllegalStateException("Empty time in Itinerary"));
            total = total.plus(pathDuration);
        }
        return total;
    }

    /**
     * Vérifie si l'ensemble des modes de transport de l'itinéraire est vide.
     *
     * @return true si la liste est vide, false sinon
     */
    public boolean isEmpty() {
        return this.transports.isEmpty();
    }

    /**
     * Ajoute un mode de transport au début de la liste.
     *
     * @param transport Mode de transport à ajouter au début de la liste de
     *                  l'itinéraire.
     */
    public void addToFirstPosition(final Transport transport) {
        this.transports.add(0, transport);
    }

    /**
     * Ajoute un mode de transport à la fin de la liste.
     *
     * @param transport Mode de transport à ajouter à la fin de la liste de
     *                  l'itinéraire.
     */
    public void addToLastPosition(final Transport transport) {
        this.transports.add(transport);
    }

    /**
     * Ajoute un mode de transport au début ou à la fin de
     * la liste de l'itinéraire.
     *
     * @param transport Mode de transport à ajouter au début ou à la fin de
     *                  l'itinéraire.
     * @param position  ajoute au début si true, l'ajoute à la fin sinon.
     */
    public void addToPosition(final Transport transport,
                              final boolean position) {
        if (position) {
            this.addToFirstPosition(transport);
        } else {
            this.addToLastPosition(transport);
        }
    }

    @Override
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder(departureTime
                                                        .toString());
        for (Transport transport : transports) {
            stringBuilder.append("\n");
            stringBuilder.append(transport);
        }
        stringBuilder.append("\n");
        stringBuilder.append(getDuration());
        stringBuilder.append("\n");
        stringBuilder.append(getArrivalTime());
        return stringBuilder.toString();
    }
}
