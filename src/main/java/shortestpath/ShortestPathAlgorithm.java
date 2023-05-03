package shortestpath;

import java.time.LocalTime;

import model.Itinerary;
import model.Network;
import model.Station;
import shortestpath.graph.NodeSize;

import java.awt.geom.Point2D;

public abstract class ShortestPathAlgorithm {

    protected final Network network;
    protected double maxWalkingDistance;

    public ShortestPathAlgorithm(Network network) {
        this.network = network;
        maxWalkingDistance = 1;
    }

    /**
     * Renvoie le réseau sur lequel appliqué l'algorithme.
     * @return le réseau
     */
    public Network getNetwork() {
        return this.network;
    }

    public abstract Itinerary bestPath(Station source, Station destination, LocalTime startTime, NodeSize size, boolean walking);
    
    /**
     * Renvoie le meilleur chemin entre deux stations, à l'heure indiquée
     * @param source station de départ
     * @param destination station d'arrivée
     * @param startTime heure de départ
     * @param size comparateur qui détermine le meilleur itinéraire. Valeurs possibles : NodeSize.TIME, NodeSize.DISTANCE, NodeSize.DURATION
     * @param walking détermine si l'itinéraire peut utiliser des trajets à pied entre les stations
     * @return Objet Itinerary suivant les arguments
     */
    public Itinerary bestPath(Point2D.Double source, Point2D.Double destination, LocalTime startTime, NodeSize size, boolean walking) {
        return bestPath(network.getStation(source), network.getStation(destination), startTime, size, walking);
    }


    public Itinerary bestPath(Point2D.Double source, Point2D.Double destination, LocalTime startTime, boolean walking) {
        return bestPath(source, destination, startTime, NodeSize.TIME, walking);
    }

    public void setWalkingDistance(double distance) {
        this.maxWalkingDistance = distance;
    }
}