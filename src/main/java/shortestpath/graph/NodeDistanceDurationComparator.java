package shortestpath.graph;

import java.util.Comparator;

public class NodeDistanceDurationComparator implements Comparator<Node> {

    @Override
    public final int compare(final Node node1, final Node node2) {
        return Double.compare(
            node1.getDistance() + node1.getDuration().toSeconds(),
            node2.getDistance() + node2.getDuration().toSeconds()
        );
    }
}
