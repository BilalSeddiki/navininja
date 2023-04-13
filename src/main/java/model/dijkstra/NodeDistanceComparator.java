package model.dijkstra;

import java.util.Comparator;

public class NodeDistanceComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        return Double.compare(node1.getDistance(), node2.getDistance());
    }
}