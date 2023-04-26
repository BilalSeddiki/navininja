package netnavi;

import java.net.*;
import java.io.*;
import netnavi.NetUtil;
import java.time.LocalTime;
import model.*;
import java.util.*;

/* la classe qui gèrent les message reseaux du server*/
public class ServerMsg {

    public static void sendSchedules(Socket c, ArrayList<Schedule> s){
        String msg="";
        char size=(char)s.size();
        msg.concat(String.valueOf(size));
        for(Schedule a: s){
            msg.concat(a.serial());
        }
        NetUtil.send(c,msg);
    }
    
}
