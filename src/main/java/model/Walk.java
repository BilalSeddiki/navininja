package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/** Un trajet à pied d'un point à un autre. */
public class Walk implements Transport {

    /** Coordonnées GPS de départ. */
    private Double departureCoordinates;

    /** Coordonnées GPS d'arrivée. */
    private Double arrivalCoordinates;

    /**
     * Construit une trajet à pied d'un point à un autre.
     * @param departureCoordinates coordonnées GPS de départ
     * @param arrivalCoordinates coordonnées GPS d'arrivée
     */
    public Walk(Double departureCoordinates, Double arrivalCoordinates) {
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
        double x1_rad = Math.toRadians(this.departureCoordinates.getX());
        double y1 = this.departureCoordinates.getY();
        double x2_rad = Math.toRadians(this.arrivalCoordinates.getX());
        double y2 = this.arrivalCoordinates.getY();
        double y_rad = Math.toRadians(y2 - y1);

        double x_sin = Math.sin(x1_rad) * Math.sin(x2_rad);
        double xy_cos = Math.cos(x1_rad) * Math.cos(x2_rad) * Math.cos(y_rad);
        double distance = Math.acos(x_sin + xy_cos) * 6371 * 1000;

        return distance;
    }

    /** 
     * Calcule la durée du trajet à pied entre deux points GPS.
     * La vitesse moyenne considérée est 5 km/h.
     * @return la durée du trajet à pied entre deux points GPS.
     */
    public Duration getTravelDuration() {
        double speed = 5.0 * 1000;
        double distance = this.getTravelDistance() * 60;
        long ratio = Math.round(distance / speed);

        Duration duration = Duration.ofMinutes(ratio);
        return duration;
    }

    /**
     * {@inheritDoc}
     * Renvoie la durée du trajet à pied d'un point à un autre.
     * @param departure l'heure de départ du trajet (inutilisée)
     * @return la durée du trajet à pied
     */
    @Override
    public Duration getTransportDuration(LocalTime departure) {
        return this.getTravelDuration();
    }

    /**
     * {@inheritDoc}
     * Renvoie le moyen de transport utilisé pour atteindre une destination
     * @return une option parmi les moyen de transports possibles.
     */
    @Override
    public TransportationMethod getTransportMethod(){
        return TransportationMethod.WALK;
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Walk w &&
            this.departureCoordinates.equals(w.departureCoordinates) &&
            this.arrivalCoordinates.equals(w.arrivalCoordinates);
    }

    @Override
    public String toString() {
        return "WALK: " + departureCoordinates + " ; " + arrivalCoordinates + " (" + getTravelDistance() + " ; "
                + getTravelDuration() + ")";
    }

    @Override
    public Point2D.Double getInCoordinates() {
        return departureCoordinates;
    }

    @Override
    public Point2D.Double getOutCoordinates() {
        return arrivalCoordinates;
    }

    @Override
    public Optional<LocalTime> nextDeparture(LocalTime from) {
        return Optional.of(from);
    }

    @Override
    public Optional<Duration> totalDuration(LocalTime departure) {
        return Optional.of(getTravelDuration());
    }
}