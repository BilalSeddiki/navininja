package netnavi;
import java.util.*;
public class MinimalStation{
    private String nom;
    private ArrayList<String> lignes;
    /**
     * une station ayant le minimum d'information pour l'affichage
     */
    public MinimalStation(String n, ArrayList<String> ls){
        nom=n;
        lignes=ls;
    }
    /**
     * renvoie le nom de la station
     */
    public String getName(){
        return nom;
    }
    /**
     * renvoie la liste des lignes
     */
    public ArrayList<String> getLignes(){
        return lignes;
    }
}