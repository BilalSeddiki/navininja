package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D.Double;

/** TODO */
public class Walk implements Transport {
    
    /** Coordonnées GPS de départ. */
    private Double departureCoordinates;

    /** Coordonnées GPS d'arrivée. */
    private Double arrivalCoordinates;

    /** TODO */
    public Walk(Double departureCoordinates, Double arrivalCoordinates) {
        this.departureCoordinates = departureCoordinates;
        this.arrivalCoordinates = arrivalCoordinates;
    }

    /** TODO */
    public Double getDepartureCoordinates() {
        return this.departureCoordinates;
    }

    /** TODO */
    public Double getArrivalCoordinates() {
        return this.arrivalCoordinates;
    }

    /** TODO */
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

    /** TODO */
    public Duration getTravelDuration() {
        double speed = 5.0 * 1000;
        double distance = this.getTravelDistance() * 60;
        long ratio = Math.round(distance / speed);

        Duration duration = Duration.ofMinutes(ratio);
        return duration;
    }

    /** TODO */
    public Duration getTransportDuration(LocalTime departure) {
        return this.getTravelDuration();
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Walk w &&
            this.departureCoordinates == w.departureCoordinates &&
            this.arrivalCoordinates == w.arrivalCoordinates;
    }

    @Override
    public String toString() {
        return departureCoordinates.toString() + " -> " + arrivalCoordinates.toString();
    }
}