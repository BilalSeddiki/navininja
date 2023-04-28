package shortestpath;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
import java.util.List;

import javafx.util.Pair;
import javafx.util.Pair;
import model.Itinerary;
import model.Network;
import model.Path;
import model.Station;
import model.Transport;
import model.Walk;
import shortestpath.graph.Node;
import shortestpath.graph.NodeSize;

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
            if (visitedStations.contains(currentNode.getCoordinates())) {
                continue;
            }
            List<Transport> transportList = new ArrayList<>();
            transportList.addAll(network.getStation(currentNode.getCoordinates()).getOutPaths());
            if (walking) {
                List<Pair<java.lang.Double, Station>> list = network.getClosestStations(currentNode.getCoordinates());
                for (int i = 0; i < list.size(); i++) {
                    Walk walk = new Walk(currentNode.getCoordinates(), list.get(0).getValue().getCoordinates());
                    if (walk.getTravelDistance() > maxWalkingDistance) {
                        break;
                    }
                    transportList.add(walk);
                    list.remove(0);
                }
            }
            for (Transport path : transportList) {
                if (!isPathLegal(path, visitedStations, startTime)) {
                    continue;
                }
                Node adjacentNode = stationNodeMap.getOrDefault(path.getOutCoordinates(),
                        new Node(path.getOutCoordinates()));
                if (processAdjacentNode(path, currentNode, adjacentNode, size)) {
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
    
    private boolean isPathLegal(Transport transport, Set<Point2D.Double> visitedStations, LocalTime startTime) {
        if (visitedStations.contains(transport.getOutCoordinates())) {
            return false;
        }
        if (transport instanceof Path) {
            Path path = (Path) transport;
            /* List<LocalTime> schedule = path.getSchedule();
            for (LocalTime time : schedule) {
                if (time.isAfter(startTime)) {
                    return true;
                }
            } */
            if (path.nextDeparture(startTime).isPresent()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
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
        Optional<LocalTime> newTimeOpt = path.nextDeparture(currentNode.getTime().plus(path.getTravelDuration()));
        if (newTimeOpt.isEmpty())
            return false;
        var newTime = newTimeOpt.get();
        boolean better = false;
        switch (size) {
            case DISTANCE:
                better = newDistance <= adjacentNode.getDistance();
                break;
            case DURATION:
                better = newDuration.minus(adjacentNode.getDuration()).isNegative();
                break;
            case TIME:
                better = newTime.isBefore(adjacentNode.getTime()) || newTime.equals(newTime);
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

    @Override
    public Itinerary bestPath(Double startingCoordinates, Double endingCoordinates, LocalTime startTime, NodeSize size) {
        return null;
    }
}