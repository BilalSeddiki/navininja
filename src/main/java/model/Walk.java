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
        return 0;
    }

    /** TODO */
    public Duration getTravelDuration() {
        return Duration.ofMinutes(0);
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Walk w &&
            this.departureCoordinates == w.departureCoordinates &&
            this.arrivalCoordinates == w.arrivalCoordinates;
    }

    //TODO
    @Override
    public String toString() {
        return "TODO";
    }
}