package netnavi;

import java.net.*;
import java.io.*;
import netnavi.*;
import java.time.LocalTime;
import model.*;
import java.util.*;

/* la classe qui gèrent les message reseaux de l'interface */
public class ClientMsg {

    /**
     * renvoie une ArrayList de minimal Station
     * @param ip l'adress ip du serv
     * @param source le nom de la station de depart/ ou les coordonnée quand best path pourra prendre les coordonné
     * @param destination l'arrivé 
     * @param time l'heure de depart
    */
    public static List<MinimalPath> bestPath(String ip,String source,String destination,LocalTime time){
        try{
            Socket s=new Socket(ip,51312);
            return bestPath(s,source,destination,time);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
   

    public static List<MinimalPath> bestPath(Socket s, String source, String destination,LocalTime time){
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
        List<MinimalPath> ret=new ArrayList<MinimalPath>();
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
     * renvoie la liste de schedule après l'heure donné a une station pour une ligne
     * @param ip l'adress du serv
     * @param station la station
     * @param time le temp a partir duquel on veut les schedule
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
     * renvoie une ArrayList de minimal Station
     * @param ip l'adress ip du serv
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