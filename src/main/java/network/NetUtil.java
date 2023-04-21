package netnavi;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class NetUtil{

    public static void send(Socket s,String msg){
        try{
            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.println(msg);
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static String receive(Socket s){
        try{
            BufferedReader in = new BufferedReader (new InputStreamReader (s.getInputStream()));
            String msg = in.readLine();
            in.close();
            return msg;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

}