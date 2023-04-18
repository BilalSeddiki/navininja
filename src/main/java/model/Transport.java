package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** TODO */
public interface Transport {

    public double getTravelDistance();

    public Duration getTravelDuration();
}