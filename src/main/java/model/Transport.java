package model;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

/** Un mode de transport d'un point à un autre. */
public interface Transport {

    /**
     * Enumeration des moyens de transport possibles: à pieds, en transports
     */
    enum TransportationMethod{
       WALK, TRANSPORTATION
    }
    /**
     * Renvoie la durée du trajet d'un point à un autre en fonction du mode de transport.
     * L'heure de départ n'est pas prise en compte dans le calcul du trajet à pied.
     * @param departureTime l'heure de départ
     * @return la durée du trajet
     */
    public Duration getTransportDuration(LocalTime departureTime);

    public double getTravelDistance();

    public Duration getTravelDuration();

    /**
     * Renvoie le moyen de transport utilisé pour atteindre une destination
     * @return une option parmi les moyen de transports possibles.
     */
    TransportationMethod getTransportMethod();

    public Point2D.Double getInCoordinates();

    public Point2D.Double getOutCoordinates();

    public Optional<LocalTime> nextDeparture(LocalTime from);

    public Optional<Duration> totalDuration(LocalTime departure);
}