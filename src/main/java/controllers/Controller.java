package controllers;

/**
 * Classe mère des contrôleurs, permet principalement de définir le contrôleur de navigation
 *  * @author R. MARTINI
 */
public class Controller {
    /**
     * Instance du controlleur de navigation
     */
    public NavigationController navigationController;

    /**
     * Setter du controlleur de navigation
     * @param navigationController instance du controlleur de navigation
     */
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

}
