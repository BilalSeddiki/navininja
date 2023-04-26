package shortestpath;

import java.awt.geom.Point2D.Double;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import model.Itinerary;
import model.Network;
import model.Path;
import model.Station;
import shortestpath.graph.Node;
import shortestpath.graph.NodeSize;

public class Dijkstra extends ShortestPathAlgorithm {

    public Dijkstra(Network network) {
        super(network);
    }

    @Override
    public Itinerary bestPath(Station source, Station destination, LocalTime startTime, NodeSize size) {
        Comparator<? super Node> comparator = size.getComparator();
        PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
        Set<String> visitedStations = new HashSet<>();
        Map<Station, Node> stationNodeMap = new HashMap<>();

        Node initialNode = new Node(source, 0, Duration.ZERO, startTime);
        stationNodeMap.put(source, initialNode);
        queue.add(initialNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if (visitedStations.contains(currentNode.getStation().getName())) {
                continue;
            }
            for (Path path : currentNode.getStation().getOutPaths()) {
                if (!isPathLegal(path, visitedStations, startTime)) {
                    continue;
                }
                Node adjacentNode = stationNodeMap.getOrDefault(path.getDestination(), new Node(path.getDestination()));
                if (processAdjacentNode(path, currentNode, adjacentNode, size)) {
                    stationNodeMap.put(path.getDestination(), adjacentNode);
                }
                queue.add(adjacentNode);
            }
            visitedStations.add(currentNode.getStation().getName());
        }
        if (!stationNodeMap.containsKey(destination)) {
            throw new IllegalArgumentException();
        }
        return new Itinerary(startTime, stationNodeMap.get(destination).getShortestPath());
    }

    private boolean isPathLegal(Path path, Set<String> visitedStations, LocalTime startTime) {
        if (visitedStations.contains(path.getDestination().getName())) {
            return false;
        }
        for (LocalTime time : path.getSchedule()) {
            if (time.isAfter(startTime)) {
                return true;
            }
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
    private boolean processAdjacentNode(Path path, Node currentNode, Node adjacentNode, NodeSize size) {
        double newDistance = currentNode.getDistance() + path.getTravelDistance();
        Duration newDuration = currentNode.getDuration().plus(path.getTravelDuration());
        Optional<LocalTime> newTimeOpt = path.nextTrainDeparture(currentNode.getTime().plus(path.getTravelDuration()));
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

    @Override
    public Itinerary bestPath(Double startingCoordinates, Double endingCoordinates, LocalTime startTime,
            NodeSize size) {
        return null;
    }

    @Override
    public Itinerary bestPathWalking(Double startingCoordinates, Double endingCoordinates, LocalTime startTime,
            NodeSize size) {
        throw new UnsupportedOperationException("Unimplemented method 'bestPathWalking'");
    }
}