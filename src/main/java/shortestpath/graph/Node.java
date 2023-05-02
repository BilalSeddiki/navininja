package shortestpath.graph;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Transport;

public class Node {

    private final Point2D.Double coordinates;
    private double distance;
    private Duration duration;
    private LocalTime time;
    private List<Transport> shortestPath;

    public Node(Point2D.Double coordinates, double distance, Duration duration, LocalTime time) {
        this.coordinates = coordinates;
        this.distance = distance;
        this.duration = duration;
        this.time = time;
        this.shortestPath = new ArrayList<>();
    }

    public Node(Point2D.Double coordinates) {
        this(coordinates, Double.MAX_VALUE, ChronoUnit.FOREVER.getDuration(), LocalTime.MAX);
    }

    public void translateDistance(double distance) {
        this.distance += distance;
    }

    public void translateDuration(Duration duration) {
        this.duration.plus(duration);
    }

    public Point2D.Double getCoordinates() {
        return coordinates;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<Transport> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(Node node, Transport path) {
        this.shortestPath.clear();
        this.shortestPath.addAll(node.getShortestPath());
        this.shortestPath.add(path);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(coordinates);
        stringBuilder.append(" (");
        stringBuilder.append(distance);
        stringBuilder.append(" km) (");
        stringBuilder.append(duration.toSeconds());
        stringBuilder.append(" seconds) (");
        stringBuilder.append(time);
        stringBuilder.append(")\n");
        for (Transport path : shortestPath) {
            stringBuilder.append(path);
            stringBuilder.append(" ");
            stringBuilder.append(time);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}