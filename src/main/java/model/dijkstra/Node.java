package model.dijkstra;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Path;
import model.Station;

public class Node {

    private final Station station;
    private double distance;
    private Duration duration;
    private List<Path> shortestPath;

    public Node(Station station, double distance, Duration duration) {
        this.station = station;
        this.distance = distance;
        this.duration = duration;
        this.shortestPath = new ArrayList<>();
    }

    public Node(Station station) {
        this(station, Double.MAX_VALUE, ChronoUnit.FOREVER.getDuration());
    }

    public void translateDistance(double distance) {
        this.distance += distance;
    }

    public void translateDuration(Duration duration) {
        this.duration.plus(duration);
    }

    public Station getStation() {
        return station;
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

    public List<Path> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(Node node, Path path) {
        this.shortestPath.clear();
        this.shortestPath.addAll(node.getShortestPath());
        this.shortestPath.add(path);
    }
}