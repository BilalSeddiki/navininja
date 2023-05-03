package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.awt.geom.Point2D.Double;

import shortestpath.*;
import shortestpath.graph.NodeSize;
import utils.IllegalTravelException;

/** Un déplacement d'un point à un autre. */
public class Travel {

    /** Algorithme de calcul du meilleur itinéraire. */
    private final ShortestPathAlgorithm algorithm;

    /** TODO */
    private final Network network;
    
    /** Heure de départ de l'itinéraire. */
    private final LocalTime departureTime;

    /** Coordonnées GPS de départ. */
    private final Double departureCoordinates;

    /** Coordonnées GPS d'arrivée. */
    private final Double arrivalCoordinates;

    /** Nombre de recherches maximal pour un meilleur itinéraire depuis une station plus éloignée. */
    private int SEARCH_LIMIT;

    /** Distance de recherche maximale pour un meilleur itinéraire depuis une station plus éloignée. */
    private double SEARCH_DISTANCE;

    /** 
     * Construit un déplacement d'un point à un autre.
     * @param builder un monteur de déplacement avec les paramètres nécessaires.
     */
    private Travel(Builder builder) {
        this.algorithm = builder.algorithm;
        this.network = builder.network;
        this.departureTime = builder.departureTime;
        this.departureCoordinates = builder.departureCoordinates;
        this.arrivalCoordinates = builder.arrivalCoordinates;
        this.SEARCH_LIMIT = builder.BUILDER_SEARCH_LIMIT;
        this.SEARCH_DISTANCE = builder.BUILDER_SEARCH_DISTANCE;
    }

    /**
     * Renvoie l'algorithme utilisé pour le calcul du meilleur itinéraire.
     * @return algorithme utilisé, qui contient le réseau.
     */
    public ShortestPathAlgorithm getShortestPathAlgorithm() {
        return this.algorithm;
    }

    /** TODO */
    public Network getNetwork() {
        return this.network;
    }

    /**
     * Renvoie l'heure de départ du déplacement.
     * @return l'heure de départ. 
     */
    public LocalTime getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Renvoie les coordonnées du point de départ.
     * Null est renvoyé si elles ne sont pas définies.
     * @return coordonnées du point de départ, null si non définies.
     */
    public Double getDepartureCoordinates() {
        return this.departureCoordinates;
    }

    /**
     * Renvoie les coordonnées du point d'arrivée.
     * Null est renvoyé si elles ne sont pas définies.
     * @return coordonnées du point d'arrivée, null si non définies.
     */
    public Double getArrivalCoordinates() {
        return this.arrivalCoordinates;
    }

    /**
     * Renvoie le nombre de recherche maximal à effectuer pendant la recherche
     * des stations les plus proches.
     * @return nombre de recherche maximal.
     */
    public int getSearchLimit() {
        return this.SEARCH_LIMIT;
    }

    /**
     * Renvoie la distance de recherche maximale à considérer pendant la recherche
     * des stations les plus proches.
     * @return distance de recherche maximale.
     */
    public double getSearchDistance() {
        return this.SEARCH_DISTANCE;
    }

    /** 
     * Calcule un itinéraire en fonction des paramètres non nuls.
     * @return itinéraire calculé en fonction des paramètres non nuls.
     */
    public Itinerary createItinerary() {
        Itinerary itinerary;
        if(this.departureCoordinates != null && this.arrivalCoordinates != null) {
            boolean isDepartureStation = this.network.hasStation(departureCoordinates);
            boolean isArrivalStation = this.network.hasStation(arrivalCoordinates);
            if(isDepartureStation && isArrivalStation) {
                itinerary = this.fromStationToStation();
            }
            else if(isDepartureStation) {
                itinerary = this.fromCoordinatesOrStation(false);
            }
            else if(isArrivalStation) {
                itinerary = this.fromCoordinatesOrStation(true);
            }
            else {
                itinerary = this.fromCoordinatesToCoordinates();
            }

        }
        else {
            itinerary = createEmptyItinerary();
        }
        return itinerary;
    }

    /** 
     * Renvoie un itinéraire vide.
     * @return itinéraire vide.
     */
    public Itinerary createEmptyItinerary() {
        List<Transport> transport = new ArrayList<Transport>();
        Itinerary empty = new Itinerary(this.departureTime, transport);
        return empty;
    }

    /** 
     * Calcule un itinéraire dont le départ est une station et l'arrivée une station.
     * @return itinéraire calculé à partir d'une station vers une station.
     */
    private Itinerary fromStationToStation() {
        try {
            Station departureStation = this.network.getStation(this.departureCoordinates);
            Station arrivalStation = this.network.getStation(this.arrivalCoordinates);
            return this.bestItinerary(departureStation, arrivalStation); 
        }
        catch(Exception e) {
            return this.createEmptyItinerary();
        }
    }

