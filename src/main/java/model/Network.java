package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.awt.geom.Point2D.Double;

/** Un réseau de stations. */
public class Network {
    /** Ensemble de stations du réseau. <p> Une HashMap avec le nom des stations pour clé, et les stations pour valeur.*/
    private HashMap<String, Station> stationsByName;
    /** Ensemble de stations du réseau. <p> Une HashMap avec les coordonnées des stations pour clé, et les stations pour valeur.*/
    private HashMap<Double, Station> stationsByCoordinates;

    /**
     * Construit un réseau de station .
     * @param stationList une liste de stations
     */
    public Network(ArrayList<Station> stationList) {
        stationsByName = new HashMap<String, Station>();
        stationList.forEach(s -> {
            stationsByName.put(s.getName(), s); 
            stationsByCoordinates.put(s.getCoordinates(), s);
        });
    }

    /**
     * Vérifie l'existence d'une station dans le réseau.
     * @param name le nom d'une station
     * @return true si la station existe
     */
    public boolean hasStation(String name) {
        return stationsByName.containsKey(name);
    }

    /**
     * Vérifie l'existence d'une station dans le réseau.
     * @param name les coordonnées d'une station
     * @return true si la station existe
     */
    public boolean hasStation(Double coordinates) {
        return stationsByCoordinates.containsKey(coordinates);
    }

    /**
     * Renvoie une station du réseau à partir d'un nom.
     * <p>
     * L'existence de la station dans le réseau doit être vérifié avant appel de cette fonction.
     * @param name le nom d'une station
     * @return la station
     * @throws NoSuchElementException si la station n'existe pas dans le réseau
     */
    public Station getStation(String name) throws NoSuchElementException {
        return null;
    }

    /**
     * Renvoie une station du réseau à partir d'un nom.
     * <p>
     * L'existence de la station dans le réseau doit être vérifié avant appel de cette fonction.
     * @param name les coordonnées d'une station
     * @return la station
     * @throws NoSuchElementException si la station n'existe pas dans le réseau
     */
    public Station getStation(Double coordinates) throws NoSuchElementException {
        return null;
    }

    /**
     * Calcule le meilleur itinéraire d'une station à une autre.
     * @param source la station de départ
     * @param destination la station d'arrivée
     * @return un itinéraire d'une station à une autre
     */
    public Itinerary bestPath(Station source, Station destination) {
        return null;
    }
}

