package model;

import java.util.ArrayList;
import java.awt.geom.Point2D.Double;

/** Une station du réseau */
public class Station {
    /** Nom de la station. */
    private String name;
    /** Coordonnées gps de la station. */
    private Double coordinates;
    /** Differents chemins que l'on peut prendre à partir de la station. */
    private ArrayList<Path> paths;

    /**
     * Construit une sation du réseau
     * @param name le nom de la station
     * @param coordinates les coordonnées gps de la station 
     * @param paths les differents chemins que l'on peut prendre à partir de la station
     */
    public Station(String name, Double coordinates, ArrayList<Path> paths) {
        this.name = name;
        this.coordinates = coordinates;
        this.paths = paths;
    }

    /**
     * Renvoie le nom de la station.
     * @return le nom de la station
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie les coordonnées gps de la station.
     * @return des coordonnées 2D
     */
    public Double getCoordinates() {
        return coordinates;
    }

    /**
     * Renvoie les differents chemins que l'on peut prendre à partir de la station.
     * @return une liste de chemin
     */
    public ArrayList<Path> getPaths() {
        return paths;
    }
    
}
