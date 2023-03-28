package model;

import java.time.LocalTime;

/** Horaire de passage dans une certaine direction. */
public class Schedule {

    /** Direction du trajet. */
    private Station direction;

    /** Horaire de passage. */
    private LocalTime passingTime;

    /**
     * Construit un horaire de passage associée à une direction.
     * @param direction La direction du trajet.
     * @param passingTime L'horaire de passage.
     */
    public Schedule(Station direction, LocalTime passingTime) {
        this.direction = direction;
        this.passingTime = passingTime;
    }

    /**
     * Renvoie la direction du trajet.
     * @return La direction du trajet.
     */
    public Station getDirection() {
        return this.direction;
    }

    /**
     * Renvoie l'horaire de passage.
     * @return L'horaire de passage.
     */
    public LocalTime getPassingTime() {
        return this.passingTime;
    }
}