package model;

import java.time.LocalTime;
import javafx.beans.property.*;
import javafx.scene.control.cell.PropertyValueFactory;

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

    /** 
     * Renvoie la direction du trajet sous forme de SimpleStringProperty.
     * @return La direction du trajet sous forme de SimpleStringProperty.
     */
    public SimpleStringProperty getDirectionAsSimpleString() {
        return new SimpleStringProperty(this.direction.getName());
    }

    /** 
     * Renvoie l'horaire de passage sous forme de SimpleStringProperty.
     * @return L'horaire de passage sous forme de SimpleStringProperty.
     */
    public SimpleStringProperty getPassingTimeAsSimpleString() {
        return new SimpleStringProperty(this.passingTime.toString());
    }
}