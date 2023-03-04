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

        /* TODO (1 ?): assertEquals -> assertSame avec redéfinition de equals.
         * TODO (2 ?): 
         * Conditions de création (nom non vide, liste de chemins non vide, ...).
         * -> Plusieurs tests (constructeur correct, constructeur incorrect).
         */
    }
}