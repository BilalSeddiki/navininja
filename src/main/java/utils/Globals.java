package utils;

/**
 * Classe statique pour définir les variables globales du logiciel.
 */
public final class Globals {
    /**
     * constante de largeur.
     */
    private static final int WIDTH = 1040;
    /**
     * constante de height.
     */
    private static final int HEIGHT = 700;
    private Globals() { };
    /**
     * Fonction statique chargée de construire le chemin vers la vue
     * depuis la racine du projet.
     * @param viewFileName nom du fichier fxml de la vue
     * @return chemin vers la vue depuis la racine
     */
    public static String pathToView(final String viewFileName) {
        return "src/main/resources/views/" + viewFileName;
    }
    /**
     * Fonction statique chargée de construire le chemin
     * vers la feuille de style depuis la racine du projet.
     * @param style nom du fichier fxml de la feuille de style
     * @return chemin vers la feuille de style depuis la racine
     */
    public static String pathToStyleSheetFromController(final String style) {
        return "../views/style/" + style;
    }
    /**
     * Fonction statique chargée de construire le chemin
     * vers la ressource depuis la racine du projet.
     * @param fileName nom du fichier demandé
     * @return chemin vers la ressource depuis la racine
     */
    public static String pathToRessources(final String fileName) {
        return "src/main/resources/" + fileName;
    }

    /**
     * Fonction statique pour récupérer la largeur de l'écran.
     * @return la largeur de l'écran
     */
    public static int windowWidth() {
        return WIDTH;
    }
    /**
     * Fonction statique pour récupérer la hauteur de l'écran.
     * @return la hauteur de l'écran
     */
    public static int windowHeight() {
        return HEIGHT;
    }
}
