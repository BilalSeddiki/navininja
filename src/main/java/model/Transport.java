package model;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;

/** TODO */
public interface Transport {

    /** TODO */
    public double getTravelDistance();

    /** TODO */
    public Duration getTravelDuration();

    public Point2D.Double getInCoordinates();

    public Point2D.Double getOutCoordinates();

    public LocalTime nextDeparture(LocalTime from);
}