    /** TODO */
    private Itinerary fromCoordinatesOrStation(boolean direction) {
        Double coordinates;
        Station station;
        Itinerary itinerary;
        try {
            if(direction) {
                coordinates = departureCoordinates;
                station = this.network.getStation(this.arrivalCoordinates);
            }
            else {
                coordinates = arrivalCoordinates;
                station = this.network.getStation(this.departureCoordinates);
            }
            Pair<Itinerary, Walk> result = 
                this.searchFromCoordAndStation(coordinates, station, direction);
            itinerary = result.getKey();
            Walk walk = result.getValue();
            if(walk != null) {
                itinerary.addToPosition(walk, direction);
            }
        }
        catch (Exception e) {
            itinerary = this.createEmptyItinerary();
        }        
        return itinerary;
    }

    /** 
     * Calcule un itinéraire dont le départ sont des coordonnées GPS et l'arrivée des coordonnées GPS.
     * @return itinéraire calculé à partir de coordonnées GPS vers des coordonnées GPS.
     */
    private Itinerary fromCoordinatesToCoordinates() {
        return this.searchFromCoord(this.departureCoordinates, this.arrivalCoordinates);
    }
    
    /** 
     * Calcule le meilleur itinéraire d'une station vers une station.
     * @param departureStation station de départ.
     * @param arrivalStation station d'arrivée.
     * @return meilleur itinéraire partant de la station de départ vers la station d'arrivée.
     */
    public Itinerary bestItinerary(Station departureStation, Station arrivalStation) {
        return this.algorithm.bestPath(departureStation, arrivalStation, this.departureTime, NodeSize.TIME, false);
    }

    /** 
     * Recherche le meilleur itinéraire et le meilleur trajet à pied à partir d'une station,
     * de coordonnées GPS et un sens.
     * @param coordinates coordonnées GPS.
     * @param station station.
     * @param direction true si le point de départ sont les coordonnées GPS et que la station est le 
     * point d'arrivée, false sinon.
     * @return couple d'un itinéraire et d'un trajet à pied.
     */
    private Pair<Itinerary, Walk> searchFromCoordAndStation(Double coordinates, Station station, boolean direction) {
        List<Station> list = this.algorithm.getNetwork().getClosestStations(coordinates);
        
        if(list.size() == 0) {
            return new Pair<Itinerary, Walk>(this.createEmptyItinerary(), null);
        }
        if(list.size() < this.SEARCH_LIMIT) {
            this.SEARCH_LIMIT = list.size();
        }

        Pair<Itinerary, Station> min = this.itineraryFromCoordAndStation(list, station, direction);
        Itinerary itineraryMin = min.getKey();
        Station stationMin = min.getValue();
        
        Walk walk = this.walkWithDirection(coordinates, stationMin.getCoordinates(), direction);
        Pair<Itinerary, Walk> result = new Pair<Itinerary, Walk>(itineraryMin, walk);
        return result;
    }

