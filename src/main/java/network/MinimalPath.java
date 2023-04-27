package netnavi;

public class MinimalPath{
    public String source;
    public String destination;
    public String lineName;
    public String variant;
    public String duration;
    /**
     * Construit Un Path qui a les information minimal, c'est a dire les seul informations qui sont utile a l'utilisateur
     * @param s La source du Path, peut etre une station ou des coordonnée GPS
     * @param d La destinattion du Path, peut etre une station ou des coordonnée GPS
     * @param ln La ligne de tranport qui est utilisée, vaut marche quand c'est de la marche a pied qui est utilisé pour le path
     * @param v Le variant de ligne qui est utilisée, est vide quand c'est de la marche a pied qui est utilisé pour le path
     * @param dura La durée du Path
     */
    public MinimalPath(String s,String d,String ln,String v,String dura){
        source=s;
        destination=d;
        lineName=ln;
        variant=v;
        duration=dura;
    }
}