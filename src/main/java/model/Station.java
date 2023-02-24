package model;

import java.util.ArrayList;
import java.awt.geom.Point2D.Double;

/** Une station du réseau */
public class Station {
    /** Le nom de la station. */
    private String name;
    /** Les coordonnées gps de la station. */
    private Double coordinates;
    /** Les differents chemins que l'on peut prendre à partir de la station. */
    private ArrayList<Path> paths;

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
