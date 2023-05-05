package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.util.Pair;
import java.awt.geom.Point2D.Double;

import shortestpath.Dijkstra;
import shortestpath.ShortestPathAlgorithm;
import shortestpath.graph.NodeSize;
import utils.IllegalTravelException;

/** Un déplacement d'un point à un autre. */
public final class Travel {

    /** Algorithme de calcul du meilleur itinéraire. */
    private final ShortestPathAlgorithm algorithm;

    /** Poids sur lequel sera basé les calculs de l'algorithme. */
    private final NodeSize nodeSize;

    /** S'il est possible de marcher entre les stations. */
    private final boolean walking;

    /** Réseau utilisé. */
    private final Network network;

    /** Heure de départ de l'itinéraire. */
    private final LocalTime departureTime;

    /** Coordonnées GPS de départ. */
    private final Double departureCoordinates;

    /** Coordonnées GPS d'arrivée. */
    private final Double arrivalCoordinates;

    /**
     * Nombre de recherches maximal pour un meilleur itinéraire depuis une
     * station plus éloignée.
     */
    private int searchLimit;

    /**
     * Distance de recherche maximale pour un meilleur itinéraire depuis une
     * station plus éloignée.
     */
    private final double searchDistance;

    /**
     * Construit un déplacement d'un point à un autre.
     * @param builder un monteur de déplacement avec les paramètres nécessaires.
     */
    private Travel(final Builder builder) {
        this.algorithm = builder.algorithm;
        this.nodeSize = builder.nodeSize;
        this.walking = builder.walking;
        this.network = builder.network;
        this.departureTime = builder.departureTime;
        this.departureCoordinates = builder.departureCoordinates;
        this.arrivalCoordinates = builder.arrivalCoordinates;
        this.searchLimit = builder.builderSearchLimit;
        this.searchDistance = builder.builderSearchDistance;
    }

    /**
     * Renvoie le réseau utilisé.
     * @return réseau utilisé.
     */
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
     * Renvoie les coordonnées du point de départ. Null est renvoyé si elles ne
     * sont pas définies.
     * @return coordonnées du point de départ, null si non définies.
     */
    public Double getDepartureCoordinates() {
        return this.departureCoordinates;
    }

    /**
     * Renvoie les coordonnées du point d'arrivée. Null est renvoyé si elles ne
     * sont pas définies.
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
        return this.searchLimit;
    }

    /**
     * Renvoie la distance de recherche maximale à considérer pendant la
     * recherche des stations les plus proches.
     * @return distance de recherche maximale.
     */
    public double getSearchDistance() {
        return this.searchDistance;
    }

    /**
     * Calcule un itinéraire en fonction des paramètres non nuls.
     * @return itinéraire calculé en fonction des paramètres non nuls.
     */
    public Itinerary createItinerary() {
        boolean isDepartureStation = this.network.hasStation(
            departureCoordinates);
        boolean isArrivalStation = this.network.hasStation(arrivalCoordinates);
        try {
            if (isDepartureStation && isArrivalStation) {
                return this.fromStationToStation();
            }
            else if (isDepartureStation) {
                return this.fromCoordinatesOrStation(false);
            }
        }
        catch (NoSuchElementException e) {
            return this.createEmptyItinerary();
        }

        if (isArrivalStation) {
            return this.fromCoordinatesOrStation(true);
        }
        else {
            return this.fromCoordinatesToCoordinates();
        }
    }

    /**
     * Renvoie un itinéraire vide.
     * @return itinéraire vide.
     */
    public Itinerary createEmptyItinerary() {
        List<Transport> transport = new ArrayList<>();
        return new Itinerary(this.departureTime, transport);
    }

    /**
     * Calcule un itinéraire dont le départ est une station et l'arrivée une
     * station.
     * @return itinéraire calculé à partir d'une station vers une station.
     */
    private Itinerary fromStationToStation() throws NoSuchElementException {
        Station departureStation = network.getStation(departureCoordinates);
        Station arrivalStation = network.getStation(arrivalCoordinates);
        return this.bestItinerary(departureStation, arrivalStation);
    }

