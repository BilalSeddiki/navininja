package controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Itinerary;
import model.Path;
import model.Transport;
import model.Travel;
import model.Walk;
import utils.IllegalTravelException;
import java.awt.geom.Point2D;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * Controlleur de la vue trouver un itinéraire.
 * @author R. MARTINI
 */
public class FindARouteController extends Controller {

    /** label static du texte "total trip duration". */
    @FXML
    Label titleTotalDuration;

    /** label valeur du "total trip duration". */
    @FXML
    Label labelTotalDuration;

    /** Bouton pour intervertir station A et B. */
    @FXML
    Button reverseInputButton;
    /** Bouton retour. */

    @FXML
    Button goBackBtn;
    /** Liste choix de l'heure. */
    @FXML
    ComboBox<String> hourComboBoxA;

    /** Liste choix des minutes. */
    @FXML
    ComboBox<String> minComboBoxA;

    /** Bouton recherche. */
    @FXML
    Button searchBtn;

    /** Menu contextuel de suggestion. */
    @FXML
    ContextMenu suggestionMenu;

    /** Champ de texte d'entrée des coordonnées A. */
    @FXML
    TextField coordinatesAInput;

    /** Champ de texte d'entrée des coordonnées B. */
    @FXML
    TextField coordinatesBInput;

    /** Table des itinéraires. */
    @FXML
    TableView<Transport> itineraryTable;

    /** Colonne de début. */
    @FXML
    TableColumn<Transport, String> startColumn;

    /** Colonne de fin. */
    @FXML
    TableColumn<Transport, String> endColumn;

    /** Colonne des lignes. */
    @FXML
    TableColumn<Transport, String> lineColumn;

    /** Colonne des durées. */
    @FXML
    TableColumn<Transport, Duration> durationColumn;

    /**
     * Enum permettant de détecter le type de saisie de l'utilisateur.
     */
    private enum InputFormat {

        /** La donnée est le nom d'une station. */
        STATION_NAME,

        /** La donnée est une coordonnée. */
        COORDINATES,

        /** La donnée est invalide. */
        INVALID
    }

    /**
     * Liste contenant les heures de la journée.
     */
    private final ObservableList<String> hours =
        FXCollections.observableArrayList(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23"
    );

    /**
     * Nombre de minutes dans une heure.
     */
    private static final int MINUTES_IN_HOUR = 60;

    /**
     * Liste contenant les minutes.
     */
    private final ObservableList<String> minutes =
        FXCollections.observableArrayList();

    /**
     * Message lorsque l'input est erroné.
     */
    private static final String INVALID_INPUT_MESSAGE = "Invalid input";

    /**
     * Fonction pour initialiser les valeurs prédéfinies de l'interface,
     * tel que les valeurs des listes "heures" ou "minutes"
     */
    public void initialize() {
        //Initialisation de la liste des minutes
        for (int i = 0; i < MINUTES_IN_HOUR; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }

        final ObservableList<String> stations =
            FXCollections.observableArrayList(
            network.getStationsByName().keySet());
        hourComboBoxA.getItems().addAll(hours);
        minComboBoxA.getItems().addAll(minutes);

        hourComboBoxA.setValue(String.valueOf(LocalTime.now().getHour()));
        minComboBoxA.setValue(String.valueOf(LocalTime.now().getMinute()));

        coordinatesAInput.textProperty().addListener(
            new TextFieldListener(
                coordinatesAInput, suggestionMenu, stations, network));
        coordinatesBInput.textProperty().addListener(
            new TextFieldListener(
                coordinatesBInput, suggestionMenu, stations, network));
        itineraryTable.getColumns().clear();
        itineraryTable.setVisible(false);
        titleTotalDuration.setVisible(false);
        labelTotalDuration.setVisible(false);
    }

    /**
     * Fonction pour identifier le type de la saisie de l'utilisateur.
     * @param input texte saisi par l'utilisateur
     * @return StationName, Coordinates, invalid
     */
    private InputFormat checkInputFormat(final String input) {
        String stationNamePattern = // Regex matche les noms des stations
            "^[a-zA-ZâêàéèœŒÂÊÉÈÀ'\\-\\s]+$";
        String coordinatesPattern = // regex matche les coordonnées lat,long
            "^-?\\d+(\\.\\d+)?[\\s]*,[\\s]*-?\\d+(\\.\\d+)?$";
        if (input.matches(stationNamePattern)) {
            return InputFormat.STATION_NAME;
        } else if (input.matches(coordinatesPattern)) {
            return InputFormat.COORDINATES;
        } else {
            return InputFormat.INVALID;
        }
    }

