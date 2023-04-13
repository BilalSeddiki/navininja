package model.dijkstra;

import java.util.Comparator;

public class NodeDistanceDurationComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        return Double.compare(node1.getDistance() + node1.getDuration().toSeconds(), node2.getDistance() + node2.getDuration().toSeconds());
    }
}