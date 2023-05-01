package controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Itinerary;
import model.Transport;
import shortestpath.Dijkstra;
import utils.IllegalTravelException;
import java.awt.geom.Point2D;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * Controlleur de la vue trouver un itinéraire
 * @author R. MARTINI
 */
public class FindARouteController extends Controller {

    @FXML
    Label titleTotalDuration;
    @FXML
    Label labelTotalDuration;
    @FXML
    Button goBackBtn;
    @FXML
    ComboBox<String> hourComboBoxA;
    @FXML
    ComboBox<String> minComboBoxA;
    @FXML
    Button searchBtn;
    @FXML
    ContextMenu suggestionMenu;
    @FXML
    TextField coordinatesAInput;
    @FXML
    TextField coordinatesBInput;
    @FXML
    TableView<Transport> itineraryTable;
    @FXML
    TableColumn<Transport, String> startColumn;
    @FXML
    TableColumn<Transport, String> endColumn;
    @FXML
    TableColumn<Transport, String> lineColumn;
    @FXML
    TableColumn<Transport, Duration> durationColumn;

    /**
     * This enum helps recognizing the input format the user chose
     */
    private enum InputFormat {
        STATION_NAME, COORDINATES, INVALID
    }

    /**
     * Liste contenant les heures de la journée
     */
    private final ObservableList<String> hours = FXCollections.observableArrayList(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
    );
    /**
     * Liste contenant les minutes
     */
    private final ObservableList<String> minutes = FXCollections.observableArrayList();

    public void initialize() {
        //Initialisation de la liste des minutes
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }

        final ObservableList<String> stations = FXCollections.observableArrayList(network.getStationsByName().keySet());
        hourComboBoxA.getItems().addAll(hours);
        minComboBoxA.getItems().addAll(minutes);

        hourComboBoxA.setValue(String.valueOf(LocalTime.now().getHour()));
        minComboBoxA.setValue(String.valueOf(LocalTime.now().getMinute()));

