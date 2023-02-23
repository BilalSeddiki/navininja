package model;

import java.util.ArrayList;

/** Une station du réseau */
public class Station {
    /** Le nom de la station. */
    private String name;
    /* private ??? coordinates; */
    /** Les differents chemins que l'on peut prendre à partir de la station. */
    private ArrayList<Path> paths;

    /**
     * Renvoie les differents chemins que l'on peut prendre à partir de la station.
     * @return une liste de chemin
     */
    public ArrayList<Path> getPaths() {
        return paths;
    }
    
    /**
     * Renvoie le nom de la station.
     * @return le nom de la station
     */
    public String getName() {
        return name;
    }
}
