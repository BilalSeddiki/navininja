package shortestpath;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Map.Entry;

import model.Itinerary;
import model.Network;
import model.Path;
import model.Station;
import model.Transport;
import shortestpath.graph.NodeSize;
import java.awt.geom.Point2D.Double;

public abstract class ShortestPathAlgorithm {

    protected final Network network;

    public ShortestPathAlgorithm(Network network) {
        this.network = network;
    }

    /** TODO */
    public Network getNetwork() {
        return this.network;
    }

    /** TODO */
    public List<Transport> pathIntoTransport(List<Path> paths) {
        List<Transport> transports = new ArrayList<Transport>();
        for(int i = 0; i < paths.size(); i++) {
            transports.add(paths.get(i));
        }
        return transports;
    }
    
    /**
     * Renvoie le meilleur chemin entre deux stations, à l'heure indiquée
     * @param source
     * @param destination
     * @param startTime
     * @param size
     * @return
     */
    public abstract Itinerary bestPath(Station source, Station destination, LocalTime startTime, NodeSize size);

    public Itinerary bestPath(Station source, Station destination, LocalTime startTime) {
        return bestPath(source, destination, startTime, NodeSize.TIME);
    }

    public abstract Itinerary bestPath(Double startingCoordinates, Double endingCoordinates, LocalTime startTime, NodeSize size);
    
    public abstract Itinerary bestPathWalking(Double startingCoordinates, Double endingCoordinates, LocalTime startTime, NodeSize size);

    /*
    protected Station createHumanBeginStation(Double coordinates, LocalTime startTime) {
        try {
            return network.getStation(coordinates);
        }
        catch (NoSuchElementException e) {}

        TreeMap<java.lang.Double, Station> map = network.getClosestStations(coordinates).entrySet().stream().limit(1).collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);;
        Station currentPosition = new Station("humain départ", coordinates);
        for (Entry<java.lang.Double, Station> entry : map.entrySet()) {
            ArrayList<LocalTime> schedule = new ArrayList<>();
            schedule.add(startTime);
            Path path = new Path(null, null, schedule, Duration.ofSeconds((long) (entry.getKey() * 10)), entry.getKey(), currentPosition, entry.getValue());
            currentPosition.addOutPath(path);
        }
        return currentPosition;
    }

    protected Station createHumanEndStation(Double coordinates) {
        try {
            return network.getStation(coordinates);
        }
        catch (NoSuchElementException e) {}

        TreeMap<java.lang.Double, Station> map = network.getClosestStations(coordinates).entrySet().stream().limit(1).collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);;
        Station currentPosition = new Station("humain fin", coordinates);
        for (Entry<java.lang.Double, Station> entry : map.entrySet()) {
            for (Path stationPath : entry.getValue().getInPaths()) {
                ArrayList<LocalTime> list = new ArrayList<LocalTime>(stationPath.getSchedule());
                for (int i = 0; i < list.size(); i++) {
                    LocalTime newTime = list.get(i).plus(stationPath.getTravelDuration());
                    list.set(i, newTime);
                }
                Path path = new Path(null, null, stationPath.getSchedule(), Duration.ofSeconds((long) (entry.getKey() * 10)), entry.getKey(), entry.getValue(), currentPosition);
                currentPosition.addInPath(path);
                stationPath.getDestination().addOutPath(path);
            }
        }
        return currentPosition;
    }
    */
}