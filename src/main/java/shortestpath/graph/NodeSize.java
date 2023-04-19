package shortestpath.graph;

import java.util.Comparator;

public enum NodeSize {

    /** Voyage qui parcourt le moins de distance */
    DISTANCE,

    /** Voyage avec le moins de temps dans les transports */
    DURATION,

    /** Voyage qui prend le moins de temps */
    TIME;

    public Comparator<? super Node> getComparator() {
        switch (this) {
            case DISTANCE:
                return new NodeDistanceComparator();
            case DURATION:
                return new NodeDurationComparator();
            case TIME:
                return new NodeTimeComparator();
        }
        throw new IllegalArgumentException();
    }
}