package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Map.Entry;

import csv.CardsDataCsv;
import csv.ScheduleDataCsv;
import javafx.util.Pair;
import shortestpath.Dijkstra;
import shortestpath.graph.NodeSize;
import utils.Globals;

import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.time.LocalTime;

public class Network {
    /** Ensemble de stations du réseau. <p> Une HashMap avec le nom des stations pour clé, et les stations pour valeur.*/
    private HashMap<String, Station> stationsByName;
    /** Ensemble de stations du réseau. <p> Une HashMap avec les coordonnées des stations pour clé, et les stations pour valeur.*/
    private HashMap<Double, Station> stationsByCoordinates;
    /** Ensemble des lignes du réseau. <p> Une HashMap à deux dimension avec le nom et le variant des lignes pour clé, et une liste de station pour valeur */
    private HashMap<String, HashMap<String, ArrayList<Station>>> lines;

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
        var schedules = new HashMap<String, HashMap<String, HashMap<String, ArrayList<LocalTime>>>>();
        var csvSchedules = new ScheduleDataCsv().readCSVFile(java.nio.file.Path.of(scheduleFile));

        for (var item : csvSchedules) {
            schedules
                    .computeIfAbsent(item.getDepartStation(),
                            k -> new HashMap<String, HashMap<String, ArrayList<LocalTime>>>())
                    .computeIfAbsent(item.getLine(), k -> new HashMap<String, ArrayList<LocalTime>>())
                    .computeIfAbsent(item.getVariant(), k -> new ArrayList<LocalTime>());
            schedules.get(item.getDepartStation()).get(item.getLine()).get(item.getVariant()).add(item.getDepartTime());
        }

        var csvPaths = new CardsDataCsv().readCSVFile(java.nio.file.Path.of(mapFile));
        var stations = new HashMap<String, Station>();
        var paths = new ArrayList<Path>();
        for (CardsDataCsv item : csvPaths) {
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

            var schedule = schedules.getOrDefault(stationNameA, new HashMap<>())
                    .getOrDefault(item.getLine(), new HashMap<>())
                    .getOrDefault(item.getLineVariant(), new ArrayList<LocalTime>());

            paths.add(new Path(item.getLine(), item.getLineVariant(), schedule, item.getDuration(),
                    item.getDistance(), stations.get(stationNameA), stations.get(stationNameB)));
        }
        return new Network(new ArrayList<Station>(stations.values()), paths);
    }
    /**
     * Returns a mapping of each station to the list of lines passing by that station.
     * @return a HashMap with each station as key and the list of lines passing by that station as value
     */
    public  ArrayList<String> getLinesByStation(String stationName) {
        ArrayList<String> linesByStation = new ArrayList<>();
        Station station = getStation(stationName);
        for (Path p : station.getInPaths()) {
            if(!linesByStation.contains(p.getLineName())) {
                linesByStation.add(p.getLineName());
            }
        }
        for (Path p : station.getOutPaths()) {
            if(!linesByStation.contains(p.getLineName())){
                linesByStation.add(p.getLineName());

            }
        }
        return linesByStation;
    }

    /**
     * Getter
     * @return retourne la liste des lignes du réseau
     */
    public HashMap<String, HashMap<String, ArrayList<Station>>> getLines() {
        return lines;
    }
    /**
     * Getter
     * @return retourne la liste des stations par nom
     */
    public HashMap<String, Station> getStationsByName() {
        return stationsByName;
    }

    /**
     * Getter
     * @return retourne la liste des stations par coordonnées
     */
    public HashMap<Double, Station> getStationsByCoordinates() {
        return stationsByCoordinates;
    }


    /**
     * Renvoie la liste de station constituant le variant d'une ligne
     * @param name le nom de la ligne
     * @param variant le variant de la ligne
     * @throws NoSuchElementException si le nom ou le variant ne correspond à aucune des lignes ou variant du réseau
     * @return une liste de station
     */
    public ArrayList<Station> getLineVariant(String name, String variant) throws NoSuchElementException {
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
    public HashMap<String, ArrayList<Station>> getLine(String name) throws NoSuchElementException {
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

    public TreeMap<java.lang.Double, Station> getClosestStations(Double coordinates) {
        TreeMap<java.lang.Double, Station> map = new TreeMap<>();
        for (Station station : stationsByCoordinates.values()) {
            if (coordinates.equals(station.getCoordinates())) {
                continue;
            }
            double distanceX = Math.abs(coordinates.getX()) - Math.abs(station.getCoordinates().getX());
            double distanceY = Math.abs(coordinates.getY()) - Math.abs(station.getCoordinates().getY());
            double distance = Math.abs(Math.abs(distanceX) - Math.abs(distanceY));
            map.put(distance, station);
        }
        return map;
    }

    /**
     * Renvoie la derniere station du variant d'une ligne.
     * @param line le nom de la ligne
     * @param variant le nom du variant de la ligne
     * @return une station
     */
    public Station getEndTerminus(String line, String variant) {
        var list = getLineVariant(line, variant);
        return list.get(list.size() - 1);
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
        lines = new HashMap<String, HashMap<String, ArrayList<Station>>>();
        var lineList = pathList.stream()
                .map(p -> new Pair<String, String>(p.getLineName(), p.getVariant()))
                .distinct()
                .toList();
        for (Pair<String, String> pair : lineList) {
            var lineName = pair.getKey();
            var variant = pair.getValue();
            if (!lines.containsKey(lineName))
                lines.put(lineName, new HashMap<String, ArrayList<Station>>());
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
    private ArrayList<Station> initVariant(String name, String variant, ArrayList<Station> stationList) {
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

    public static void main(String[] args) throws IOException {
        Network network = Network.fromCSV(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
        Dijkstra dijkstra = new Dijkstra(network);
        Double start = new Double(2.346411849769496, 48.85955653272677);
        Double end = new Double(2.376487371168305, 48.829925765928905);
        Itinerary itinerary = dijkstra.bestPath(start, end, LocalTime.now(), NodeSize.TIME);
        System.out.println(itinerary);
    }
}