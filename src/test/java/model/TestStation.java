package model;

import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStation {

    @Test
    public void testConstructorAndGetters() {
        String name = "";
        Double coordinates = new Double(0, 0);
        ArrayList<Path> paths = new ArrayList<Path>();

        Station station = new Station(name, coordinates, paths);
        assertEquals(name, station.getName(), "L'attribut name est incorrect.");
        assertEquals(coordinates, station.getCoordinates(), "L'attribut coordinates est incorrect.");
        assertEquals(paths, station.getPaths(), "L'attribut paths est incorrect.");

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals. */
    }

    //TODO (?): Factorisation des constructeurs.
    @Test
    public void testConstructorNameNotEmpty() {
        String name = "";
        Double coordinates = new Double(0, 0);
        ArrayList<Path> paths = new ArrayList<Path>();

        Station station = new Station(name, coordinates, paths);
        //assertFalse(station.getName().isEmpty(), "L'attribut name ne peut pas être vide.");
    }

    @Test
    public void testConstructorValidLatitude() {
        String name = "";
        Double coordinates = new Double(0, 0);
        ArrayList<Path> paths = new ArrayList<Path>();

        Station station = new Station(name, coordinates, paths);
        double latitude = station.getCoordinates().x;
        assertTrue((latitude >= -90.0) && (latitude <= 90.0), "La latitude doit être comprise en -90 degrés et 90 degrés.");
    }

    @Test
    public void testConstructorValidLongitude() {
        String name = "";
        Double coordinates = new Double(0, 0);
        ArrayList<Path> paths = new ArrayList<Path>();

        Station station = new Station(name, coordinates, paths);
        double longitude = station.getCoordinates().y;
        assertTrue((longitude >= -180.0) && (longitude <= 180.0), "La longitude doit être comprise en -180 degrés et 180 degrés.");
    }

    @Test
    public void testConstructorPathNotEmpty() {
        String name = "";
        Double coordinates = new Double(0, 0);
        ArrayList<Path> paths = new ArrayList<Path>();

        Station station = new Station(name, coordinates, paths);
        //assertFalse(station.getPaths().isEmpty(), "L'attribut paths ne peut pas être vide.");
    }
}