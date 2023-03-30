package model;

import csv.CardsDataCsv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javafx.util.Pair;

import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.time.LocalTime;

/* TODO: Changer l'initialisation de lines après l'implémentation des horaires */
/** Un réseau de stations. */
public class Network {
    /** Ensemble de stations du réseau. <p> Une HashMap avec le nom des stations pour clé, et les stations pour valeur.*/
    private HashMap<String, Station> stationsByName;
    /** Ensemble de stations du réseau. <p> Une HashMap avec les coordonnées des stations pour clé, et les stations pour valeur.*/
    private HashMap<Double, Station> stationsByCoordinates;
    /** Ensemble des lignes du réseau. <p> Une HashMap à deux dimension avec le nom et le variant des lignes pour clé, et une liste de station pour valeur */
    private HashMap<String, HashMap<Character, ArrayList<Station>>> lines;

    /**
     * Construit un réseau de station.
     * <p>
     * Le constructeur ajoute à chaque stations les chemins entrant et sortant.
     * @param stationList une liste de stations
     * @param pathList une liste de chemins entre les stations
     */
    public Network(ArrayList<Station> stationList, ArrayList<Path> pathList) {
        initStationByName(stationList);
        initStationByCoordinates(stationList);
        addPathsToStations(pathList);
        initLines(stationList, pathList);
    }

    /**
     * Crée un réseau à partir de deux fichiers CSV.
     * @param mapFile le nom d'un fichier CSV contenant les informations des chemins du réseau
     * @param scheduleFile le nom d'un fichier CSV contenant les informations des horaires des lignes 
     * @return un réseau
     * @throws IOException si la lecture d'un des fichier echoue
     */
    public static Network fromCSV(String mapFile, String scheduleFile) throws IOException {
        var list = new CardsDataCsv().readCSVFile(java.nio.file.Path.of(mapFile));
        var stations = new HashMap<String, Station>();
        var paths = new ArrayList<Path>();
        for (CardsDataCsv item : list) {
            String stationNameA = item.getStationA();
            if (!stations.containsKey(stationNameA)) {
                Double coordinatesA = item.getCoordinatesA();
                Station stationA = new Station(stationNameA, coordinatesA);
                stations.put(stationA.getName(), stationA);
            }

            String stationNameB = item.getStationB();
            if (!stations.containsKey(stationNameB)) {
                Double coordinatesB = item.getCoordinatesB();
                Station stationB = new Station(stationNameB, coordinatesB);
                stations.put(stationB.getName(), stationB);
            }

            paths.add(new Path(item.getLine(), item.getLineVariant(), new ArrayList<LocalTime>(), item.getDuration(),
                    item.getDistance(), stations.get(stationNameA), stations.get(stationNameB)));
        }
        return new Network(new ArrayList<Station>(stations.values()), paths);
    }

    /**
     * Renvoie la liste de station constituant le variant d'une ligne
     * @param name le nom de la ligne
     * @param variant le variant de la ligne
     * @throws NoSuchElementException si le nom ou le variant ne correspond à aucune des lignes ou variant du réseau
     * @return une liste de station
     */
    public ArrayList<Station> getLineVariant(String name, char variant) throws NoSuchElementException {
        var lineVariant = getLine(name).get(variant);
        if (lineVariant == null)
            throw new NoSuchElementException();
        return lineVariant;
    }

    /**
     * Renvoie l'ensemble des variant d'une ligne sous la forme d'un ensemle de liste de stations
     * @param name le nom de la ligne
     * @throws NoSuchElementException si le nom ne correspond à aucune des lignes du réseau
     * @return une HashMap avec pour clé le numéro des variants et pour valeur la liste des stations de chaque variants
     */
    public HashMap<Character, ArrayList<Station>> getLine(String name) throws NoSuchElementException {
        var line = lines.get(name);
        if (line == null)
            throw new NoSuchElementException();
        return line;
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
     * @param coordinates les coordonnées d'une station
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
        var station = stationsByName.get(name);
        if (station == null)
            throw new NoSuchElementException();
        return station;
    }

