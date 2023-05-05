package shortestpath.graph;

import java.util.Comparator;

public class NodeDurationComparator implements Comparator<Node> {

    @Override
    public final int compare(final Node node1, final Node node2) {
        return node1.getDuration().compareTo(node2.getDuration());
    }
}
