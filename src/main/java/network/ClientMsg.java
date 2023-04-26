package netnavi;

import java.net.*;
import java.io.*;
import netnavi.NetUtil;
import java.time.LocalTime;
import model.*;
import java.util.*;

/* la classe qui gèrent les message reseaux de l'interface */
public class ClientMsg {

   

    /*public static String bestPath(Socket s, String stationa, String stationb,LocalTime time){
        String msg="bp"
        String tmp=time.toString();
        msg.concat(stationa.length());
        msg.concat(stationa);
        msg.concat(stationb.length());
        msg.concat(stationb);
        msg.concat(tmp.length());
        msg.concat(tmp);
        NetUtil.send(s,msg);
        return receive(s);
    }*/
    public static ArrayList<Schedule> schedule(Socket s,String station,LocalTime time, String line){
        String msg="hs";
        String tmp=time.toString();
        msg.concat(String.valueOf((char)station.length()));
        msg.concat(station);
        msg.concat(String.valueOf((char)tmp.length()));
        msg.concat(tmp);
        msg.concat(String.valueOf((char)line.length()));
        msg.concat(line);
        NetUtil.send(s,msg);
        String msg2=NetUtil.receive(s);
        ArrayList<Schedule> ret=new ArrayList<Schedule>();
        int size=msg2.charAt(0);
        int current0=0;
        for(int i=0;i<size;i++){
            int i1=msg2.charAt(current0);
            String d=msg2.substring(current0+1,current0+i1+1);
            int i2=msg2.charAt(current0+i1+1);
            String p=msg2.substring(current0+i1+2,current0+i1+i2+1);
            current0=current0+i1+i2+1;
            ret.add(new Schedule(d,p));
        }

    }
}