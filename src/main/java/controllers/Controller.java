package controllers;

import model.Network;
import utils.Globals;

import java.io.IOException;

import csv.CsvData;

/**
 * Classe mère des contrôleurs, permet principalement de définir le contrôleur de navigation
 *  * @author R. MARTINI
 */
public class Controller {
    /**
     * Instance du controlleur de navigation
     */
    public NavigationController navigationController;


    public Network network;

    public Controller() {
        try {
            this.network = CsvData.makeNetwork(Globals.pathToRessources("map_data.csv"),
                    Globals.pathToRessources("timetables.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Setter du controlleur de navigation
     * @param navigationController instance du controlleur de navigation
     */
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

}
