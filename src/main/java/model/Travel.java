package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;

/** TODO */
public class Travel {
    
    /** TODO */
    private LocalTime departureTime;

    /** TODO */
    private Double departureCoordinates;

    /** TODO */
    private Double arrivalCoordinates;

    /** TODO */
    private Station departureStation;

    /** TODO */
    private Station arrivalStation;

    //TODO: Patron de conception en fonction des arguments (et ordre) ou arguments null ?
    /** TODO */
    public Travel(LocalTime departureTime, Double departureCoordinates, 
        Double arrivalCoordinates, Station departureStation, 
        Station arrivalStation) {
            
        this.departureTime = departureTime;
        this.departureCoordinates = departureCoordinates;
        this.arrivalCoordinates = arrivalCoordinates;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    /* TODO: Representation des parties a pieds (Walk). */
    /* Interface: Path
     * Walk: coordonnees de depart, d'arrivee
     * Responsable du calcul de la distance et de la duree
     * Transport: Path actuel
     */

    /*
    public Itinerary fromStationToStation(Network network) {
        if(this.departureStation != null && this.arrivalStation != null) {
            return network.bestPath(this.departureStation, this.arrivalStation, this.departureTime);
        }
    }
    */

    public void fromCoordinatesToCoordinates() {
        /* TODO */
    }

    public void fromCoordinatesToStation() {
        /* Trouver les stations alentours du point de depart
         * Trouver le meilleur chemin jusqu'a la station d'arrivee
         * Calculer la distance entre le point de depart et la station de depart trouvee
         */
    }

    public void fromStationToCoordinates() {
        /* Trouver les stations alentours du point d'arrivee
         * Trouver le meilleur chemin depuis la station de depart vers la station d'arrivee trouvee
         * Calculer la distance entre le point d'arrivee et la station trouvee
         */
    }
}