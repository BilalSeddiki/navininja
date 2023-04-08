package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Path;
import model.Schedule;
import model.Station;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controlleur de la vue trouver un itinéraire
 * @author R. MARTINI
 */
public class ScheduleController extends Controller {

    @FXML
    ComboBox<String> lineComboBox;
    @FXML
    TextField stationInput;
    @FXML
    ContextMenu suggestionMenuStation;
    @FXML
    Button searchBtn;
    @FXML
    Button goBackBtn;
    @FXML
    TableView<Schedule> busScheduleTable;

    //todo on doit avoir TableColumn<Schedule, String> directionColumn; à la fin
    @FXML
     TableColumn<Schedule, String> directionColumn;

    //todo on doit avoir TableColumn<Schedule, String> timeColumn; à la fin
    @FXML
    TableColumn<Schedule, String> timeColumn;

    public void initialize(){
        final ObservableList<String> stations = FXCollections.observableArrayList(network.getStationsByName().keySet());
        stationInput.textProperty().addListener(new TextFieldListener(stationInput, suggestionMenuStation, stations, network, lineComboBox));
        final ObservableList<String> lines = FXCollections.observableArrayList(network.getLines().keySet());
        lineComboBox.getItems().addAll(lines);
        busScheduleTable.setVisible(false);
        //TODO mettre ici la bonne liste d'horaires

    }
    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente
     * @param actionEvent événement détécté
     */
    public void goBackListener(ActionEvent actionEvent) {
        navigationController.navigateBack();

    }
    /**
     * Écouteur du bouton 'Search' permet d'effectuer la recherche des prochains passage d'une ligne à une station
     * @param actionEvent événement détécté
     */
    public void FindSchedule(ActionEvent actionEvent) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        ObservableList<Schedule> scheduleList ;
        String stationName = stationInput.getText().trim();
        if(!stationName.equals("") && !lineComboBox.getValue().equals("") ){
             if(network.hasStation(stationName)){
                 for (Path p: network.getStation(stationName).getInPaths()){
                     System.out.println();
                     //todo afficher les horaire ici
                     //schedules.add(new Schedule(p.getDestination().getName(), p.getSchedule().get(0).toString()));
                 }
                 scheduleList = FXCollections.observableArrayList(schedules);
                 directionColumn.setCellValueFactory(new PropertyValueFactory<>("directionAsSimpleString"));
                 timeColumn.setCellValueFactory(new PropertyValueFactory<>("passingTimeAsSimpleString"));
                 busScheduleTable.setItems(scheduleList);
                 busScheduleTable.setVisible(true);
             }
        }


    }
}
