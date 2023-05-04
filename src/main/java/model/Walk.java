package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/** Un trajet à pied d'un point à un autre. */
public class Walk implements Transport {

    /**
     * constante de calcul pour GPS.
     */
    private static final int GPS = 6371 * 1000;
    /**
     * constante de calcul pour la vitesse de marche.
     */
    private static final double SPEED = 5.0 * 1000;
    /**
     * constante de calcul pour la conversion minute/seconde.
     */
    private static final double MS = 60;


    /** Coordonnées GPS de départ. */
    private Double departureCoordinates;

    /** Coordonnées GPS d'arrivée. */
    private Double arrivalCoordinates;

    /**
     * Construit une trajet à pied d'un point à un autre.
     * @param departureCoordinates coordonnées GPS de départ
     * @param arrivalCoordinates coordonnées GPS d'arrivée
     */
    public Walk(final Double departureCoordinates,
                final Double arrivalCoordinates) {
        this.departureCoordinates = departureCoordinates;
        this.arrivalCoordinates = arrivalCoordinates;
    }

    /**
     * Renvoie les coordonnées GPS du point de départ.
     * @return coordonnées GPS du point de départ.
     */
    public Double getDepartureCoordinates() {
        return this.departureCoordinates;
    }

    /**
     * Renvoie les coordonnées GPS du point d'arrivée.
     * @return coordonnées GPS du point d'arrivée.
     */
    public Double getArrivalCoordinates() {
        return this.arrivalCoordinates;
    }

    /**
     * Calcule la distance entre les deux points GPS en mètres.
     * @return distance entre les deux points GPS en mètres.
     */
    public double getTravelDistance() {
        double x1Rad = Math.toRadians(this.departureCoordinates.getX());
        double y1 = this.departureCoordinates.getY();
        double x2Rad = Math.toRadians(this.arrivalCoordinates.getX());
        double y2 = this.arrivalCoordinates.getY();
        double yRad = Math.toRadians(y2 - y1);

        double xSin = Math.sin(x1Rad) * Math.sin(x2Rad);
        double xyCos = Math.cos(x1Rad) * Math.cos(x2Rad) * Math.cos(yRad);
        return Math.acos(xSin + xyCos) * GPS;
    }

    /**
     * Calcule la durée du trajet à pied entre deux points GPS.
     * La vitesse moyenne considérée est 5 km/h.
     *
     * @return la durée du trajet à pied entre deux points GPS.
     */
    public Duration getTravelDuration() {
        double distance = this.getTravelDistance() * MS;
        long ratio = Math.round(distance / SPEED);

        return Duration.ofMinutes(ratio);
    }

    /**
     * {@inheritDoc}
     * Renvoie la durée du trajet à pied d'un point à un autre.
     *
     * @param departure l'heure de départ du trajet (inutilisée)
     * @return la durée du trajet à pied
     */
    @Override
    public Duration getTransportDuration(final LocalTime departure) {
        return this.getTravelDuration();
    }

    /**
     * {@inheritDoc}
     * Renvoie le moyen de transport utilisé pour atteindre une destination
     *
     * @return une option parmi les moyen de transports possibles.
     */
    @Override
    public TransportationMethod getTransportMethod() {
        return TransportationMethod.WALK;
    }

    @Override
    public final boolean equals(final Object arg0) {
        return arg0 instanceof Walk w
                && this.departureCoordinates.equals(w.departureCoordinates)
                && this.arrivalCoordinates.equals(w.arrivalCoordinates);
    }

    @Override
    public final String toString() {
        return "WALK: " + departureCoordinates + " ; " + arrivalCoordinates
            + " (" + getTravelDistance() + " ; " + getTravelDuration() + ")";
    }

    @Override
    public final Point2D.Double getInCoordinates() {
        return departureCoordinates;
    }

    @Override
    public final Point2D.Double getOutCoordinates() {
        return arrivalCoordinates;
    }

    @Override
    public final Optional<LocalTime> nextDeparture(final LocalTime from) {
        return Optional.of(from);
    }

    @Override
    public final Optional<Duration> totalDuration(final LocalTime departure) {
        return Optional.of(getTravelDuration());
    }
}
