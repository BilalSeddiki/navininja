package network;

public class MinimalPath{
    public String source;
    public String destination;
    public String lineName;
    public String variant;
    public String duration;
    /**
     * Construit un Minimal Path qui a les information minimal necessaire pour l'interface graphique
     * @param s le nom de la station de la source / ou les coordonnées GPS de la source
     * @param d le nom de la station de destination/ ou les coordonnées GPS de destination
     * @param ln la ligne du reseau, vaut "marche" si c'est de la marche a pied
     * @param v le variant de la ligne, vide si ln vaut "marche"
     * @param dura le temp de trajet du Path
     */
    public MinimalPath(String s,String d,String ln,String v,String dura){
        source=s;
        destination=d;
        lineName=ln;
        variant=v;
        duration=dura;
    }
}