    /** 
     * Calcule le meilleur itinéraire à partir d'une liste triée de paires de station
     * et de leur distance avec un point, d'une station et d'un sens.
     * @param list liste triée de paires de station et de leur distance avec un point.
     * @param station station.
     * @param direction true si le point de départ sont les coordonnées GPS et que la station est le 
     * point d'arrivée, false sinon.
     * @return couple d'un itinéraire et d'une station.
     */
    private Pair<Itinerary, Station> itineraryFromCoordAndStation(List<Station> list, Station station, boolean direction) {
        Station stationMin = list.get(0);
        Itinerary itineraryMin = this.itineraryWithDirection(stationMin, station, direction);
        Duration durationMin = itineraryMin.getDuration();

        for(int i = 1; i < this.SEARCH_LIMIT; i++) {
            Station stationLoop = list.get(i);
            Itinerary itineraryLoop = this.itineraryWithDirection(stationLoop, station, direction);
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
        Pair<Itinerary, Station> result = new Pair<Itinerary, Station>(itineraryMin, stationMin);
        return result;
    }

    /** 
     * Renvoie le meilleur itinéraire en partant d'une station vers une station, en fonction 
     * de la direction.
     * @param station1 Première station.
     * @param station2 Seconde station.
     * @param direction true si le point de départ est la première station et le point d'arrivée
     * est la deuxième station, false sinon.
     * @return itinéraire calculé à partir des deux stations.
     */
    private Itinerary itineraryWithDirection(Station station1, Station station2, boolean direction) {
        if(direction) {
            return this.bestItinerary(station1, station2);
        }
        else {
            return this.bestItinerary(station2, station1);
        }
    }

    /** 
     * Renvoie un trajet à pied en partant de coordonnées GPS vers d'autres coordonnées GPS.
     * @param coordinates1 Premières coordonnées GPS.
     * @param coordinates1 Secondes coordonnées GPS.
     * @param direction true si le point de départ sont les premières coordonnées GPS et le point d'arrivée
     * sont les deuxièmes coordonnées, false sinon.
     * @return trajet à pied.
     */
    private Walk walkWithDirection(Double coordinates1, Double coordinates2, boolean direction) {
        if(direction) {
            return new Walk(coordinates1, coordinates2);
        }
        else {
            return new Walk(coordinates2, coordinates1);
        }
    }

    /** 
     * Recherche le meilleur itinéraire à partir d'un point de départ qui sont des coordonnées GPS
     * et d'un point d'arrivée qui sont des coordonnées GPS.
     * @param departure coordonnées GPS de départ.
     * @param arrival coordonnées GPS d'arrivée.
     * @return itinéraire calculé à partir des coordonnées GPS de départ et d'arrivée.
     */
    private Itinerary searchFromCoord(Double departure, Double arrival) {
        List<Station> listDeparture = this.algorithm.getNetwork().getClosestStations(departure);
        List<Station> listArrival = this.algorithm.getNetwork().getClosestStations(arrival);

        if(listDeparture.size() == 0 || listArrival.size() == 0) {
            return this.createEmptyItinerary();
        }
        int sizeMin = Math.min(listDeparture.size(), listArrival.size());
        if(sizeMin < this.SEARCH_LIMIT) {
            this.SEARCH_LIMIT = sizeMin;
        }
        Itinerary itineraryMin = this.itineraryFromCoord(listDeparture, listArrival, departure, arrival);
        return itineraryMin;
    }

    /** 
     * Calcule le meilleur itinéraire à partir d'une liste triée de paires de station 
     * et de leur distance avec le point de départ, une seconde liste similaire
     * pour le point d'arrivée, les coordonnées de départ et les coordonnées d'arrivée.
     * @param listDeparture liste triée de paires de station et de leur distance avec le point de départ.
     * @param listArrival liste triée de paires de station et de leur distance avec le point d'arrivée.
     * @param departure coordonnées GPS de départ.
     * @param arrival coordonnées GPS d'arrivée.
     * @return itinéraire calculé à partir des listes et coordonnées.
     */
    private Itinerary itineraryFromCoord(
        List<Station> listDeparture, 
        List<Station> listArrival,
        Double departure, Double arrival
        ) {
        Station departureMin = listDeparture.get(0);
        Station arrivalMin = listArrival.get(0);
        Itinerary itineraryMin = this.bestItinerary(departureMin, arrivalMin);
        Duration durationMin = itineraryMin.getDuration();

        if(itineraryMin.getTransports().size() == 0) {
            Walk walk = new Walk(departure, arrival);
            itineraryMin.addToFirstPosition(walk);
            return itineraryMin;
        }

        for(int i = 1; i < this.SEARCH_LIMIT; i++) {
            Station stationDepartureLoop = listDeparture.get(i);
            for(int j = 1; j < this.SEARCH_LIMIT; j++) {
                Station stationArrivalLoop = listArrival.get(j);
                if(stationDepartureLoop.equals(stationArrivalLoop)) {
                    continue;
                }
                Itinerary itineraryLoop = this.bestItinerary(stationDepartureLoop, stationArrivalLoop);
                Duration durationLoop = itineraryLoop.getDuration();
                if(durationLoop.compareTo(durationMin) < 0) {
                    Walk walkDifference = new Walk(arrivalMin.getCoordinates(), stationArrivalLoop.getCoordinates());
                    double distance = walkDifference.getTravelDistance();
                    if(distance < this.SEARCH_DISTANCE) {
                        departureMin = stationDepartureLoop;
                        arrivalMin = stationArrivalLoop;
                        itineraryMin = itineraryLoop;
                        durationMin = durationLoop;
                    }
                }
            }
        }

        Walk departureWalk = new Walk(departure, departureMin.getCoordinates());
        itineraryMin.addToFirstPosition(departureWalk);

        Walk arrivalWalk = new Walk(arrivalMin.getCoordinates(), arrival);
        itineraryMin.addToLastPosition(arrivalWalk);

        return itineraryMin;
    }

    /** Monteur permettant de gérer les différentes combinaisons d'attributs. */
    public static class Builder {
        private ShortestPathAlgorithm algorithm;
        private Network network;
        private LocalTime departureTime;
        private Double departureCoordinates;
        private Double arrivalCoordinates;
        private Station departureStation;
        private Station arrivalStation;
        private int BUILDER_SEARCH_LIMIT = 5;
        private double BUILDER_SEARCH_DISTANCE = 1000.0;

        /**
         * Construit un monteur.
         * @param algorithm Algorithme de calcul du meilleur itinéraire.
         */
        public Builder(Network network) {
            this.network = network;
        }

        /** TODO */
        public Builder setDijkstra() {
            this.algorithm = new Dijkstra(this.network);
            return this;
        } 

        /**
         * Fixe l'heure de départ de l'itinéraire.
         * @param departureTime l'heure de départ de l'itinéraire.
         * @return Monteur dont l'heure de départ a été fixée.
         */
        public Builder setDepartureTime(LocalTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        /**
         * Fixe les coordonnées du point de départ.
         * @param departureCoordinates coordonnées du point de départ.
         * @return Monteur dont les coordonnées du point de départ ont été fixées.
         */
        public Builder setDepartureCoordinates(Double departureCoordinates) {
            this.departureCoordinates = departureCoordinates;
            return this;
        }

        /**
         * Fixe les coordonnées du point d'arrivée.
         * @param arrivalCoordinates coordonnées du point d'arrivée.
         * @return Monteur dont les coordonnées du point d'arrivée ont été fixées.
         */
        public Builder setArrivalCoordinates(Double arrivalCoordinates) {
            this.arrivalCoordinates = arrivalCoordinates;
            return this;
        }

        /**
         * Fixe la station de départ.
         * @param departureStation station de départ.
         * @return Monteur dont la station de départ a été fixée.
         */
        public Builder setDepartureStation(Station departureStation) {
            this.departureStation = departureStation;
            return this;
        }

        /**
         * Fixe la station d'arrivée.
         * @param arrivalStation station d'arrivée.
         * @return Monteur dont la station d'arrivée a été fixée.
         */
        public Builder setArrivalStation(Station arrivalStation) {
            this.arrivalStation = arrivalStation;
            return this;
        }

        /**
         * Fixe la limite de recherche.
         * @param limit limite de recherche.
         * @return Monteur dont la limite de recherche a été fixée.
         */
        public Builder setSearchLimit(int limit) {
            this.BUILDER_SEARCH_LIMIT = limit;
            return this;
        }

        /**
         * Fixe la distance de recherche maximale.
         * @param distance distance de recherche maximale.
         * @return Monteur dont la distance de recherche maximale a été fixée.
         */
        public Builder setSearchDistance(double distance) {
            this.BUILDER_SEARCH_DISTANCE = distance;
            return this;
        }

        /**
         * Construit un déplacement à partir des attributs.
         * @return Déplacement construit en fonction des paramètres fournis.
         */
        public Travel build() throws IllegalTravelException {
            if(this.network == null) {
                throw new IllegalTravelException("Le réseau n'est pas défini.");
            }

            if(this.algorithm == null) {
                this.algorithm = new Dijkstra(this.network);
            }

            if(this.departureTime == null) {
                LocalTime now = LocalTime.now();
                this.departureTime = LocalTime.of(now.getHour(), now.getMinute());
            }

            if(this.departureCoordinates == null && this.departureStation == null) {
                throw new IllegalTravelException("Aucun point de départ n'a été défini.");
            }

            else if(this.departureCoordinates != null && this.departureStation != null) {
                throw new IllegalTravelException("Il ne peut y avoir qu'un unique point de départ.");
            }

            else if(this.arrivalCoordinates == null && this.arrivalStation == null) {
                throw new IllegalTravelException("Aucun point d'arrivée n'a été défini.");
            }

            else if(this.arrivalCoordinates != null && this.arrivalStation != null) {
                throw new IllegalTravelException("Il ne peut y avoir qu'un unique point d'arrivée.");
            }

            if(this.BUILDER_SEARCH_LIMIT < 0) {
                this.BUILDER_SEARCH_LIMIT = 1;
            }

            if(this.BUILDER_SEARCH_DISTANCE < 0.0) {
                this.BUILDER_SEARCH_DISTANCE = 10.0;
            }

            if(this.departureStation != null) {
                this.departureCoordinates = departureStation.getCoordinates();
            }

            if(this.arrivalStation != null) {
                this.arrivalCoordinates = arrivalStation.getCoordinates();
            }

            return new Travel(this);
        }
    }
}