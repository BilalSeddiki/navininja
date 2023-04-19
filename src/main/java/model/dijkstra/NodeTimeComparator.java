package model.dijkstra;

import java.util.Comparator;

public class NodeTimeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        return node1.getTime().compareTo(node2.getTime());
    }
}