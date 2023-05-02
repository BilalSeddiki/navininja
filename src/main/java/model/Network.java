package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.util.List;

import javafx.util.Pair;
import model.dijkstra.Node;
import model.dijkstra.NodeDistanceComparator;
import model.dijkstra.NodeDistanceDurationComparator;
import model.dijkstra.NodeDurationComparator;
import model.dijkstra.NodeTimeComparator;
import network.MinimalStation;
import utils.Globals;

import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
     * Returns a mapping of each station to the list of lines passing by that station.
     * @return a HashMap with each station as key and the list of lines passing by that station as value
     */

    public HashMap<String, HashMap<String, ArrayList<Station>>> getLines() {
        return lines;
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

    public List<Pair<java.lang.Double, Station>> getClosestStations(Double coordinates) {
        List<Pair<java.lang.Double, Station>> list = new ArrayList<>();
        for (Station station : stationsByCoordinates.values()) {
            if (station.getCoordinates().equals(coordinates)) {
                continue;
            }
            double distance = Math.sqrt(
                Math.pow(station.getCoordinates().getX() - coordinates.getX(), 2) + 
                Math.pow(station.getCoordinates().getX() - coordinates.getX(), 2));
            list.add(new Pair<>(distance, station));
        }
        list.sort(new Comparator<Pair<java.lang.Double, Station>>() {

            @Override
            public int compare(Pair<java.lang.Double, Station> o1, Pair<java.lang.Double, Station> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
            
        });
        return list;
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
     * necessite un refactor
     * <p>
     * renvoie la liste des schedule de la station pour une ligne a l'horaire donnée
     * @param name le nom de la station
     * @param line la ligne a la station
     * @param time l'horaire donnée
     * @return ArrayList<Schedule> qui contient les Schedules demandés
     */
    public ArrayList<Schedule> traitementSchedule(String name,String line,LocalTime time){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<Schedule> schedulestmp=new ArrayList<Schedule>();
        if(hasStation(name)){
            List<Path> paths = getStation(name).getOutPathsFromLine(line);
            for (Path p: paths ){
                for(LocalTime lt : p.getSchedule()){
                    if(lt.isAfter(time))
                        schedulestmp.add(new Schedule(getEndTerminus(line, p.getVariant()).getName()  , lt.format(dtf)));
                }
            }
        }
        return schedulestmp;

    }
    /**
     * Renvoie une liste de MinimalStation qui contient les noms des stations et des lignes qui y passent
     * @return ArrayList<MinimalStation> 
     */
    public ArrayList<MinimalStation> allStation(){
        Set<String> a=stationsByName.keySet();
        ArrayList<MinimalStation> ret=new ArrayList<MinimalStation>();
        for( var s:a){
            ArrayList<String> tmp=getLinesByStation(s);
            ret.add(new MinimalStation(s,tmp));
        }
        return ret;
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
}