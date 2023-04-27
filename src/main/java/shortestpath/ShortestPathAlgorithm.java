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
import shortestpath.graph.NodeSize;
import java.awt.geom.Point2D.Double;

public abstract class ShortestPathAlgorithm {

    protected final Network network;

    public ShortestPathAlgorithm(Network network) {
        this.network = network;
    }
    
    /**
     * Renvoie le meilleur chemin entre deux stations, à l'heure indiquée
     * @param source station de départ
     * @param destination station d'arrivée
     * @param startTime heure de départ
     * @param size comparateur qui détermine le meilleur itinéraire. Valeurs possibles : NodeSize.TIME, NodeSize.DISTANCE, NodeSize.DURATION
     * @param walking détermine si l'itinéraire peut utiliser des trajets à pied entre les stations
     * @return Objet Itinerary suivant les arguments
     */
    public abstract Itinerary bestPath(Station source, Station destination, LocalTime startTime, NodeSize size, boolean walking);

    public Itinerary bestPath(Station source, Station destination, LocalTime startTime, boolean walking) {
        return bestPath(source, destination, startTime, NodeSize.TIME, walking);
    }

    public abstract Itinerary bestPath(Double startingCoordinates, Double endingCoordinates, LocalTime startTime, NodeSize size);
}