package shortestpath.graph;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Path;
import model.Station;

public class Node {

    private final Station station;
    private double distance;
    private Duration duration;
    private LocalTime time;
    private List<Path> shortestPath;

    public Node(Station station, double distance, Duration duration, LocalTime time) {
        this.station = station;
        this.distance = distance;
        this.duration = duration;
        this.time = time;
        this.shortestPath = new ArrayList<>();
    }

    public Node(Station station) {
        this(station, Double.MAX_VALUE, ChronoUnit.FOREVER.getDuration(), LocalTime.MAX);
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<Path> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(Node node, Path path) {
        this.shortestPath.clear();
        this.shortestPath.addAll(node.getShortestPath());
        this.shortestPath.add(path);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(station.getName());
        stringBuilder.append(" (");
        stringBuilder.append(distance);
        stringBuilder.append(" km) (");
        stringBuilder.append(duration.toSeconds());
        stringBuilder.append(" seconds) (");
        stringBuilder.append(time);
        stringBuilder.append(")\n");
        for (Path path : shortestPath) {
            stringBuilder.append(path);
            stringBuilder.append(" ");
            stringBuilder.append(time);
            stringBuilder.append("\n");
        }
        stringBuilder.append(station.getName());
        return stringBuilder.toString();
    }
}