package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Network {
    private HashMap<String, Station> stations;

    public Network(ArrayList<Station> stationList) {
        stations = new HashMap<String, Station>();
        stationList.forEach(s -> stations.put(s.getName(), s));
    }

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

