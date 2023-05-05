package shortestpath.graph;

import java.util.Comparator;

public class NodeTimeComparator implements Comparator<Node> {

    @Override
    public final int compare(final Node node1, final Node node2) {
        return node1.getTime().compareTo(node2.getTime());
    }
}
