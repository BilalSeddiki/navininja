package netnavi;

import java.net.*;
import java.io.*;
import netnavi.*;
import java.time.LocalTime;
import model.*;
import java.util.*;
import java.time.Duration;

/* la classe qui gèrent les message reseaux du server*/
public class ServerMsg {

    public static void sendSchedules(Socket c, ArrayList<Schedule> s){
        String msg="";
        char size=(char)s.size();
        msg=msg.concat(String.valueOf(size));
        for(Schedule a: s){
            msg=msg.concat(a.serial());
        }
        NetUtil.send(c,msg);
    }
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
