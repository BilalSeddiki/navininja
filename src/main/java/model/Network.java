package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import csv.CardsDataCsv;
import javafx.util.Pair;
import model.dijkstra.Node;
import model.dijkstra.NodeDistanceComparator;
import model.dijkstra.NodeDistanceDurationComparator;
import model.dijkstra.NodeDurationComparator;
import model.dijkstra.NodeTimeComparator;

import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.time.Duration;
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
    public static Network fromCSV() throws IOException {
        var list = new CardsDataCsv().readCSVFile();
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

    /**
     * Calcule un itinéraire d'une station à une autre de manière naïve.
     * @param source la station de départ
     * @param destination la station d'arrivée
     * @return un itinéraire d'une station à une autre
     */
    public Itinerary naivePath(Station source, Station destination) {
        return null;
    }

    public enum DijkstraComparator {

        /** Voyage qui parcourt le moins de distance */
        DISTANCE,

        /** Voyage avec le moins de temps dans les transports */
        DURATION,

        /** Voyage qui parcourt le moins de distance et avec le moins de temps dans les transports */
        DISTANCE_PLUS_DURATION,

        /** Voyage qui prend le moins de temps */
        TIME
    }

    /**
     * Calcule le meilleur itinéraire d'une station à une autre.
     * @param source la station de départ
     * @param destination la station d'arrivée
     * @return un itinéraire d'une station à une autre
     */
    public Itinerary bestPath(Station source, Station destination, LocalTime startTime, DijkstraComparator valueToCompare) {
        Comparator<? super Node> comparator = null;
        switch (valueToCompare) {
            case DISTANCE:
                comparator = new NodeDistanceComparator();
                break;
            case DURATION:
                comparator = new NodeDurationComparator();
                break;
            case DISTANCE_PLUS_DURATION:
                comparator = new NodeDistanceDurationComparator();
                break;
            case TIME:
                comparator = new NodeTimeComparator();
        }
        PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
        Set<String> visitedStations = new HashSet<>();
        Map<Station, Node> stationNodeMap = new HashMap<>();
        
        Node initialNode = new Node(source, 0, Duration.ZERO, startTime);
        stationNodeMap.put(source, initialNode);
        queue.add(initialNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if (visitedStations.contains(currentNode.getStation().getName())) {
                System.out.println("Node déjà visitée = " + currentNode.getStation().getName());
                continue;
            }
            System.out.println("Node actuelle = " + currentNode.getStation().getName());
            for (Path path : currentNode.getStation().getOutPaths()) {
                if (visitedStations.contains(path.getDestination().getName())) {
                    continue;
                }
                Node adjacentNode = stationNodeMap.getOrDefault(
                    path.getDestination(),
                    new Node(path.getDestination())
                );
                double newDistance = currentNode.getDistance() + path.getTravelDistance();
                Duration newDuration = currentNode.getDuration().plus(path.getTravelDuration());
                LocalTime newTime = path.nextTrainDeparture(currentNode.getTime()).plus(path.getTravelDuration());
                boolean better = false;
                switch (valueToCompare) {
                    case DISTANCE:
                        better = newDistance <= adjacentNode.getDistance();
                        break;
                    case DURATION:
                        better = newDuration.minus(adjacentNode.getDuration()).isNegative();
                        break;
                    case DISTANCE_PLUS_DURATION:
                        better = newDistance + newDuration.toSeconds() <= adjacentNode.getDistance() + adjacentNode.getDuration().toSeconds();
                        break;
                    case TIME:
                        better = newTime.isBefore(adjacentNode.getTime());
                        break;
                }

                if (better) {
                    adjacentNode.setDistance(newDistance);
                    adjacentNode.setDuration(newDuration);
                    adjacentNode.setTime(newTime);
                    adjacentNode.setShortestPath(currentNode, path);
                    stationNodeMap.put(path.getDestination(), adjacentNode);
                    System.out.println("Optimal = " + currentNode.getStation() + " -> " + adjacentNode.getStation());
                }
                else {
                    System.out.println("Pas optimal = " + currentNode.getStation() + " -> " + adjacentNode.getStation());
                }
                queue.add(adjacentNode);
            }
            visitedStations.add(currentNode.getStation().getName());
        }
        System.out.println(stationNodeMap.get(destination).getDistance() + " km");
        return new Itinerary(null, stationNodeMap.get(destination).getShortestPath());
    }

    public static void main(String[] args) {
        Station station1 = new Station("1", null);
        Station station2 = new Station("2", null);
        Station station3 = new Station("3", null);
        Station station4 = new Station("4", null);
        Station station5 = new Station("5", null);
        Station station6 = new Station("6", null);
        ArrayList<Station> stations = new ArrayList<>(Arrays.asList(station1, station2, station3, station4, station5, station6));
        ArrayList<Path> paths = new ArrayList<>(Arrays.asList(
            new Path("", 0, null, null, 7, station1, station2),
            new Path("", 0, null, null, 9, station1, station3),
            new Path("", 0, null, null, 14, station1, station6),
            new Path("", 0, null, null, 10, station2, station3),
            new Path("", 0, null, null, 15, station2, station4),
            new Path("", 0, null, null, 11, station3, station4),
            new Path("", 0, null, null, 2, station3, station6),
            new Path("", 0, null, null, 6, station5, station4),
            new Path("", 0, null, null, 9, station6, station5)
        ));
        Network network = new Network(stations, paths);
        Itinerary itinerary = network.bestPath(station1, station5, DijkstraComparator.DISTANCE);
        for (Path path : itinerary.getPaths()) {
            System.out.println(path.getSource().getName() + " -> " + path.getDestination().getName()); 
        }
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
        Network network = Network.fromCSV();
        network.bestPath(network.getStation("Lourmel"), network.getStation("Filles du Calvaire"), LocalTime.of(8, 0, 0), DijkstraComparator.DURATION);
        /* Station station1 = new Station("1", null);
        Station station2 = new Station("2", null);
        Station station3 = new Station("3", null);
        Station station4 = new Station("4", null);
        Station station5 = new Station("5", null);
        Station station6 = new Station("6", null);
        ArrayList<Station> stations = new ArrayList<>(Arrays.asList(station1, station2, station3, station4, station5, station6));
        ArrayList<Path> paths = new ArrayList<>(Arrays.asList(
            new Path("", "", null, Duration.ofSeconds(7), 7, station1, station2),
            new Path("", "", null, Duration.ofSeconds(9), 9, station1, station3),
            new Path("", "", null, Duration.ofSeconds(14), 14, station1, station6),
            new Path("", "", null, Duration.ofSeconds(10), 10, station2, station3),
            new Path("", "", null, Duration.ofSeconds(15), 15, station2, station4),
            new Path("", "", null, Duration.ofSeconds(11), 11, station3, station4),
            new Path("", "", null, Duration.ofSeconds(2), 2, station3, station6),
            new Path("", "", null, Duration.ofSeconds(6), 6, station5, station4),
            new Path("", "", null, Duration.ofSeconds(9), 9, station6, station5)
        ));
        Network network = new Network(stations, paths);
        Itinerary itinerary = network.bestPath(station1, station5, LocalTime.of(12, 00), DijkstraComparator.DURATION); */
    }
}