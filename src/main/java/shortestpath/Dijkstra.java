package shortestpath;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import csv.CsvData;
import model.Itinerary;
import model.Network;
import model.Path;
import model.Station;
import model.Transport;
import model.Walk;
import shortestpath.graph.Node;
import shortestpath.graph.NodeSize;
import utils.Globals;

public class Dijkstra extends ShortestPathAlgorithm {

    public Dijkstra(Network network) {  
        super(network);
    }

    @Override
    public Itinerary bestPath(Station source, Station destination, LocalTime startTime, NodeSize size, boolean walking) {
        Comparator<? super Node> comparator = size.getComparator();
        PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
        Set<Point2D.Double> visitedStations = new HashSet<>();
        Map<Point2D.Double, Node> stationNodeMap = new HashMap<>();

        Node initialNode = new Node(source.getCoordinates(), 0, Duration.ZERO, startTime);
        stationNodeMap.put(source.getCoordinates(), initialNode);
        queue.add(initialNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if (visitedStations.contains(currentNode.getCoordinates()) || currentNode.getCoordinates().equals(destination.getCoordinates())) {
                continue;
            }
            List<Transport> transportList = getTransportList(currentNode, walking, destination);
            for (Transport path : transportList) {
                if (!isPathLegal(currentNode, path, visitedStations, startTime, destination.getCoordinates())) {
                    continue;
                }
                Node adjacentNode = stationNodeMap.getOrDefault(path.getOutCoordinates(),
                        new Node(path.getOutCoordinates()));
                if (path.getOutCoordinates().equals(destination.getCoordinates())) {
                    System.out.println(path);
                    System.out.println(adjacentNode.getTime());
                }
                if (processAdjacentNode(path, currentNode, adjacentNode, size)) {
                    if (path.getOutCoordinates().equals(destination.getCoordinates())) {
                        System.out.println(path);
                        System.out.println(adjacentNode.getTime());
                    }
                    stationNodeMap.put(path.getOutCoordinates(), adjacentNode);
                }
                queue.add(adjacentNode);
            }
            visitedStations.add(currentNode.getCoordinates());
        }
        if (!stationNodeMap.containsKey(destination.getCoordinates())) {
            throw new IllegalArgumentException("Aucun chemin n'a été trouvé.");
        }
        return new Itinerary(startTime, stationNodeMap.get(destination.getCoordinates()).getShortestPath());
    }

    private boolean isPathLegal(Node currentNode, Transport transport, Set<Point2D.Double> visitedStations, LocalTime startTime, Point2D.Double destination) {
        if (visitedStations.contains(transport.getOutCoordinates())) {
            return false;
        }
        if (transport instanceof Path) {
            Path path = (Path) transport;
            if (path.nextDeparture(startTime).isPresent()) {
                return true;
            }
        } 
        else if (transport instanceof Walk) {
            return !transport.getOutCoordinates().equals(destination) && (currentNode.getShortestPath().isEmpty() || (!currentNode.getShortestPath().isEmpty() && currentNode.getShortestPath().get(currentNode.getShortestPath().size() - 1) instanceof Path));
            /* if (currentNode.getShortestPath().isEmpty()) {
                return true;
            }
            return currentNode.getShortestPath().get(currentNode.getShortestPath().size() - 1) instanceof Path; */
            /* return true; */
        }
        return false;
    }

    private List<Transport> getTransportList(Node currentNode, boolean walking, Station destination) {
        List<Transport> transportList = new ArrayList<>();
        if (network.hasStation(currentNode.getCoordinates())) {
            transportList.addAll(network.getStation(currentNode.getCoordinates()).getOutPaths());
        }
        if (walking || !network.hasStation(currentNode.getCoordinates())) {
            List<Station> list = network.getClosestStations(currentNode.getCoordinates());
            for (Station pair : list) {
                Walk walk = new Walk(currentNode.getCoordinates(), pair.getCoordinates());
                if (walk.getTravelDuration().compareTo(Duration.ofMinutes(10)) > 0 && !transportList.isEmpty()) {
                    break;
                }
                transportList.add(walk);
            }
        }
        if (transportList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return transportList;
    }

    /**
     * Vérifie si la node adjacente a un meilleur poids avec la node actuelle comme voisin. Si vrai, mets à jour ses valeurs.
     * @param path
     * @param currentNode
     * @param adjacentNode
     * @param size
     * @return true si la node adjacente est meilleure que précédemment
     */
    private boolean processAdjacentNode(Transport path, Node currentNode, Node adjacentNode, NodeSize size) { 
        double newDistance = currentNode.getDistance() + path.getTravelDistance();
        Duration newDuration = currentNode.getDuration().plus(path.getTravelDuration());
        LocalTime newTime = path.nextDeparture(currentNode.getTime().plus(path.getTravelDuration())).get();
        boolean better = false;
        switch (size) {
            case DISTANCE:
                better = newDistance <= adjacentNode.getDistance();
                break;
            case DURATION:
                better = newDuration.minus(adjacentNode.getDuration()).isNegative();
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
        }
        return better;
    }

    public static void main(String[] args) throws IOException {
        Network network = CsvData.makeNetwork(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
        Dijkstra dijkstra = new Dijkstra(network);
        Itinerary itinerary = dijkstra.bestPath(network.getStation("Châtelet"), network.getStation("Bibliothèque François Mitterrand"), LocalTime.of(10, 10, 10), NodeSize.TIME, true);
        System.out.println(itinerary);
    }
}