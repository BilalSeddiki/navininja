package model;

import java.util.ArrayList;
import java.util.Optional;
import java.awt.geom.Point2D.Double;
import java.time.LocalTime;


/** Une station du réseau. */
public class Station {
    /** Nom de la station. */
    private String name;
    /** Coordonnées gps de la station. */
    private Double coordinates;
    /** Differents chemins que l'on peut prendre à partir de la station. */
    private ArrayList<Path> outPaths;
    /** Differents chemins qui arrivent à la station. */
    private ArrayList<Path> inPaths;

    /**
     * Construit une sation du réseau
     * @param name le nom de la station
     * @param coordinates les coordonnées gps de la station 
     */
    public Station(String name, Double coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.inPaths = new ArrayList<Path>();
        this.outPaths = new ArrayList<Path>();
    }

    /**
     * Ajoute un chemin que l'on peut prendre à partir de la station.
     * <p>
     * Le chemin doit avoir cette station comme attribut source.
     * @param path un chemin qui part de cette station
     */
    public void addOutPath(Path path) {
        if (path.getSource() != this)
            throw new IllegalArgumentException(
                    "The path should start from this station " + this + " but instead starts from " + path.getSource());
        outPaths.add(path);
    }

    /**
     * Ajoute un chemin qui arrive à la station.
     * <p>
     * Le chemin doit avoir cette station comme attribut destination.
     * @param path un chemin qui arrive à cette station
     */
    public void addInPath(Path path) {
        if (path.getDestination() != this)
            throw new IllegalArgumentException(
                    "The path should lead to this station " + this + " but instead leads to " + path.getDestination());
        inPaths.add(path);
    }
    public void setTerminus(String nom,Integer variant,ArrayList<LocalTime> schedule){
        Optional<Path> tmp=outPaths.stream()
                .filter(path -> path.getLineName().equals(nom) && path.getVariant() == variant)
                .findFirst();
        tmp.get().setTerminus(schedule);
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
     * @return une liste de chemins
     */
    public ArrayList<Path> getOutPaths() {
        return outPaths;
    }

    /**
     * Renvoie les différents chemins qui arrivent à la station.
     * @return une liste de chemins
     */
    public ArrayList<Path> getInPaths() {
        return inPaths;
    }

    /**
     * Renvoie le chemin partant de la station avec le nom de ligne et le variant spécifié, si celui-ci existe.
     * @param lineName le nom de de la ligne du chemin
     * @param variant le variant de la ligne du chemin
     * @return un Optional du chemin si celui-ci existe, sinon un Optional vide
     */
    public Optional<Path> getOutPath(String lineName, char variant) {
        return outPaths.stream()
                .filter(path -> path.getLineName().equals(lineName) && path.getVariant() == variant)
                .findFirst();
    }

    /**
    * Renvoie le chemin arrivant à station avec le nom de ligne et le variant spécifié, si celui-ci existe.
    * @param lineName le nom de de la ligne du chemin
    * @param variant le variant de la ligne du chemin
    * @return un Optional du chemin si celui-ci existe, sinon un Optional vide
    */
    public Optional<Path> getInPath(String lineName, char variant) {
        return inPaths.stream()
                .filter(path -> path.getLineName().equals(lineName) && path.getVariant() == variant)
                .findFirst();
    }

    /**
     * Renvoie une chaine de charactères représentant la station
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Station s &&
                this.name.equals(s.name) &&
                this.coordinates.equals(s.coordinates) &&
                this.inPaths.containsAll(s.inPaths) &&
                s.inPaths.containsAll(this.inPaths) &&
                this.outPaths.containsAll(s.outPaths) &&
                s.outPaths.containsAll(this.outPaths);
    }

    public boolean equalsNonRecursive(Object arg0) {
        return arg0 instanceof Station s &&
                this.name.equals(s.name) &&
                this.coordinates.equals(s.coordinates);
    }
}