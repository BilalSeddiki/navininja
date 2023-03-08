package model;

import java.util.HashMap;

public class Network {
    private HashMap<String, Station> stations;

    public boolean hasStation(String name) {
        return stations.containsKey(name);
    }

    public Station getStation(String name) {
        return stations.get(name);
    }

    public Itinerary bestPath(Station source, Station destination) {
        return null;
    }
}
