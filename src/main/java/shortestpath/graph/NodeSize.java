package shortestpath.graph;

import java.util.Comparator;

public enum NodeSize {

    /** Voyage qui parcourt le moins de distance. */
    DISTANCE,

    /** Voyage avec le moins de temps dans les transports. */
    DURATION,

    /** Voyage qui prend le moins de temps. */
    TIME;

    /**
     * Renvoie le comparateur correspondant à la valeur de l'énumération.
     * @return comparateur correspondant à la valeur de l'énumération
     */
    public Comparator<Node> getComparator() {
        switch (this) {
            case DISTANCE:
                return new NodeDistanceComparator();
            case DURATION:
                return new NodeDurationComparator();
            case TIME:
                return new NodeTimeComparator();
            default:
                break;
        }
        throw new IllegalArgumentException();
    }
}