    /**
     * Fonction pour verifier si une station existe.
     * @param text nom de la station
     * @return vrai si le reseau contient cette station, faux sinon
     */
    private boolean isValidInput(final String text) {
        return network.hasStation(text);
    }

    /**
     * Fonction pour afficher un dialogue avec un message d'erreur.
     * @param title titre de l'erreur
     * @param error description de l'erreur
     */
    private void displayErrorMessage(final String title, final String error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(error);
        errorAlert.showAndWait();

    }

    /**
     * Fonction qui retourne l'itinéraire à effectuer.
     * @param inputFormatA format de la saisie départ
     * @param inputFormatB format de la saisie arrivée
     * @param inputA saisie départ
     * @param inputB saisie arrovée
     * @return itinéraire
     * @throws IllegalTravelException dans le cas où le voyage est impossible
     * à réaliser
     */
    private Itinerary getItinerary(final InputFormat inputFormatA,
        final InputFormat inputFormatB, final String inputA,
        final String inputB) throws IllegalTravelException {
        Point2D.Double coordA;
        Point2D.Double coordB;
        LocalTime time =
            LocalTime.of(Integer.parseInt(hourComboBoxA.getValue()),
            Integer.parseInt(minComboBoxA.getValue()));
        Travel travel;
        if (inputFormatA == InputFormat.COORDINATES
            && inputFormatB == InputFormat.COORDINATES) {
            //both inputs are coordinates
            String[] latlongA = inputA.split(",");
            coordA =
                new Point2D.Double(Double.parseDouble(latlongA[0]),
                Double.parseDouble(latlongA[1]));
            String[] latlongB = inputB.split(",");
            coordB = new Point2D.Double(
                Double.parseDouble(latlongB[0]),
                Double.parseDouble(latlongB[1]));
            travel = new Travel
                    .Builder(network)
                    .setDijkstra()
                    .setDepartureCoordinates(coordA)
                    .setArrivalCoordinates(coordB)
                    .setDepartureTime(time)
                    .build();
        } else if (inputFormatA == InputFormat.COORDINATES) {
            String[] latlongA = inputA.split(",");
            coordA =
                new Point2D.Double(
                    Double.parseDouble(latlongA[0]),
                    Double.parseDouble(latlongA[1]));
            travel = new Travel
                    .Builder(network)
                    .setDijkstra()
                    .setDepartureCoordinates(coordA)
                    .setArrivalStation(network.getStation(inputB))
                    .setDepartureTime(time)
                    .build();
        } else if (inputFormatB == InputFormat.COORDINATES) {
            String[] latlongB = inputB.split(",");
            coordB =
                new Point2D.Double(
                    Double.parseDouble(latlongB[0]),
                    Double.parseDouble(latlongB[1]));
            travel = new Travel
                    .Builder(network)
                    .setDijkstra()
                    .setDepartureStation(network.getStation(inputA))
                    .setArrivalCoordinates(coordB)
                    .setDepartureTime(time)
                    .build();
        } else {
            //both are stations
            travel = new Travel
                    .Builder(network)
                    .setDijkstra()
                    .setDepartureStation(network.getStation(inputA))
                    .setArrivalStation(network.getStation(inputB))
                    .setDepartureTime(time)
                    .build();
        }
        return travel.createItinerary();
    }

