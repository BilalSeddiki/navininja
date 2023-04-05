package utils;

/**
 * Classe statique pour définir les variables globales du logiciel
 */
public class Globals {
    /**
     * Fonction statique chargée de construire le chemin ver la vue depuis la racine du projet.
     * @param viewFileName nom du fichier fxml de la vue
     * @return chemin vers la vue depuis la racine
     */
    public static String pathToView(String viewFileName){
        return "src/main/resources/views/"+viewFileName;
    }
    public static String pathToStyleSheetFromController(String stylesheet){
        return "../views/style/"+stylesheet;
    }

    public static String pathToRessources(String fileName) {
        return "src/main/resources/" + fileName;
    }

    /**
     * Fonction statique pour récupérer la largeur de l'écran
     * @return la largeur de l'écran
     */
    public static int windowWidth(){
        return 1040;
    }
    /**
     * Fonction statique pour récupérer la hauteur de l'écran
     * @return la hauteur de l'écran
     */
    public static int windowHeight(){
        return 700;
    }
}
