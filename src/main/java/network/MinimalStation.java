package netnavi;
import java.util.*;
public class MinimalStation{
    String nom;
    ArrayList<String> lignes;
    public MinimalStation(String n, ArrayList<String> ls){
        nom=n;
        lignes=ls;
    }
    public String getName(){
        return nom;
    }
    public ArrayList<String> getLignes(){
        return lignes;
    }
}