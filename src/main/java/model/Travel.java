package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.util.Pair;

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
    private static int SEARCH_LIMIT = 5;

    /** TODO */
    private static double SEARCH_DISTANCE = 1000.0;

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
        List<Path> transport = new ArrayList<Path>(); //TODO: Temporaire
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
        Pair<Walk, Itinerary> result = 
            this.bestItineraryWithCoordinatesAndStation(
                this.departureCoordinates, this.arrivalStation, true
            );
        Walk walk = result.getKey();
        Itinerary itinerary = result.getValue();
        if(walk != null) {
            itinerary.addToTransportsBeginning(result.getKey());
        }
        return itinerary;
    }

    /** TODO */
    private Itinerary fromStationToCoordinates() {
        Pair<Walk, Itinerary> result = 
            this.bestItineraryWithCoordinatesAndStation(
                this.arrivalCoordinates, this.departureStation, false
            );
        Walk walk = result.getKey();
        Itinerary itinerary = result.getValue();
        if(walk != null) {
            itinerary.addToTransportsEnding(result.getKey());
        }
        return itinerary;
    }

    /** TODO */
    public Itinerary bestItinerary(Station departureStation, Station arrivalStation) {
        return this.algorithm.bestPath(departureStation, arrivalStation, this.departureTime, NodeSize.TIME);
    }

    /** TODO */
    private Pair<Walk, Itinerary> bestItineraryWithCoordinatesAndStation(Double coordinates, Station station, boolean direction) {
        List<Pair<java.lang.Double, Station>> list = this.algorithm.getNetwork().getClosestStations(coordinates);
        
        if(list.size() == 0) {
            return new Pair<Walk, Itinerary>(null, this.createEmptyItinerary());
        }
        if(list.size() < this.SEARCH_LIMIT) {
            this.SEARCH_LIMIT = list.size();
        }

        Station stationMin = list.get(0).getValue();
        Itinerary itineraryMin;
        if(direction) {
            itineraryMin = this.bestItinerary(stationMin, station);
        }
        else {
            itineraryMin = this.bestItinerary(station, stationMin);
        }
        Duration durationMin = itineraryMin.getDuration();

        for(int i = 1; i < this.SEARCH_LIMIT; i++) {
            Pair<java.lang.Double, Station> pair = list.get(i);
            Station stationLoop = pair.getValue();
            Itinerary itineraryLoop;
            if(direction) {
                itineraryLoop = this.bestItinerary(stationLoop, station);
            }
            else {
                itineraryLoop = this.bestItinerary(station, stationLoop);
            }
            Duration durationLoop = itineraryLoop.getDuration();
            if(durationLoop.compareTo(durationMin) < 0) {
                Walk walkDifference = new Walk(stationMin.getCoordinates(), stationLoop.getCoordinates());
                double distance = walkDifference.getTravelDistance();
                if(distance < this.SEARCH_DISTANCE) {
                    stationMin = stationLoop;
                    itineraryMin = itineraryLoop;
                    durationMin = durationLoop;
                }
            }
        }
        Walk walk;
        if(direction) {
            walk = new Walk(coordinates, stationMin.getCoordinates());
        }
        else {
            walk = new Walk(stationMin.getCoordinates(), coordinates);
        }
        Pair<Walk, Itinerary> result = new Pair<Walk, Itinerary>(walk, itineraryMin);
        return result;
    }

    public static class Builder {
        private ShortestPathAlgorithm algorithm;
        private LocalTime departureTime;
        private Double departureCoordinates;
        private Double arrivalCoordinates;
        private Station departureStation;
        private Station arrivalStation;
        private int BUILDER_SEARCH_LIMIT;
        private double BUILDER_SEARCH_DISTANCE;

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
            this.BUILDER_SEARCH_DISTANCE = difference;
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