package network;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

/* Classe d'utilitaire pour les envoies et receptions reseau */
public class NetUtil{

    /**
     * Envoie un Message via le Socket donné
     * @param s Le Socket reseaux connecté a un autre Socket 
     * @param msg Le msg reseau a envoyer 
     */
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
    /**
     * Receptionne un Message qui arrive sur le Socket, fonction bloquante tant qu'un message n'est pas recus
     * @param s Le socket reseau connecté a un autre Socket 
     */
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