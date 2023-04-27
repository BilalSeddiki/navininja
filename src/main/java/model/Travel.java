package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.geom.Point2D.Double;

import shortestpath.Dijkstra;
import shortestpath.ShortestPathAlgorithm;
import shortestpath.graph.NodeSize;

/** TODO */
public class Travel {

    /** TODO */
    private final ShortestPathAlgorithm algorithm;
    
    /** TODO */
    private final LocalTime departureTime;

    /** TODO */
    private final Double departureCoordinates;

    /** TODO */
    private final Double arrivalCoordinates;

    /** TODO */
    private final Station departureStation;

    /** TODO */
    private final Station arrivalStation;

    /** TODO */
    private static final int SEARCH_LIMIT = 5;

    /** TODO */
    private static final double SEARCH_DIFFERENCE = 1000.0;

    /** TODO */
    private Travel(Builder builder) {
        this.algorithm = builder.algorithm;
        this.departureTime = builder.departureTime;
        this.departureCoordinates = builder.departureCoordinates;
        this.arrivalCoordinates = builder.arrivalCoordinates;
        this.departureStation = builder.departureStation;
        this.arrivalStation = builder.arrivalStation;
    }

    /** TODO */
    public Itinerary createItinerary() {
        //verifier si algo est null
        if(this.departureStation != null && this.arrivalStation != null) {
            return this.fromStationToStation();
        }
        else if(this.departureStation != null && this.arrivalCoordinates != null) {
            return this.fromStationToCoordinates();
        }
        else if(this.departureCoordinates != null && this.arrivalCoordinates != null) {
            return this.fromCoordinatesToCoordinates();
        }
        else if(this.departureCoordinates != null && this.arrivalStation != null) {
            return this.fromCoordinatesToStation();
        }
        return this.createEmptyItinerary();
    }

    /** TODO */
    public Itinerary createEmptyItinerary() {
        //List<Transport> transport = new ArrayList<Transport>();
        List<Path> transport = new ArrayList<Path>(); //Temporaire
        Itinerary empty = new Itinerary(this.departureTime, transport);
        return empty;
    }

    /** TODO */
    private Itinerary fromStationToStation() {
        return this.bestItinerary(this.departureStation, this.arrivalStation); 
    }

    /** TODO */
    private Itinerary fromCoordinatesToCoordinates() {
        //TODO
        return this.createEmptyItinerary();
    }

    /** TODO */
    private Itinerary fromCoordinatesToStation() {
        TreeMap<java.lang.Double, Station> tree = this.algorithm.getNetwork().getClosestStations(this.departureCoordinates);
        System.out.println(this.algorithm.getNetwork().getStationsByName().size());
        SortedMap<java.lang.Double, Station> map = tree.headMap(tree.firstKey() + SEARCH_LIMIT);
        
        Station stationMin = map.get(map.firstKey());
        Itinerary itineraryMin = this.bestItinerary(stationMin, this.arrivalStation);
        Duration durationMin = itineraryMin.getDuration();
        for(java.lang.Double key : map.keySet()) {
            Station stationLoop = map.get(key);
            Itinerary itineraryLoop = this.bestItinerary(stationLoop, this.arrivalStation);
            Duration durationLoop = itineraryLoop.getDuration();
            if(durationLoop.compareTo(durationMin) < 0) {
                Walk walkDifference = new Walk(stationMin.getCoordinates(), stationLoop.getCoordinates());
                double distance = walkDifference.getTravelDistance();
                if(distance < this.SEARCH_DIFFERENCE) {
                    stationMin = stationLoop;
                    itineraryMin = itineraryLoop;
                    durationMin = durationLoop;
                }
            }
        }
        Walk departureWalk = new Walk(this.departureCoordinates, stationMin.getCoordinates());
        System.out.println(departureWalk.toString());
        itineraryMin.addToItineraryWithIndex(0, departureWalk);
        return itineraryMin;
    }

    /** TODO */
    private Itinerary fromStationToCoordinates() {
        //TODO
        return this.createEmptyItinerary();
    }

    /** TODO */
    public Itinerary bestItinerary(Station departureStation, Station arrivalStation) {
        return this.algorithm.bestPath(departureStation, arrivalStation, this.departureTime, NodeSize.TIME);
    }

    /** TODO */
    public Walk bestWalk() {
        return null;
    }

    public static class Builder {
        private ShortestPathAlgorithm algorithm;
        private LocalTime departureTime;
        private Double departureCoordinates;
        private Double arrivalCoordinates;
        private Station departureStation;
        private Station arrivalStation;
        private int BUILDER_SEARCH_LIMIT;
        private double BUILDER_SEARCH_DIFFERENCE;

        public Builder(ShortestPathAlgorithm algorithm) {
            this.algorithm = algorithm;
        }

        public Builder setDepartureTime(LocalTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public Builder setDepartureCoordinates(Double departureCoordinates) {
            this.departureCoordinates = departureCoordinates;
            return this;
        }

        public Builder setArrivalCoordinates(Double arrivalCoordinates) {
            this.arrivalCoordinates = arrivalCoordinates;
            return this;
        }

        public Builder setDepartureStation(Station departureStation) {
            this.departureStation = departureStation;
            return this;
        }

        public Builder setArrivalStation(Station arrivalStation) {
            this.arrivalStation = arrivalStation;
            return this;
        }

        public Builder setSearchLimit(int limit) {
            this.BUILDER_SEARCH_LIMIT = limit;
            return this;
        }

        public Builder setSearchDifference(double difference) {
            this.BUILDER_SEARCH_DIFFERENCE = difference;
            return this;
        }

        public Travel build() {
            if(this.algorithm == null) {
                //TODO: Exception
            }

            if(this.departureTime == null) {
                this.departureTime = LocalTime.now();
            }

            if(this.departureCoordinates == null && this.arrivalCoordinates == null
                && this.departureStation == null && this.arrivalStation == null
            ) {
                //TODO: throw new exception
            }

            if(this.departureCoordinates == null && this.departureStation == null) {
                //TODO exn
            }

            if(this.arrivalCoordinates == null && this.arrivalStation == null) {
                //TODO exn
            }

            return new Travel(this);
        }
    }
}