import model.*;
import network.MinimalStation;
import network.ServerMsg;
import utils.Globals;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.time.*;

public class Server{
    /**
     * Le serveur qui gerent les appel au modèle  
     * La logique est qu'il prend une demande de connexion, la traite, la ferme et en attend une autre
     * */ 
    public static void main(String[] args) {
        try{
            var network = Network.fromCSV(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
            ServerSocket serveurSocket = new ServerSocket(51312);
            while(true){
                Socket clientSocket=serveurSocket.accept();
                BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
                String msg=in.readLine();
                
                if(msg.charAt(0)=='h' && msg.charAt(1)=='s'){
                    int current0=0;
                    int sl=msg.charAt(current0);
                    String name=msg.substring(current0+1,current0+sl+1);
                    current0=current0+sl+1;
                    int ltl=msg.charAt(current0);
                    String lttmp=msg.substring(current0+1,current0+ltl+1);
                    current0=current0+ltl+1;
                    LocalTime time=LocalTime.parse(lttmp);
                    int ll=msg.charAt(current0);
                    String line=msg.substring(current0+1,current0+ll+1);
                    ArrayList<Schedule> tmp=new ArrayList<Schedule>();
                    tmp=network.traitementSchedule(name,line,time); 
                    ServerMsg.sendSchedules(clientSocket,tmp);
                    clientSocket.close();
                }else if(msg.charAt(0)=='b' && msg.charAt(1)=='p'){
                    int current0=2;
                    int l=msg.charAt(current0);
                    String source=msg.substring(current0+1,current0+l+1);
                    current0=current0+l+1;
                    l=msg.charAt(current0);
                    String destination=msg.substring(current0+1,current0+l+1);
                    current0=current0+l+1;
                    l=msg.charAt(current0);
                    String tmp=msg.substring(current0+1,current0+l+1);
                    current0=current0+l+1;
                    LocalTime time=LocalTime.parse(tmp);
                    Itinerary a=network.bestPath(network.getStation(source), network.getStation(destination),time);
                    ServerMsg.sendBestPath(clientSocket,a);
                }else if(msg.charAt(0)=='l' && msg.charAt(1)=='p'){
                    ArrayList<MinimalStation> stations=network.allStation();
                    ServerMsg.sendStations(clientSocket,stations);
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