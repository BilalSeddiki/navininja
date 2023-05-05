package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Path;
import model.Schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlleur de la vue trouver un itinéraire.
 * @author R. MARTINI
 */
public class ScheduleController extends Controller {

    /** Liste déroulante. */
    @FXML
    ComboBox<String> lineComboBox;

    /** Champ de texte d'entrée de station. */
    @FXML
    TextField stationInput;

    /** Menu contextuel des suggestions de stations. */
    @FXML
    ContextMenu suggestionMenuStation;

    /** Bouton de recherche. */
    @FXML
    Button searchBtn;

    /** Bouton retour. */
    @FXML
    Button goBackBtn;

    /** Tableau des horaires. */
    @FXML
    TableView<Schedule> busScheduleTable;

    /**Colonne de la direction du variant. */
    @FXML
    TableColumn<Schedule, String> directionColumn;

    /**Colonne de l'horaire de passage. */
    @FXML
    TableColumn<Schedule, String> timeColumn;

    /**
     * Initialise le controlleur.
     */
    public void initialize() {
        final ObservableList<String> stations =
            FXCollections.observableArrayList(
                network.getStationsByName().keySet());
        stationInput.textProperty().addListener(
            new TextFieldListener(
                stationInput, suggestionMenuStation,
                stations, network, lineComboBox));
        final ObservableList<String> lines =
            FXCollections.observableArrayList(network.getLines().keySet());
        lineComboBox.getItems().addAll(lines);
        busScheduleTable.setVisible(false);
    }

    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente.
     * @param actionEvent événement détécté
     */
    public void goBackListener(final ActionEvent actionEvent) {
        navigationController.navigateBack();
    }

    /**
     * Écouteur du bouton Search permet d'effectuer la recherche des prochains.
     * passage d'une ligne à une station
     * @param actionEvent événement détécté
     */
    public void findSchedule(final ActionEvent actionEvent) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<Schedule> schedules = new ArrayList<>();
        ObservableList<Schedule> scheduleList;
        String stationName = stationInput.getText().trim();
        if (!stationName.equals("") && !lineComboBox.getValue().equals("")
            && (network.hasStation(stationName))) {
                List<Path> paths =
                    network.getStation(stationName)
                    .getOutPathsFromLine(lineComboBox.getValue());
                for (Path p : paths) {
                    for (LocalTime lt : p.getSchedule()) {
                        if (lt.isAfter(LocalTime.now())) {
                            schedules.add(new Schedule(
                            network.getEndTerminus(lineComboBox.getValue(),
                            p.getVariant()).getName(), lt.format(dtf)));
                        }
                    }
                }
                scheduleList = FXCollections.observableArrayList(schedules);
                scheduleList.sort((o1, o2) ->
                    o1.getPassingTimeAsSimpleString()
                    .compareTo(o2.getPassingTimeAsSimpleString()));
                directionColumn.setCellValueFactory(
                    new PropertyValueFactory<>("directionAsSimpleString"));
                timeColumn.setCellValueFactory(
                    new PropertyValueFactory<>("passingTimeAsSimpleString"));
                busScheduleTable.setItems(scheduleList);
                busScheduleTable.setVisible(true);
        }
    }
}