    /**
     * Renvoie une station du réseau à partir de coordonnées.
     * <p>
     * L'existence de la station dans le réseau doit être vérifié avant appel de cette fonction.
     * @param coordinates les coordonnées d'une station
     * @return la station
     * @throws NoSuchElementException si la station n'existe pas dans le réseau
     */
    public Station getStation(Double coordinates) throws NoSuchElementException {
        var station = stationsByCoordinates.get(coordinates);
        if (station == null)
            throw new NoSuchElementException();
        return station;
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

    /**
     * Ajoute à chaque station ses chemins entrant et sortant
     * <p>
     * Cette méthode doit être utilisée après avoir initialisé stationByName
     * @param pathList la liste des chemins
     */
    private void addPathsToStations(ArrayList<Path> pathList) {
        pathList.stream().forEach(p -> {
            stationsByName.get(p.getSource().getName()).addOutPath(p);
            stationsByName.get(p.getDestination().getName()).addInPath(p);
        });
    }

    /**
     * Initialise l'attribut stationByName
     * @param stationList la liste des stations
     */
    private void initStationByName(ArrayList<Station> stationList) {
        stationsByName = new HashMap<String, Station>();
        stationList.forEach(s -> {
            stationsByName.put(s.getName(), s);
        });
    }

    /**
     * Initialise l'attribut stationByCoordinates
     * @param stationList la liste des stations
     */
    private void initStationByCoordinates(ArrayList<Station> stationList) {
        stationsByCoordinates = new HashMap<Double, Station>();
        stationList.forEach(s -> {
            stationsByCoordinates.put(s.getCoordinates(), s);
        });
    }

    /**
     * Initialise l'attribut lines
     * <p>
     * Cette méthode doit être utilisée après avoir ajouter les chemins aux stations via addPathToStations
     * @param stationList la liste des stations
     * @param pathList la liste des chemins
     */
    private void initLines(ArrayList<Station> stationList, ArrayList<Path> pathList) {
        lines = new HashMap<String, HashMap<Character, ArrayList<Station>>>();
        var lineList = pathList.stream()
                .map(p -> new Pair<String, Character>(p.getLineName(), p.getVariant()))
                .distinct()
                .toList();
        for (Pair<String, Character> pair : lineList) {
            var lineName = pair.getKey();
            var variant = pair.getValue();
            if (!lines.containsKey(lineName))
                lines.put(lineName, new HashMap<Character, ArrayList<Station>>());
            lines.get(lineName).put(variant, initVariant(lineName, variant, stationList));

        }
    }

    /**
     * Initialise le variant d'une ligne
     * <p>
     * Cette méthode doit être utilisée après avoir ajouter les chemins aux stations via addPathToStations
     * @param name le nom de la ligne
     * @param variant le numéro de variant de la ligne
     * @param stationList la liste des stations
     * @return une liste de stations
     */
    private ArrayList<Station> initVariant(String name, char variant, ArrayList<Station> stationList) {
        var stationFromLine = stationList.stream()
                .filter(s -> s.getInPath(name, variant).isPresent() || s.getOutPath(name, variant).isPresent())
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException());
        var line = new ArrayList<Station>();
        line.add(stationFromLine);
        var previousPath = stationFromLine.getInPath(name, variant);
        while (previousPath.isPresent()) {
            line.add(0, previousPath.get().getSource());
            previousPath = previousPath.get().getSource().getInPath(name, variant);
        }
        var nextPath = stationFromLine.getOutPath(name, variant);
        while (nextPath.isPresent()) {
            line.add(nextPath.get().getDestination());
            nextPath = nextPath.get().getDestination().getOutPath(name, variant);
        }
        return line;
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Network n &&
                this.stationsByName.equals(n.stationsByName) &&
                this.stationsByCoordinates.equals(n.stationsByCoordinates) &&
                this.lines.equals(n.lines);
    }

}
