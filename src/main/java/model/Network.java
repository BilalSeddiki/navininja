package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javafx.util.Pair;

import java.awt.geom.Point2D;

public class Network {

    /** Ensemble de stations du réseau. <p>Une HashMap
     * avec le nom des stations pour clé, et les
     * stations pour valeur.*/
    private Map<String, Station> stationsByName;

    /** Ensemble de stations du réseau. <p>Une HashMap
     * avec les coordonnées des stations pour clé, et
     * les stations pour valeur.*/
    private Map<Point2D.Double, Station> stationsByCoordinates;

    /** Ensemble des lignes du réseau. <p> Une HashMap à
     * deux dimension avec le nom et le variant des lignes
     * pour clé, et une liste de station pour valeur */
    private Map<String, Map<String, List<Station>>> lines;

    /**
     * Construit un réseau de station.
     * <p>
     * Le constructeur ajoute à chaque stations les chemins entrant et sortant.
     * @param stationList une liste de stations
     * @param pathList une liste de chemins entre les stations
     */
    public Network(final List<Station> stationList,
    final List<Path> pathList) {
        initStationByName(stationList);
        initStationByCoordinates(stationList);
        addPathsToStations(pathList);
        initLines(stationList, pathList);
    }

    /**
     * Returns a mapping of each station to the list of lines passing by that
     * station.
     * @param stationName une liste de stations
     * @return a List with each station as key and the list of lines passing by
     *         that station as value
     */
    public List<String> getLinesByStation(final String stationName) {
        List<String> linesByStation = new ArrayList<>();
        Station station = getStation(stationName);
        for (Path p : station.getInPaths()) {
            if (!linesByStation.contains(p.getLineName())) {
                linesByStation.add(p.getLineName());
            }
        }
        for (Path p : station.getOutPaths()) {
            if (!linesByStation.contains(p.getLineName())) {
                linesByStation.add(p.getLineName());

            }
        }
        return linesByStation;
    }

    /**
     * Getter.
     * @return retourne la liste des stations par nom
     */
    public Map<String, Station> getStationsByName() {
        return stationsByName;
    }

    /**
     * Getter.
     * @return retourne la liste des stations par coordonnées
     */
    public Map<Point2D.Double, Station> getStationsByCoordinates() {
        return stationsByCoordinates;
    }

    /**
     * Returns a mapping of each station to the list of lines passing by that
     * station.
     * @return a Map with each station as key and the list of lines passing by
     * that station as value
     */

    public Map<String, Map<String, List<Station>>> getLines() {
        return lines;
    }

    /**
     * Renvoie la liste de station constituant le variant d'une ligne.
     * @param name    le nom de la ligne
     * @param variant le variant de la ligne
     * @throws NoSuchElementException si le nom ou le variant ne correspond
     * à aucune des lignes ou variant du réseau
     * @return une liste de station
     */
    public List<Station> getLineVariant(final String name, final String variant)
        throws NoSuchElementException {
        var lineVariant = getLine(name).get(variant);
        if (lineVariant == null) {
            throw new NoSuchElementException();
        }
        return lineVariant;
    }

    /**
     * Renvoie l'ensemble des variant d'une ligne sous la forme d'un ensemle de
     * liste de stations.
     * @param name le nom de la ligne
     * @throws NoSuchElementException si le nom ne correspond à aucune des
     * lignes du réseau.
     * @return une Map avec pour clé le numéro des variants et pour valeur la
     * liste des stations de chaque variants
     */
    public Map<String, List<Station>> getLine(final String name)
        throws NoSuchElementException {
        var line = lines.get(name);
        if (line == null) {
            throw new NoSuchElementException();
        }
        return line;
    }

    /**
     * Vérifie l'existence d'une station dans le réseau.
     * @param name le nom d'une station
     * @return true si la station existe
     */
    public boolean hasStation(final String name) {
        return stationsByName.containsKey(name);
    }

    /**
     * Vérifie l'existence d'une station dans le réseau.
     * @param coordinates les coordonnées d'une station
     * @return true si la station existe
     */
    public boolean hasStation(final Point2D.Double coordinates) {
        return stationsByCoordinates.containsKey(coordinates);
    }

    /**
     * Renvoie une station du réseau à partir d'un nom.
     * <p>
     * L'existence de la station dans le réseau doit être vérifié avant appel de
     * cette fonction.
     * @param name le nom d'une station
     * @return la station
     * @throws NoSuchElementException si la station n'existe pas dans le réseau
     */
    public Station getStation(final String name) throws NoSuchElementException {
        var station = stationsByName.get(name);
        if (station == null) {
            throw new NoSuchElementException();
        }
        return station;
    }

