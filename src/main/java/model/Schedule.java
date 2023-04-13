package model;

import javafx.beans.property.*;

/** Horaire de passage dans une certaine direction. */
public class Schedule {

    /** Direction du trajet. */
    private final SimpleStringProperty direction;

    /** Horaire de passage. */
    private final SimpleStringProperty passingTime;

    /**
     * Construit un horaire de passage associée à une direction.
     * @param direction La direction du trajet.
     * @param passingTime L'horaire de passage.
     */
    public Schedule(String direction, String passingTime) {
        this.direction = new SimpleStringProperty(direction);
        this.passingTime = new SimpleStringProperty(passingTime);
    }

    /** 
     * Renvoie la direction du trajet sous forme de SimpleStringProperty.
     * @return La direction du trajet sous forme de SimpleStringProperty.
     */
    public String getDirectionAsSimpleString() {
        return this.direction.get();
    }

    /** 
     * Renvoie l'horaire de passage sous forme de SimpleStringProperty.
     * @return L'horaire de passage sous forme de SimpleStringProperty.
     */
    public String getPassingTimeAsSimpleString() {
        return this.passingTime.get();
    }
}