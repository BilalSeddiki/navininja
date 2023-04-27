package netnavi;

public class MinimalPath{
    public String source;
    public String destination;
    public String lineName;
    public String variant;
    public String duration;
    /**
     * un path minimal entre Deux station/deux point de coord gps
     */
    public MinimalPath(String s,String d,String ln,String v,String dura){
        source=s;
        destination=d;
        lineName=ln;
        variant=v;
        duration=dura;
    }
}