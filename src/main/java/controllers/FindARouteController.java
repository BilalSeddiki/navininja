package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Itinerary;
import model.Transport;
import shortestpath.Dijkstra;

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
     * Écouteur du bouton 'Search' : lance la recherche d'itinéraire
     * @param actionEvent événement détécté
     */
    public void searchPathListener(ActionEvent actionEvent) {
        //todo implement search action
        String stationAName = coordinatesAInput.getText();
        String stationBName = coordinatesBInput.getText();
        LocalTime time = LocalTime.of(Integer.parseInt(hourComboBoxA.getValue()),Integer.parseInt(minComboBoxA.getValue()));

        if(network.hasStation(stationAName) && network.hasStation(stationBName)){
            Itinerary it = new Dijkstra(network).bestPath(network.getStation(stationAName), network.getStation(stationBName), time );
            List<Transport> paths = it.getTransports();
            //List<Transport> transports = it.getTransports();
            long hours = it.getDuration().toHours();
            long minutes = it.getDuration().toMinutes() % 60;
            long seconds = it.getDuration().getSeconds() % 60;
            labelTotalDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
            lineColumn.setCellValueFactory(new PropertyValueFactory<>("lineName"));
            // Define a cell factory for the duration column
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("travelDuration"));
            durationColumn.setCellFactory(column -> {
                TableCell<Transport, Duration> cell = new TableCell<Transport, Duration>() {
                    @Override
                    protected void updateItem(Duration duration, boolean empty) {
                        super.updateItem(duration, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            long hours = duration.toHours();
                            long minutes = duration.toMinutes() % 60;
                            long seconds = duration.getSeconds() % 60;
                            setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                        }
                    }
                };
                return cell;
            });



            itineraryTable.getColumns().clear();
            itineraryTable.getColumns().addAll(startColumn, endColumn, lineColumn, durationColumn);

            ObservableList<Transport> data = FXCollections.observableList(paths);
            itineraryTable.setItems(data);
            itineraryTable.setVisible(true);
            titleTotalDuration.setVisible(true);

            labelTotalDuration.setVisible(true);

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


