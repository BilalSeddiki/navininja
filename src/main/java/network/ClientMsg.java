package netnavi;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import NetUtil;

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
    public static ArrayList<Schedule> schedule(Socket s,String station,LocalTime time){
        String msg="hs"
        String tmp=time.toString();
        msg.concat(station.length());
        msg.concat(station);
        msg.concat(tmp.length());
        msg.concat(tmp);
        NetUtil.send(s,msg);
        String msg2=NetUtil.receive(s);
        ArrayList<Schedule> ret=new ArrayList<Schedule>();
        int size=msg2.charAt(0);
        int current0=0;
        for(int i=0;i<size;i++){
            int i1=a.charAt(current0);
            String d=a.substring(current0+1,current0+i1);
            int i2=a.charAt(current0+i1);
            String p=a.substring(current0+i1+1,current0+i1+i2);
            current0=current0+i1+i2
            ret.add(new Schedule(d,p));
        }

    }
}