        coordinatesAInput.textProperty().addListener(new TextFieldListener( coordinatesAInput, suggestionMenu, stations, network));
        coordinatesBInput.textProperty().addListener( new TextFieldListener( coordinatesBInput, suggestionMenu, stations, network));
        itineraryTable.getColumns().clear();
        itineraryTable.setVisible(false);
        titleTotalDuration.setVisible(false);
        labelTotalDuration.setVisible(false);

    }


    /**
     * Fonction pour identifier le type de la saisie de l'utilisateur
     * @param input texte saisi par l'utilisateur
     * @return StationName, Coordinates, invalid
     */
    private InputFormat checkInputFormat(String input) {
        String stationNamePattern = "^[a-zA-ZâêàéèœŒÂÊÉÈÀ\\-\\s]+$"; // Regex matche les noms des stations
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
     * Fonction pour verifier si une station existe
     * @param text nom de la station
     * @return vrai si le reseau contient cette station, faux sinon
     */
    private boolean isValidInput(String text){
        return network.hasStation(text);
    }

    /**
     * Fonction pour afficher un dialogue avec un message d'erreur
     * @param title titre de l'erreur
     * @param error description de l'erreur
     */
    private void displayErrorMessage(String title, String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(error);
        errorAlert.showAndWait();

    }

    /**
     * Fonction qui retourne l'itinéraire à effectuer
     * @param inputFormatA format de la saisie départ
     * @param inputFormatB format de la saisie arrivée
     * @param inputA saisie départ
     * @param inputB saisie arrovée
     * @return itinéraire
     * @throws IllegalTravelException dans le cas où le voyage est impossible à réaliser
     */
    private Itinerary getItinerary(InputFormat inputFormatA, InputFormat inputFormatB, String inputA , String inputB) throws IllegalTravelException {
        Point2D.Double coordA;
        Point2D.Double coordB;
        LocalTime time = LocalTime.of(Integer.parseInt(hourComboBoxA.getValue()),Integer.parseInt(minComboBoxA.getValue()));
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

    /**
     * Fonction qui popule le tableau avec les bonnes informations
     * @param it itinéraire à effectuer
     */
    private void findItinerary(Itinerary it){
        List<Transport> paths = it.getTransports();
        TableColumn<Transport, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            String source = transport.getTransportMethod()==
                    Transport.TransportationMethod.TRANSPORTATION ? ((Path) transport).getSource().getName()
                    : (network.hasStation(((Walk) transport).getDepartureCoordinates()) ? network.getStation(((Walk) transport).getDepartureCoordinates()).getName(): ((Walk) transport).getDepartureCoordinates().getX() + " , "+ ((Walk) transport).getDepartureCoordinates().getY());

            return new ReadOnlyStringWrapper(source);
        });

        TableColumn<Transport, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();

            String destination = transport.getTransportMethod()==
                    Transport.TransportationMethod.TRANSPORTATION ? ((Path) transport).getDestination().getName()
                    : (network.hasStation(((Walk) transport).getArrivalCoordinates()) ? network.getStation(((Walk) transport).getArrivalCoordinates()).getName(): ((Walk) transport).getArrivalCoordinates().getX() + " , "+ ((Walk) transport).getArrivalCoordinates().getY());
            return new ReadOnlyStringWrapper(destination);
        });

        TableColumn<Transport, String> lineColumn = new TableColumn<>("Line");
        lineColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            String lineName = transport.getTransportMethod()== Transport.TransportationMethod.TRANSPORTATION? ((Path) transport).getLineName() : "On foot";
            return new ReadOnlyStringWrapper(lineName);
        });

        TableColumn<Transport, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            LocalTime time = LocalTime.of(Integer.parseInt(hourComboBoxA.getValue()),Integer.parseInt(minComboBoxA.getValue()));

            Duration travelDuration = transport.getTransportDuration(time);
            long hours1 = travelDuration.toHours();
            long minutes1 = travelDuration.toMinutes() % 60;
            long seconds = travelDuration.getSeconds() % 60;
            return new ReadOnlyStringWrapper(String.format("%02d:%02d:%02d", hours1, minutes1, seconds));
        });

        itineraryTable.getColumns().clear();
        itineraryTable.getColumns().addAll(startColumn, endColumn, lineColumn, durationColumn);
        ObservableList<Transport> data = FXCollections.observableList(paths);
        itineraryTable.setItems(data);
        System.out.println(data);
        itineraryTable.setVisible(true);

    }

    /**
     * Écouteur du bouton 'Search' : lance la recherche d'itinéraire
     * @param actionEvent événement détécté
     */
    public void searchPathListener(ActionEvent actionEvent) {
        String inputA = coordinatesAInput.getText();
        String inputB = coordinatesBInput.getText();
        InputFormat inputFormatA = checkInputFormat(inputA);
        InputFormat inputFormatB = checkInputFormat(inputB);
        if (!isValidInput(inputA) && inputFormatA == InputFormat.STATION_NAME) {
            // Show error dialog for invalid station name input
            displayErrorMessage("Invalid input", "The input of source text does not match a valid station name.");
        }else if(isValidInput(inputB) && inputFormatB == InputFormat.STATION_NAME){
            displayErrorMessage("Invalid input", "The input of destination text does not match a valid station name.");

        } else if ((checkInputFormat(inputA) == InputFormat.INVALID) || (checkInputFormat(inputB) == InputFormat.INVALID)) {
            // Show error dialog for invalid coordinates input
            displayErrorMessage("Invalid input", "The input text does not match the coordinates format.");
        } else {
            try {
                Itinerary it = getItinerary(inputFormatA, inputFormatB, inputA, inputB);
                findItinerary(it);
            }
            catch (IllegalTravelException e){
                displayErrorMessage("Invalid itinerary", "Something went wrong");

            }
        }
    }

    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente
     * @param actionEvent événement détécté
     */
    public void goBackListener(ActionEvent actionEvent) {
        navigationController.navigateBack();
    }
}
