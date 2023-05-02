package network;

import java.net.*;
import java.io.*;
import java.time.LocalTime;
import model.*;
import network.*;

import java.util.*;
import java.time.Duration;

/* La classe qui gèrent les messages reseaux que peut envoyer le serveur*/
public class ServerMsg {

    /**
     * Envoie les Schedules demandés a l'interface Graphique
     * @param c Le Socket de l'interface Graphique
     * @param s L'ArrayList de Schedule a envoyer a l'interface Graphique
     */
    public static void sendSchedules(Socket c, ArrayList<Schedule> s){
        String msg="";
        char size=(char)s.size();
        msg=msg.concat(String.valueOf(size));
        for(Schedule a: s){
            msg=msg.concat(a.serial());
        }
        NetUtil.send(c,msg);
    }

    /**
     * Envoie le chemin le plus court a l'interface graphique
     * @param c Le Socket reseau de l'interface graphique
     * @param a L'itineraire a envoyer a l'interface graphique
     */
    public static void sendBestPath(Socket c, Itinerary a){
        String msg="";
        var tmp=a.getPaths();
        msg=msg.concat(String.valueOf((char)tmp.size()));
        for(var t:tmp){
            msg=msg.concat(String.valueOf((char)t.getSource().toString().length()));
            msg=msg.concat(t.getSource().toString());
            msg=msg.concat(String.valueOf((char)t.getDestination().toString().length()));
            msg=msg.concat(t.getDestination().toString());
            msg=msg.concat(String.valueOf((char)t.getLineName().length()));
            msg=msg.concat(t.getLineName());
            msg=msg.concat(String.valueOf((char)t.getVariant().length()));
            msg=msg.concat(t.getVariant());
            Duration duration=t.getTravelDuration();
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;
            String d=String.format("%02d:%02d:%02d", hours, minutes, seconds);
            msg=msg.concat(String.valueOf((char)d.length()));
            msg=msg.concat(d);
        }
        NetUtil.send(c,msg);
    }
    /**
     * Envoie la listes des station avec les lignes qui y passent a l'interface graphique
     * @param clientSocket Le socket reseau de l'interface graphique
     * @param stations L'Arraylist qui contient les stations a envoyer
     */
    public static void sendStations(Socket clientSocket,ArrayList<MinimalStation>stations){
        String msg="";
        char size=(char)stations.size();
        msg=msg.concat(String.valueOf(size));
        for(var s:stations){
            msg=msg.concat(String.valueOf((char)s.getName().length()));
            msg=msg.concat(s.getName());
            ArrayList<String> lignes=s.getLignes();
            for(var l:lignes){
                msg=msg.concat(String.valueOf((char)l.length()));
                msg=msg.concat(l);
            }
        }
        NetUtil.send(clientSocket,msg);
    }
    
}
