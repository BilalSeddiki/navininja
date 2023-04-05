package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Schedule;

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

    private final ArrayList<String> stations = new ArrayList<>();
    public void initialize(){

        //TODO modifier liste des lignes
         final ObservableList<String> lines = FXCollections.observableArrayList(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14"
        );
        //TODO modifier liste stations ici
        stations.add("BNF");
        stations.add("place d'italie");
        stations.add("nation");
        stations.add("Glacière");
        stations.add("Gare de lyon");
        stationInput.textProperty().addListener(new TextFieldListener( stationInput, suggestionMenuStation, stations));
        lineComboBox.getItems().addAll(lines);

        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        //TODO mettre ici la bonne liste d'horaires
        for( String s : stations ){
            schedules.add(new Schedule(s, LocalTime.now().format(dtf) ));
        }

        ObservableList<Schedule> scheduleList = FXCollections.observableArrayList(schedules);

        directionColumn.setCellValueFactory(new PropertyValueFactory<>("directionAsSimpleString"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("passingTimeAsSimpleString"));


        busScheduleTable.setItems(scheduleList);
    }
    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente
     * @param actionEvent événement détécté
     */
    public void goBackListener(ActionEvent actionEvent) {
        navigationController.navigateBack();

    }
}
