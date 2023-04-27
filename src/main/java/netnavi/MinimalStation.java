package netnavi;
import java.util.*;
public class MinimalStation{
    private String nom;
    private ArrayList<String> lignes;
    /**
     * Construit une Minimal Station qui a les information minimal necessaire pour l'interface graphique
     * @param n Le nom de la Station
     * @param ls Toutes les Lignes qui passent a cette station
     */
    public MinimalStation(String n, ArrayList<String> ls){
        nom=n;
        lignes=ls;
    }
    /**
     * Getter pour obtenir le nom de la Station
     */
    public String getName(){
        return nom;
    }
    /**
     * Getter pour obtenir la liste des lignes qui passent a cette station
     */
    public ArrayList<String> getLignes(){
        return lignes;
    }
}