package model;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

/** TODO */
public interface Transport {

    /** TODO */
    public double getTravelDistance();

    /** TODO */
    public Duration getTravelDuration();

    public Point2D.Double getInCoordinates();

    public Point2D.Double getOutCoordinates();

    public Optional<LocalTime> nextDeparture(LocalTime from);

    public Optional<Duration> totalDuration(LocalTime departure);
}