    /**
     * Calcule un itinéraire dont le départ ou l'arrivée est une station.
     * @param direction l'arrivée est une station et le départ des coordonnées
     * si true, sinon l'inverse.
     * @return itinéraire.
     */
    private Itinerary fromCoordinatesOrStation(final boolean direction)
        throws NoSuchElementException {
        Double coordinates;
        Station station;
        Itinerary itinerary;
        if (direction) {
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
        if (walk != null) {
            itinerary.addToPosition(walk, direction);
        }
        return itinerary;
    }

    /**
     * Calcule un itinéraire dont le départ sont des coordonnées GPS et
     * l'arrivée des coordonnées GPS.
     * @return itinéraire calculé à partir de coordonnées GPS vers des
     * coordonnées GPS.
     */
    private Itinerary fromCoordinatesToCoordinates() {
        return searchFromCoord(departureCoordinates, arrivalCoordinates);
    }

    /**
     * Calcule le meilleur itinéraire d'une station vers une station.
     * @param departureStation station de départ.
     * @param arrivalStation   station d'arrivée.
     * @return meilleur itinéraire partant de la station de départ
     * vers la station d'arrivée.
     */
    public Itinerary bestItinerary(final Station departureStation,
        final Station arrivalStation) {
        return algorithm.bestPath(
            departureStation, arrivalStation, departureTime, nodeSize, walking);
    }

    /**
     * Recherche le meilleur itinéraire et le meilleur trajet à pied à partir
     * d'une station, de coordonnées GPS et un sens.
     * @param coordinates coordonnées GPS.
     * @param station     station.
     * @param direction   true si le point de départ sont les coordonnées GPS et
     * que la station est le point d'arrivée, false sinon.
     * @return couple d'un itinéraire et d'un trajet à pied.
     */
    private Pair<Itinerary, Walk> searchFromCoordAndStation(
        final Double coordinates, final Station station,
        final boolean direction) {
        List<Station> list = algorithm.getNetwork()
            .getClosestStations(coordinates);

        if (list.isEmpty()) {
            return new Pair<>(this.createEmptyItinerary(), null);
        }
        if (list.size() < this.searchLimit) {
            this.searchLimit = list.size();
        }

        Pair<Itinerary, Station> min =
            itineraryFromCoordAndStation(list, station, direction);
        Itinerary itineraryMin = min.getKey();
        Station stationMin = min.getValue();

        Walk walk = walkWithDirection(coordinates,
            stationMin.getCoordinates(), direction);
        return new Pair<>(itineraryMin, walk);
    }

    /**
     * Calcule le meilleur itinéraire à partir d'une liste triée de paires de
     * station et de leur distance avec un point, d'une station et d'un sens.
     * @param list liste triée de paires de station et de leur distance avec un
     * point.
     * @param station station.
     * @param direction true si le point de départ sont les coordonnées GPS
     * et que la station est le point d'arrivée, false sinon.
     * @return couple d'un itinéraire et d'une station.
     */
    private Pair<Itinerary, Station> itineraryFromCoordAndStation(
        final List<Station> list, final Station station,
        final boolean direction) {
        Station stationMin = list.get(0);
        Itinerary itineraryMin =
            itineraryWithDirection(stationMin, station, direction);
        Duration durationMin = itineraryMin.getDuration();

        for (int i = 1; i < this.searchLimit; i++) {
            Station stationLoop = list.get(i);
            Itinerary itineraryLoop =
                itineraryWithDirection(stationLoop, station, direction);
            Duration durationLoop = itineraryLoop.getDuration();
            if (durationLoop.compareTo(durationMin) < 0) {
                Walk walkDifference =
                    new Walk(stationMin.getCoordinates(),
                        stationLoop.getCoordinates());
                double distance = walkDifference.getTravelDistance();
                if (distance < this.searchDistance) {
                    stationMin = stationLoop;
                    itineraryMin = itineraryLoop;
                    durationMin = durationLoop;
                }
            }
        }
        return new Pair<>(itineraryMin, stationMin);
    }

    /**
     * Renvoie le meilleur itinéraire en partant d'une station vers une station,
     * en fonction de la direction.
     * @param station1  Première station.
     * @param station2  Seconde station.
     * @param direction true si le point de départ est la première station et le
     *                  point d'arrivée est la deuxième station, false sinon.
     * @return itinéraire calculé à partir des deux stations.
     */
    private Itinerary itineraryWithDirection(final Station station1,
        final Station station2, final boolean direction) {
        if (direction) {
            return this.bestItinerary(station1, station2);
        }
        else {
            return this.bestItinerary(station2, station1);
        }
    }

    /**
     * Renvoie un trajet à pied en partant de coordonnées GPS vers d'autres
     * coordonnées GPS.
     * @param coordinates1 Premières coordonnées GPS.
     * @param coordinates2 Secondes coordonnées GPS.
     * @param direction true si le point de départ sont les premières
     * coordonnées GPS et le point d'arrivée sont les deuxièmes coordonnées,
     * false sinon.
     * @return trajet à pied.
     */
    private Walk walkWithDirection(final Double coordinates1,
        final Double coordinates2, final boolean direction) {
        if (direction) {
            return new Walk(coordinates1, coordinates2);
        }
        else {
            return new Walk(coordinates2, coordinates1);
        }
    }

    /**
     * Recherche le meilleur itinéraire à partir d'un point de départ qui sont
     * des coordonnées GPS et d'un point d'arrivée qui sont des coordonnées GPS.
     * @param departure coordonnées GPS de départ.
     * @param arrival   coordonnées GPS d'arrivée.
     * @return itinéraire calculé à partir des coordonnées GPS de départ et
     *         d'arrivée.
     */
    private Itinerary searchFromCoord(final Double departure,
        final Double arrival) {
        List<Station> listDeparture =
            algorithm.getNetwork().getClosestStations(departure);
        List<Station> listArrival =
            algorithm.getNetwork().getClosestStations(arrival);

        if (listDeparture.isEmpty() || listArrival.isEmpty()) {
            return this.createEmptyItinerary();
        }
        int sizeMin = Math.min(listDeparture.size(), listArrival.size());
        if (sizeMin < this.searchLimit) {
            this.searchLimit = sizeMin;
        }
        return
            itineraryFromCoord(listDeparture, listArrival, departure, arrival);
    }

    /**
     * Calcule le meilleur itinéraire à partir d'une liste triée de paires de
     * station et de leur distance avec le point de départ, une seconde liste
     * similaire pour le point d'arrivée, les coordonnées de départ et les
     * coordonnées d'arrivée.
     * @param listDeparture liste triée de paires de station et de leur distance
     *                      avec le point de départ.
     * @param listArrival   liste triée de paires de station et de leur distance
     *                      avec le point d'arrivée.
     * @param departure     coordonnées GPS de départ.
     * @param arrival       coordonnées GPS d'arrivée.
     * @return itinéraire calculé à partir des listes et coordonnées.
     */
    private Itinerary itineraryFromCoord(final List<Station> listDeparture,
        final List<Station> listArrival, final Double departure,
        final Double arrival) {
        Station departureMin = listDeparture.get(0);
        Station arrivalMin = listArrival.get(0);
        Itinerary itineraryMin = this.bestItinerary(departureMin, arrivalMin);
        Duration durationMin = itineraryMin.getDuration();

        if (itineraryMin.getTransports().isEmpty()) {
            Walk walk = new Walk(departure, arrival);
            itineraryMin.addToFirstPosition(walk);
            return itineraryMin;
        }

        for (int i = 1; i < this.searchLimit; i++) {
            Station stationDepartureLoop = listDeparture.get(i);
            for (int j = 1; j < this.searchLimit; j++) {
                Station stationArrivalLoop = listArrival.get(j);
                if (stationDepartureLoop.equals(stationArrivalLoop)) {
                    continue;
                }
                Itinerary itineraryLoop =
                    bestItinerary(stationDepartureLoop, stationArrivalLoop);
                Duration durationLoop = itineraryLoop.getDuration();
                if (durationLoop.compareTo(durationMin) < 0) {
                    Walk walkDifference =
                        new Walk(arrivalMin.getCoordinates(),
                        stationArrivalLoop.getCoordinates());
                    double distance = walkDifference.getTravelDistance();
                    if (distance < this.searchDistance) {
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

        /** Valeur par défaut de la limite de recherche de stations proches. */
        private static final int DEFAULT_SEARCH_LIMIT = 5;

        /** Valeur par défaut de la limite de recherche si la valeur donnée
         * est incorrecte. */
        private static final int DEFAULT_SEARCH_LIMIT_EFFECTIVE = 1;

        /** Valeur par défaut de la dist. de recherche de stations proches. */
        private static final double DEFAULT_SEARCH_DISTANCE = 1000.0;

        /** Valeur par défaut de la dist. de recherche si la valeur donnée
         * est incorrecte. */
        private static final double DEFAULT_SEARCH_DISTANCE_EFFECTIVE = 10.0;

        /** Network surlequel sera alimenté le network. */
        private final Network network;

        /** Algorithme de plus court chemin. */
        private ShortestPathAlgorithm algorithm;

        /** Poids des noeuds. */
        private NodeSize nodeSize;

        /** Si la marche entre station est activé. */
        private boolean walking = true;

        /** Heure de départ. */
        private LocalTime departureTime;

        /** Coordonnées de départ. */
        private Double departureCoordinates;

        /** Coordonnées d'arrivée. */
        private Double arrivalCoordinates;

        /** Station de départ. */
        private Station departureStation;

        /** Station d'arrivée. */
        private Station arrivalStation;

        /** Limite de recherche de statiosn proches.  */
        private int builderSearchLimit = DEFAULT_SEARCH_LIMIT;

        /** Limite de distance de recherche de stations proches. */
        private double builderSearchDistance = DEFAULT_SEARCH_DISTANCE;

        /**
         * Construit un monteur.
         * @param network Network qui sera alimenté à l'algorithme
         */
        public Builder(final Network network) {
            this.network = network;
        }

        /**
         * Fixe l'algorithme de calcul du plus court chemin à Dijkstra.
         * @return Monteur dont l'algorithme de calcul du plus court chemin
         * a été fixé à Dijkstra.
         */
        public Builder setDijkstra() {
            this.algorithm = new Dijkstra(this.network);
            return this;
        }

        /**
         * Fixe le poids de l'algorithme de calcul du plus court chemin.
         * @param nodeSize le poids de l'algo. de calcul du plus court chemin.
         * @return Monteur dont le poids de l'algorithme de calcul du plus
         * court chemin a été fixé.
         */
        public Builder setNodeSize(final NodeSize nodeSize) {
            this.nodeSize = nodeSize;
            return this;
        }

        /**
         * Fixe le mode de transport à pied entre les stations.
         * @param walking s'il est possible de marcher à pied entre les stations
         * @return Monteur dont le mode de transport à pied entre les stations
         * à la marche est activé.
         */
        public Builder setWalking(final boolean walking) {
            this.walking = walking;
            return this;
        }

        /**
         * Fixe l'heure de départ de l'itinéraire.
         * @param departureTime l'heure de départ de l'itinéraire.
         * @return Monteur dont l'heure de départ a été fixée.
         */
        public Builder setDepartureTime(final LocalTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        /**
         * Fixe les coordonnées du point de départ.
         * @param departureCoordinates coordonnées du point de départ.
         * @return Monteur dont les coordonnées du pt. de départ ont été fixées
         */
        public Builder setDepartureCoordinates(final
            Double departureCoordinates) {
            this.departureCoordinates = departureCoordinates;
            return this;
        }

        /**
         * Fixe les coordonnées du point d'arrivée.
         * @param arrivalCoordinates coordonnées du point d'arrivée.
         * @return Monteur dont les coord. du point d'arrivée ont été fixées.
         */
        public Builder setArrivalCoordinates(final Double arrivalCoordinates) {
            this.arrivalCoordinates = arrivalCoordinates;
            return this;
        }

        /**
         * Fixe la station de départ.
         * @param departureStation station de départ.
         * @return Monteur dont la station de départ a été fixée.
         */
        public Builder setDepartureStation(final Station departureStation) {
            this.departureStation = departureStation;
            return this;
        }

        /**
         * Fixe la station d'arrivée.
         * @param arrivalStation station d'arrivée.
         * @return Monteur dont la station d'arrivée a été fixée.
         */
        public Builder setArrivalStation(final Station arrivalStation) {
            this.arrivalStation = arrivalStation;
            return this;
        }

        /**
         * Fixe la limite de recherche.
         * @param limit limite de recherche.
         * @return Monteur dont la limite de recherche a été fixée.
         */
        public Builder setSearchLimit(final int limit) {
            this.builderSearchLimit = limit;
            return this;
        }

        /**
         * Fixe la distance de recherche maximale.
         * @param distance distance de recherche maximale.
         * @return Monteur dont la distance de recherche maximale a été fixée.
         */
        public Builder setSearchDistance(final double distance) {
            this.builderSearchDistance = distance;
            return this;
        }

        /**
         * Construit un déplacement à partir des attributs.
         * @return Déplacement construit en fonction des paramètres fournis.
         */
        public Travel build() throws IllegalTravelException {
            if (this.network == null) {
                throw new IllegalTravelException("Le réseau n'est pas défini.");
            }

            if (this.algorithm == null) {
                this.algorithm = new Dijkstra(this.network);
                algorithm.setWalkingDistance(builderSearchDistance);
            }

            if (this.nodeSize == null) {
                this.nodeSize = NodeSize.TIME;
            }

            if (this.departureTime == null) {
                LocalTime now = LocalTime.now();
                departureTime = LocalTime.of(now.getHour(), now.getMinute());
            }

            if (departureCoordinates == null && departureStation == null) {
                throw new IllegalTravelException(
                    "Aucun point de départ n'a été défini.");
            }

            else if (departureCoordinates != null && departureStation != null) {
                throw new IllegalTravelException(
                    "Il ne peut y avoir qu'un unique point de départ.");
            }

            else if (arrivalCoordinates == null && arrivalStation == null) {
                throw new IllegalTravelException(
                    "Aucun point d'arrivée n'a été défini.");
            }

            else if (arrivalCoordinates != null && arrivalStation != null) {
                throw new IllegalTravelException(
                    "Il ne peut y avoir qu'un unique point d'arrivée.");
            }

            if (this.builderSearchLimit < 0) {
                this.builderSearchLimit = DEFAULT_SEARCH_LIMIT_EFFECTIVE;
            }

            if (this.builderSearchDistance < 0.0) {
                this.builderSearchDistance = DEFAULT_SEARCH_DISTANCE_EFFECTIVE;
            }

            if (this.departureStation != null) {
                this.departureCoordinates = departureStation.getCoordinates();
            }

            if (this.arrivalStation != null) {
                this.arrivalCoordinates = arrivalStation.getCoordinates();
            }

            return new Travel(this);
        }
    }
}
