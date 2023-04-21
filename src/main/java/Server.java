import model.Network;
import utils.Globals;
import java.net.*;
import netnavi.ServerMsg;

public class Server{
    public static void main(String[] args) {
        var network = Network.fromCSV(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
        try{
            ServerSocket serveurSocket = new ServerSocket(5000);
            while(true){
                Socket clientSocket=serveurSocket.accept();
                BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
                String msg=in.readLine();
                if(msg.charAt(0)=='h' && msg.charAt(1)=='s'){
                    Arraylist<Schedule> tmp=traitementSchedule(network,msg.substring(2,msg.length())); // a modifier pour envoyer un local time et une string
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