    /**
     * Fonction qui popule le tableau avec les bonnes informations.
     * @param it itinéraire à effectuer
     */
    private void findItinerary(final Itinerary it) {
        List<Transport> paths = it.getTransports();
        TableColumn<Transport, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            String source = null;
            if (transport.getTransportMethod()
                    == Transport.TransportationMethod.TRANSPORTATION) {
                source = ((Path) transport).getSource().getName();
            } else if (network.hasStation(((Walk) transport)
                    .getDepartureCoordinates())) {
                source =
                    network.getStation(((Walk) transport)
                    .getDepartureCoordinates()).getName();
            } else {
                source =
                    (((Walk) transport).getDepartureCoordinates().getX()
                    + " , "
                    + ((Walk) transport).getDepartureCoordinates().getY());
            }
            return new ReadOnlyStringWrapper(source);
        });

        TableColumn<Transport, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();

            String destination = null;
            if (transport.getTransportMethod()
            == Transport.TransportationMethod.TRANSPORTATION) {
                destination = ((Path) transport).getDestination().getName();
            } else if (network.hasStation(
            ((Walk) transport).getArrivalCoordinates())) {
                destination = network.getStation(((Walk) transport)
                    .getArrivalCoordinates()).getName();
            } else {
                destination = (((Walk) transport).getArrivalCoordinates().getX()
                    + " , "
                    + ((Walk) transport).getArrivalCoordinates().getY());
            }
            return new ReadOnlyStringWrapper(destination);
        });

        TableColumn<Transport, String> lineColumn = new TableColumn<>("Line");
        lineColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            String lineName =
                transport.getTransportMethod()
                == Transport.TransportationMethod.TRANSPORTATION
                ? ((Path) transport).getLineName() : "On foot";
            return new ReadOnlyStringWrapper(lineName);
        });

        TableColumn<Transport, String> durationColumn =
            new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> {
            Transport transport = cellData.getValue();
            LocalTime time =
                LocalTime.of(
                    Integer.parseInt(hourComboBoxA.getValue()),
                    Integer.parseInt(minComboBoxA.getValue()));

            Duration travelDuration = transport.getTransportDuration(time);
            long hours1 = travelDuration.toHours();
            long minutes1 = travelDuration.toMinutes() % MINUTES_IN_HOUR;
            long seconds = travelDuration.getSeconds() % MINUTES_IN_HOUR;
            return new ReadOnlyStringWrapper(
                String.format("%02d:%02d:%02d", hours1, minutes1, seconds));
        });
        Duration itDuration = it.getDuration();
        long hours1 = itDuration.toHours();
        long minutes1 = itDuration.toMinutes() % MINUTES_IN_HOUR;
        long seconds = itDuration.getSeconds() % MINUTES_IN_HOUR;
        labelTotalDuration.setText(
            String.format("%02d:%02d:%02d", hours1, minutes1, seconds));
        itineraryTable.getColumns().clear();
        itineraryTable.getColumns().addAll(
            startColumn, endColumn, lineColumn, durationColumn);
        ObservableList<Transport> data = FXCollections.observableList(paths);
        itineraryTable.setItems(data);
        itineraryTable.setVisible(true);
        labelTotalDuration.setVisible(true);
        titleTotalDuration.setVisible(true);

    }

    /**
     * Écouteur du bouton 'Search' : lance la recherche d'itinéraire.
     * @param actionEvent événement détécté
     */
    public void searchPathListener(final ActionEvent actionEvent) {
        String inputA = coordinatesAInput.getText();
        String inputB = coordinatesBInput.getText();
        InputFormat inputFormatA = checkInputFormat(inputA);
        InputFormat inputFormatB = checkInputFormat(inputB);
        if (!isValidInput(inputA) && inputFormatA == InputFormat.STATION_NAME) {
            // Show error dialog for invalid station name input
            displayErrorMessage(
                INVALID_INPUT_MESSAGE,
                "The input of source text does not match a valid station name."
            );
        } else if (!isValidInput(inputB)
        && inputFormatB == InputFormat.STATION_NAME) {
            displayErrorMessage(
                INVALID_INPUT_MESSAGE,
            "The input of destination text does not match a valid station name."
            );

        } else if ((checkInputFormat(inputA) == InputFormat.INVALID)
        || (checkInputFormat(inputB) == InputFormat.INVALID)) {
            // Show error dialog for invalid coordinates input
            displayErrorMessage(
                INVALID_INPUT_MESSAGE,
            "The input text does not match the coordinates format.");
        } else {
            try {
                findItinerary(
                    getItinerary(inputFormatA, inputFormatB, inputA, inputB));
            }
            catch (IllegalTravelException e) {
                displayErrorMessage(
                    "Invalid itinerary",
                    "Something went wrong");
            }
        }
    }

    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente.
     * @param actionEvent événement détécté
     */
    public void goBackListener(final ActionEvent actionEvent) {
        navigationController.navigateBack();
    }

    /**
     * Écouteur du bouton 'échanger' permet d'échang. les deux zones de textes'.
     * @param actionEvent événement détécté
     */
    public void reverseInputListener(final ActionEvent actionEvent) {
        String textInputB = coordinatesBInput.getText();
        coordinatesBInput.setText(coordinatesAInput.getText());
        coordinatesAInput.setText(textInputB);
        suggestionMenu.hide();
    }
}
