package shortestpath.graph;

import java.util.Comparator;

public class NodeDistanceComparator implements Comparator<Node> {

    @Override
    public final int compare(final Node node1, final Node node2) {
        return Double.compare(node1.getDistance(), node2.getDistance());
    }
}
