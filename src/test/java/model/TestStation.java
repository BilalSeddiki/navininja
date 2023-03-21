package model;

import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStation {

    @Test
    public void testConstructorAndGetters() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        assertEquals(name, station.getName(), "L'attribut name est incorrect.");
        assertEquals(coordinates, station.getCoordinates(), "L'attribut coordinates est incorrect.");

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals. */
    }

    //TODO (?): Factorisation des constructeurs.
    @Test
    public void testConstructorNameNotEmpty() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        //assertFalse(station.getName().isEmpty(), "L'attribut name ne peut pas être vide.");
    }

    @Test
    public void testConstructorValidLatitude() {
        String name = "";
        Double coordinates = new Double(0, 0);

        Station station = new Station(name, coordinates);
        double latitude = station.getCoordinates().x;
        assertTrue((latitude >= -90.0) && (latitude <= 90.0),
                "La latitude doit être comprise en -90 degrés et 90 degrés.");
    }

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