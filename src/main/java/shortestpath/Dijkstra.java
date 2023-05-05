package shortestpath;

import java.awt.geom.Point2D;
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

import model.Itinerary;
import model.Network;
import model.Path;
import model.Station;
import model.Transport;
import model.Walk;
import shortestpath.graph.Node;
import shortestpath.graph.NodeSize;

public class Dijkstra extends ShortestPathAlgorithm {

    /**
     * Constructeur de l'algorithme Dijkstra.
     * @param network réseau sur lequel on effectue l'algorithme
     */
    public Dijkstra(final Network network) {
        super(network);
    }

    @Override
    public final Itinerary bestPath(final Station source,
        final Station destination, final LocalTime startTime,
        final NodeSize size, final boolean walking) {
        Comparator<Node> comparator = size.getComparator();
        PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
        Set<Point2D.Double> visitedStations = new HashSet<>();
        Map<Point2D.Double, Node> stationNodeMap = new HashMap<>();

        Node initialNode = new Node(source.getCoordinates(), 0,
            Duration.ZERO, startTime);
        stationNodeMap.put(source.getCoordinates(), initialNode);
        queue.add(initialNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if (visitedStations.contains(currentNode.getCoordinates())
                || currentNode.getCoordinates().equals(
                        destination.getCoordinates())) {
                continue;
            }
            List<Transport> transportList =
                getTransportList(currentNode, walking);
            for (Transport path : transportList) {
                if (!isPathLegal(currentNode, path, visitedStations,
                startTime, destination.getCoordinates())) {
                    continue;
                }
                Node adjacentNode = stationNodeMap.getOrDefault(
                    path.getOutCoordinates(),
                    new Node(path.getOutCoordinates()));
                if (processAdjacentNode(path, currentNode, adjacentNode,
                    size)) {
                    stationNodeMap.put(path.getOutCoordinates(), adjacentNode);
                }
                queue.add(adjacentNode);
            }
            visitedStations.add(currentNode.getCoordinates());
        }
        if (!stationNodeMap.containsKey(destination.getCoordinates())) {
            throw new IllegalArgumentException("Aucun chemin n'a été trouvé.");
        }
        return new Itinerary(startTime,
            stationNodeMap.get(destination.getCoordinates()).getShortestPath());
    }

    private boolean isPathLegal(final Node currentNode,
        final Transport transport, final Set<Point2D.Double> visitedStations,
        final LocalTime startTime, final Point2D.Double destination) {
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
            return !transport.getOutCoordinates().equals(destination)
                && (currentNode.getShortestPath().isEmpty()
                || (!currentNode.getShortestPath().isEmpty()
                && currentNode.getShortestPath().get(
                currentNode.getShortestPath().size() - 1)
                instanceof Path));
        }
        return false;
    }

    private List<Transport> getTransportList(final Node currentNode,
        final boolean walking) {
        List<Transport> transportList = new ArrayList<>();
        if (getNetwork().hasStation(currentNode.getCoordinates())) {
            transportList.addAll(getNetwork().getStation(
                currentNode.getCoordinates()).getOutPaths());
        }
        if (walking || !getNetwork().hasStation(currentNode.getCoordinates())) {
            List<Station> list = getNetwork().getClosestStations(
                currentNode.getCoordinates());
            for (Station pair : list) {
                Walk walk = new Walk(currentNode.getCoordinates(),
                    pair.getCoordinates());
                if (walk.getTravelDistance() > getWalkingDistance()
                    && !transportList.isEmpty()) {
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
     * Vérifie si la node adjacente a un meilleur poids avec la node actuelle
     * comme voisin. Si vrai, mets à jour ses valeurs.
     * @param path
     * @param currentNode
     * @param adjacentNode
     * @param size
     * @return true si la node adjacente est meilleure que précédemment
     */
    private boolean processAdjacentNode(final Transport path,
        final Node currentNode, final Node adjacentNode, final NodeSize size) {
        double newDistance = currentNode.getDistance()
            + path.getTravelDistance();
        Duration newDuration = currentNode.getDuration().plus(
            path.getTravelDuration());
        Optional<LocalTime> nextDeparture = path.nextDeparture(
            currentNode.getTime().plus(path.getTravelDuration()));
        if (!nextDeparture.isPresent()) {
            return false;
        }
        LocalTime newTime = nextDeparture.get();
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
        default:
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
}
