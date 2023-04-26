import model.*;
import utils.Globals;
import java.net.*;
import netnavi.ServerMsg;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class Server{
    public static void main(String[] args) {
        try{
            var network = Network.fromCSV(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
            ServerSocket serveurSocket = new ServerSocket(51312);
            while(true){
                Socket clientSocket=serveurSocket.accept();
                BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
                String msg=in.readLine();
                
                if(msg.charAt(0)=='h' && msg.charAt(1)=='s'){
                    ArrayList<Schedule> tmp=new ArrayList<Schedule>();
                    tmp=network.traitementSchedule(msg.substring(2,msg.length())); 
                    ServerMsg.sendSchedules(clientSocket,tmp);
                    clientSocket.close();
                }else{
                    clientSocket.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
}