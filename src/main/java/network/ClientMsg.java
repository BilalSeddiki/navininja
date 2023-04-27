package netnavi;

import java.net.*;
import java.io.*;
import netnavi.*;
import java.time.LocalTime;
import model.*;
import java.util.*;

/* La classe qui gèrent les messages reseaux que l'interface graphique peut envoyer et les reponse du serveur*/
public class ClientMsg {

    /**
     * Fait une demande de bestPath au serveur et renvoie la reponse sous un format de liste de MinimalPath
     * @param ip l'adresse ip du serveur
     * @param source le nom de la station de depart ou les coordonnée GPS du depart
     * @param destination le nom de la station d'arrivée ou les coordonnée GPS de l'arrivée
     * @param time l'heure de départ
     * @return une ArrayList de MinimalPath
    */
    public static ArrayList<MinimalPath> bestPath(String ip,String source,String destination,LocalTime time){
        try{
            Socket s=new Socket(ip,51312);
            return bestPath(s,source,destination,time);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
   
    /**
     * Fait une demande de bestPath au serveur et renvoie la reponse sous un format de liste de MinimalPath
     * @param s Le Socket reseau connecté au serveur
     * @param source Le nom de la station de depart ou les coordonnée GPS du depart
     * @param destination Le nom de la station d'arrivée ou les coordonnée GPS de l'arrivée
     * @param time L'heure de départ
     * @return une ArrayList de MinimalPath
    */
    public static ArrayList<MinimalPath> bestPath(Socket s, String source, String destination,LocalTime time){
        String msg="bp";
        String tmp=time.toString();
        msg=msg.concat(String.valueOf((char)source.length()));
        msg=msg.concat(source);
        msg=msg.concat(String.valueOf((char)destination.length()));
        msg=msg.concat(destination);
        msg=msg.concat(String.valueOf((char)tmp.length()));
        msg=msg.concat(tmp);
        NetUtil.send(s,msg);
        String msg2=NetUtil.receive(s);
        ArrayList<MinimalPath> ret=new ArrayList<MinimalPath>();
        int size=(int)msg2.charAt(0);
        int current0=1;
        for(int i=0;i<size;i++){
            int i1=(int)msg2.charAt(current0);
            String source2=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            i1=(int)msg2.charAt(current0);
            String destination2=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            i1=(int)msg2.charAt(current0);
            String lineName=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            i1=(int)msg2.charAt(current0);
            String variant=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            i1=(int)msg2.charAt(current0);
            String duration=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            ret.add(new MinimalPath(source2,destination2,lineName,variant,duration));
        }
        return ret;
    }


    /**
     * Fait une demande de Schedule au serveur pour une ligne a une station precise et renvoie la reponse sous un format de liste de Schedule
     * @param ip l'adresse ip du serveur
     * @param station la station demandée
     * @param time le temps a partir du quel, les schedules sont demandés
     * @param line la ligne demandée
     * @return une ArrayList de Schedule
     */
    public static ArrayList<Schedule> schedule(String ip,String station,LocalTime time,String line){
        try{
            Socket s=new Socket(ip,51312);
            return schedule(s,station,time,line);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Fait une demande de Schedule au serveur pour une ligne a une station precise et renvoie la reponse sous un format de liste de Schedule
     * @param s Le Socket reseau connecté au serveur
     * @param station la station demandée
     * @param time le temps a partir du quel, les schedules sont demandés
     * @param line la ligne demandée
     * @return une ArrayList de Schedule
     */
    public static ArrayList<Schedule> schedule(Socket s,String station,LocalTime time, String line){
        String msg="hs";
        String tmp=time.toString();
        msg=msg.concat(String.valueOf((char)station.length()));
        msg=msg.concat(station);
        msg=msg.concat(String.valueOf((char)tmp.length()));
        msg=msg.concat(tmp);
        msg=msg.concat(String.valueOf((char)line.length()));
        msg=msg.concat(line);
        NetUtil.send(s,msg);
        String msg2=NetUtil.receive(s);
        ArrayList<Schedule> ret=new ArrayList<Schedule>();
        int size=(int)msg2.charAt(0);
        int current0=1;
        for(int i=0;i<size;i++){
            int i1=(int)msg2.charAt(current0);
            String d=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            int i2=(int)msg2.charAt(current0);
            String p=msg2.substring(current0+1,current0+i2+1);
            current0=current0+i2+1;
            ret.add(new Schedule(d,p));
        }
        return ret;

    }
    /**
     * renvoie une liste de toute les stations du reseau de transport avec les lignes qui y correspondent
     * @param ip l'adresse ip du serveur
     * @return une ArrayList de MinimalStation 
    */
    public static ArrayList<MinimalStation> listeStations(String ip){
        try{
            Socket s=new Socket(ip,51312);
            return listeStations(s);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * renvoie une liste de toute les stations du reseau de transport avec les lignes qui y correspondent
     * @param s le Socket reseau connecté au serveur
     * @return une ArrayList de MinimalStation 
    */
    public static ArrayList<MinimalStation> listeStations(Socket s){
        String msg="ls";
        NetUtil.send(s,msg);
        String msg2=NetUtil.receive(s);
        ArrayList<MinimalStation> ret=new ArrayList<MinimalStation>();
        int size=(int)msg2.charAt(0);
        int current0=1;
        for( int i=0;i<size;i++){
            int i1=(int)msg2.charAt(current0);
            String n=msg2.substring(current0+1,current0+i1+1);
            current0=current0+i1+1;
            int li=(int)msg2.charAt(current0);
            current0=current0+1;
            ArrayList<String> lignes=new ArrayList<String>();
            for(int j=0;i<li;i++){
                int li1=(int)msg2.charAt(current0);
                String l=msg2.substring(current0+1,current0+li1+1);
                current0=current0+li1+1;
                lignes.add(l);
            }
            ret.add(new MinimalStation(n,lignes));
        }
        return ret;
    }
}