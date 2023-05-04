package model;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

/** Un mode de transport d'un point à un autre. */
public interface Transport {

    /**
     * Enumeration des moyens de transport possibles: à pieds, en transports.
     */
    enum TransportationMethod {
        /** indique si c'est de la marche a pied. */
        WALK,
        /** indique si c'est un transport. */
        TRANSPORTATION
    }
    /**
     * Renvoie la durée du trajet d'un point à un autre
     * en fonction du mode de transport.
     * L'heure de départ n'est pas prise en compte
     * dans le calcul du trajet à pied.
     * @param departureTime l'heure de départ
     * @return la durée du trajet
     */
    Duration getTransportDuration(LocalTime departureTime);
    /**
     * renvoie la distance de voyage.
     * @return la distance de voyage
     */
    double getTravelDistance();
    /**
     * renvoie la durée de voyage.
     * @return la durée de voyage
     */
    Duration getTravelDuration();

    /**
     * Renvoie le moyen de transport utilisé pour atteindre une destination.
     *
     * @return une option parmi les moyen de transports possibles.
     */
    TransportationMethod getTransportMethod();

    /**
     * renvoie les Cordonnée GPS de depart.
     * @return les Cordonnée GPS de depart
     */
    Point2D.Double getInCoordinates();
    /**
     * renvoie les Cordonnée GPS de l'arrivée.
     * @return les Cordonnée GPS de l'arrivée
     */
    Point2D.Double getOutCoordinates();

    /**
     * renvoie le prochain depart si il existe a partir de l'heure donnée.
     * @param from l'heure de depart
     * @return renvoie le prochain depart si il existe sinon optional vide
     */
    Optional<LocalTime> nextDeparture(LocalTime from);

    /**
     * renvoie la durée a partir du prochain depart.
     * @param departure l'heure de depart
     * @return renvoie la durée a partir du prochain depart si il existe
     * sinon optional vide
     */
    Optional<Duration> totalDuration(LocalTime departure);
}