    /**
     * Renvoie une station du réseau à partir de coordonnées.
     * <p>
     * L'existence de la station dans le réseau doit être vérifié avant appel de
     * cette fonction.
     * @param coordinates les coordonnées d'une station
     * @return la station
     * @throws NoSuchElementException si la station n'existe pas dans le réseau
     */
    public Station getStation(final Point2D.Double coordinates)
        throws NoSuchElementException {
        var station = stationsByCoordinates.get(coordinates);
        if (station == null) {
            throw new NoSuchElementException();
        }
        return station;
    }

    /**
     * Renvoie la liste des stations du réseau les plus proches des coordonnées.
     * @param coordinates les coordonnées
     * @return une liste de stations trié par leur proximité aux coordonnées
     */
    public final List<Station> getClosestStations(
        final Point2D.Double coordinates) {
        List<Station> list = new ArrayList<>();
        list.addAll(stationsByCoordinates.values());
        list.removeIf(t -> t.getCoordinates().equals(coordinates));
        list.sort(new Comparator<Station>() {

            private double getDistance(final Station station) {
                return Math.sqrt(
                    Math.pow(
                        station.getCoordinates().getX() - coordinates.getX(), 2)
                    + Math.pow(
                        station.getCoordinates().getY() - coordinates.getY(), 2)
                );
            }

            @Override
            public int compare(final Station o1, final Station o2) {
                return Double.compare(getDistance(o1), getDistance(o2));
            }

        });
        return list;
    }

    /**
     * Renvoie la derniere station du variant d'une ligne.
     * @param line    le nom de la ligne
     * @param variant le nom du variant de la ligne
     * @return une station
     */
    public Station getEndTerminus(final String line, final String variant) {
        var list = getLineVariant(line, variant);
        return list.get(list.size() - 1);
    }

    /**
     * Ajoute à chaque station ses chemins entrant et sortant.
     * <p>
     * Cette méthode doit être utilisée après avoir initialisé stationByName
     * @param pathList la liste des chemins
     */
    private void addPathsToStations(final List<Path> pathList) {
        pathList.stream().forEach(p -> {
            stationsByName.get(p.getSource().getName()).addOutPath(p);
            stationsByName.get(p.getDestination().getName()).addInPath(p);
        });
    }

    /**
     * Initialise l'attribut stationByName.
     * @param stationList la liste des stations
     */
    private void initStationByName(final List<Station> stationList) {
        stationsByName = new HashMap<>();
        stationList.forEach(s -> stationsByName.put(s.getName(), s));
    }

    /**
     * Initialise l'attribut stationByCoordinates.
     * @param stationList la liste des stations
     */
    private void initStationByCoordinates(final List<Station> stationList) {
        stationsByCoordinates = new HashMap<>();
        stationList.forEach(s ->
            stationsByCoordinates.put(s.getCoordinates(), s));
    }

    /**
     * Initialise l'attribut lines.
     * <p>
     * Cette méthode doit être utilisée après avoir ajouter les chemins aux
     * stations via addPathToStations.
     * @param stationList la liste des stations
     * @param pathList    la liste des chemins
     */
    private void initLines(final List<Station> stationList,
    final List<Path> pathList) {
        lines = new HashMap<>();
        List<Pair<String, String>> lineList = pathList.stream().map(p ->
            new Pair<String, String>(p.getLineName(),
            p.getVariant())).distinct().toList();
        for (Pair<String, String> pair : lineList) {
            String lineName = pair.getKey();
            String variant = pair.getValue();
            lines.computeIfAbsent(lineName, k -> new HashMap<>());
            lines.get(lineName).put(variant,
                initVariant(lineName, variant, stationList));

        }
    }

    /**
     * Initialise le variant d'une ligne.
     * <p>
     * Cette méthode doit être utilisée après avoir ajouter les chemins aux
     * stations via addPathToStations
     * @param name        le nom de la ligne
     * @param variant     le numéro de variant de la ligne
     * @param stationList la liste des stations
     * @return une liste de stations
     */
    private List<Station> initVariant(final String name,
        final String variant, final List<Station> stationList) {
        var stationFromLine = stationList.stream().filter(s ->
            s.getInPath(name, variant).isPresent()
                || s.getOutPath(name, variant).isPresent())
            .findFirst().orElseThrow(NoSuchElementException::new);
        var line = new ArrayList<Station>();
        line.add(stationFromLine);
        var previousPath = stationFromLine.getInPath(name, variant);
        while (previousPath.isPresent()) {
            line.add(0, previousPath.get().getSource());
            previousPath =
                previousPath.get().getSource().getInPath(name, variant);
        }
        var nextPath = stationFromLine.getOutPath(name, variant);
        while (nextPath.isPresent()) {
            line.add(nextPath.get().getDestination());
            nextPath =
                nextPath.get().getDestination().getOutPath(name, variant);
        }
        return line;
    }

    @Override
    public final boolean equals(final Object arg0) {
        return arg0 instanceof Network n
            && this.stationsByName.equals(n.stationsByName)
            && this.stationsByCoordinates.equals(n.stationsByCoordinates)
            && this.lines.equals(n.lines);
    }
}
