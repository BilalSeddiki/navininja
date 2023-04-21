package netnavi;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import NetUtil;

/* la classe qui gèrent les message reseaux du server*/
public class ServerMsg {

    public static void sendSchedules(Socket c, ArrayList<Schedule> s){
        Sring msg="";
        char size=(char)s.size();
        msg.concat(String.valueOf(size));
        for(Schedule a: s){
            msg.concat(a.serial);
        }
        NetUtil.send(c,msg);
    }
    
}
