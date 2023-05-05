package model;

import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStation {

    /**
     * Teste le constructeur ainsi que les getters de la classe Station.
     */
    @Test
    public void testConstructorAndGetters() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        assertEquals(name, station.getName(), "L'attribut name est incorrect.");
        assertEquals(coordinates, station.getCoordinates(),
            "L'attribut coordinates est incorrect.");
    }

    /**
     * Teste la création d'une station et que sa latitude soit comprise
     * entre -90.0 et 90.0.
     */
    @Test
    public void testConstructorValidLatitude() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        double latitude = station.getCoordinates().x;
        assertTrue((latitude >= -90.0) && (latitude <= 90.0),
            "La latitude doit être comprise en -90 degrés et 90 degrés.");
    }

    /**
     * Teste la création d'une station et que sa longitude soit comprise
     * entre -180.0 et 180.0.
     */
    @Test
    public void testConstructorValidLongitude() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        double longitude = station.getCoordinates().y;
        assertTrue((longitude >= -180.0) && (longitude <= 180.0),
            "La longitude doit être comprise en -180 degrés et 180 degrés.");
    }
}
