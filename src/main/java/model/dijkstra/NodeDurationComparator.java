package model.dijkstra;

import java.util.Comparator;

public class NodeDurationComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        return node1.getDuration().compareTo(node2.getDuration());
    }
}