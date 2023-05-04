package shortestpath.graph;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Transport;

public class Node {

    /** Coordonnées de la station représentée par la node. */
    private final Point2D.Double coordinates;
    /** Distance pour atteindre les coordonnées de la node. */
    private double distance;
    /** Durée pour atteindre les coordonnées de la node. */
    private Duration duration;
    /** Heure d'arrivée à la node. */
    private LocalTime time;
    /** Chemin le plus court pour atteindre la node. */
    private List<Transport> shortestPath;

    /**
     * Constructeur de la classe Node.
     * @param coordinates coordonnées de la node, sert d'identifiant
     * @param distance distance de la node par rapport à la source
     * @param duration durée du trajet de la node par rapport à la source
     * @param time heure d'arrivée de la node par rapport à la source
     */
    public Node(final Point2D.Double coordinates, final double distance,
        final Duration duration, final LocalTime time) {
        this.coordinates = coordinates;
        this.distance = distance;
        this.duration = duration;
        this.time = time;
        this.shortestPath = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Node, initialise la distance, durée,
     * et l'heure à des valeurs simulant l'infini.
     * @param coordinates coordonnées de la node, sert d'identifiant
     */
    public Node(final Point2D.Double coordinates) {
        this(coordinates, Double.MAX_VALUE, ChronoUnit.FOREVER.getDuration(),
            LocalTime.MAX);
    }

    /**
     * Ajoute une distance à la distance de la node.
     * @param distance distance à ajouter
     */
    public void translateDistance(final double distance) {
        this.distance += distance;
    }

    /**
     * Ajoute une durée à la durée de la node.
     * @param duration durée à ajouter
     */
    public void translateDuration(final Duration duration) {
        this.duration = this.duration.plus(duration);
    }

    /**
     * Renvoie les coordonnées de la node.
     * @return coordonnées de la node
     */
    public Point2D.Double getCoordinates() {
        return coordinates;
    }

    /**
     * Renvoie la distance de la node.
     * @return distance de la node
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Modifie la distance de la node.
     * @param distance nouvelle distance de la node
     */
    public void setDistance(final double distance) {
        this.distance = distance;
    }

    /**
     * Renvoie la durée de la node.
     * @return durée de la node
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Modifie la durée de la node.
     * @param duration nouvelle durée de la node
     */
    public void setDuration(final Duration duration) {
        this.duration = duration;
    }

    /**
     * Renvoie l'heure d'arrivée à la node.
     * @return heure d'arrivée à la node
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Modifie l'heure d'arrivée à la node.
     * @param time nouvelle heure d'arrivée à la node
     */
    public void setTime(final LocalTime time) {
        this.time = time;
    }

    /**
     * Renvoie le chemin le plus court pour atteindre la node.
     * @return chemin le plus court pour atteindre la node sous forme de
     * liste de transports
     */
    public List<Transport> getShortestPath() {
        return shortestPath;
    }

    /**
     * Ajoute un chemin à la liste des chemins les plus courts pour atteindre
     * la node.
     * @param node node précédente
     * @param path chemin à ajouter
     */
    public void setShortestPath(final Node node, final Transport path) {
        this.shortestPath.clear();
        this.shortestPath.addAll(node.getShortestPath());
        this.shortestPath.add(path);
    }

    @Override
    public final String toString() {
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
