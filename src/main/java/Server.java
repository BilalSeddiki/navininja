import model.*;
import network.MinimalStation;
import network.ServerMsg;
import shortestpath.Dijkstra;
import utils.Globals;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

import org.apache.commons.collections4.IterableGet;

import java.time.*;
import csv.CsvData;
import utils.IllegalTravelException;
import java.awt.geom.Point2D;


public class Server{

    /**
     * This enum helps recognizing the input format the user chose*
     * * @author R. MARTINI
     */
    private enum InputFormat {
        STATION_NAME, COORDINATES, INVALID
    }
    /**
     * Fonction pour identifier le type de la saisie de l'utilisateur
     * @param input texte saisi par l'utilisateur
     * @return StationName, Coordinates, invalid
     * * @author R. MARTINI
     */
    private static InputFormat checkInputFormat(String input) {
        String stationNamePattern = "^[a-zA-ZâêàéèœŒÂÊÉÈÀ'\\-\\s]+$"; // Regex matche les noms des stations
        String coordinatesPattern = "^-?\\d+(\\.\\d+)?[\\s]*,[\\s]*-?\\d+(\\.\\d+)?$"; // regex matche les coordonnées lat,long
        if (input.matches(stationNamePattern)) {
            return InputFormat.STATION_NAME;
        } else if (input.matches(coordinatesPattern)) {
            return InputFormat.COORDINATES;
        } else {
            return InputFormat.INVALID;
        }
    }

        /**
     * Fonction qui retourne l'itinéraire à effectuer
     * @param inputFormatA format de la saisie départ
     * @param inputFormatB format de la saisie arrivée
     * @param inputA saisie départ
     * @param inputB saisie arrovée
     * @return itinéraire
     * @throws IllegalTravelException dans le cas où le voyage est impossible à réaliser
     * * @author R. MARTINI
     */
    private static Itinerary getItinerary(Network network, InputFormat inputFormatA, InputFormat inputFormatB, String inputA , String inputB,LocalTime time) throws IllegalTravelException {
        Point2D.Double coordA;
        Point2D.Double coordB;
        Dijkstra algorithm = new Dijkstra(network);
        Travel travel;
        if( inputFormatA == InputFormat.COORDINATES && inputFormatB == InputFormat.COORDINATES){
            //both inputs are coordinates
            String[] latlongA = inputA.split("[,]");
            coordA = new Point2D.Double(Double.parseDouble(latlongA[0]),Double.parseDouble(latlongA[1]));
            String[] latlongB = inputB.split("[,]");
            coordB = new Point2D.Double(Double.parseDouble(latlongB[0]),Double.parseDouble(latlongB[1]));
            travel = new Travel
                    .Builder(algorithm)
                    .setDepartureCoordinates(coordA)
                    .setArrivalCoordinates(coordB)
                    .setDepartureTime(time)
                    .build();
        }else if(inputFormatA == InputFormat.COORDINATES ){
            String[] latlongA = inputA.split("[,]");
            coordA = new Point2D.Double(Double.parseDouble(latlongA[0]),Double.parseDouble(latlongA[1]));
            travel = new Travel
                    .Builder(algorithm)
                    .setDepartureCoordinates(coordA)
                    .setArrivalStation(network.getStation(inputB))
                    .setDepartureTime(time)
                    .build();
        }else if(inputFormatB == InputFormat.COORDINATES){
            String[] latlongB = inputB.split("[,]");
            coordB = new Point2D.Double(Double.parseDouble(latlongB[0]),Double.parseDouble(latlongB[1]));
            travel = new Travel
                    .Builder(algorithm)
                    .setDepartureStation(network.getStation(inputA))
                    .setArrivalCoordinates(coordB)
                    .setDepartureTime(time)
                    .build();
        }else{
            //both are stations
            travel = new Travel
                    .Builder(algorithm)
                    .setDepartureStation(network.getStation(inputA))
                    .setArrivalStation(network.getStation(inputB))
                    .setDepartureTime(time)
                    .build();
        }

        return travel.createItinerary();
    }
    private static Itinerary bestPath(Network network,String inputA , String inputB,LocalTime time) throws IllegalTravelException{
        var FA = checkInputFormat(inputA);
        var FB = checkInputFormat(inputB);
        return getItinerary(network,FA, FB, inputA, inputB,time);
    }

    /**
     * Le serveur qui gerent les appel au modèle  
     * La logique est qu'il prend une demande de connexion, la traite, la ferme et en attend une autre
     * */ 
    public static void main(String[] args) {
        try{
            var network = CsvData.makeNetwork(Globals.pathToRessources("map_data.csv"), Globals.pathToRessources("timetables.csv"));
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
                    try{
                        Itinerary itinerary=bestPath(network, destination, tmp, time);
                        ServerMsg.sendBestPath(clientSocket,itinerary);
                    }catch(IllegalTravelException e){
                        ServerMsg.sendError(clientSocket);
                    }
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