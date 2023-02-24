package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/** Un chemin jusqu'à une prochaine station. */
public class Path {
    /** Nom de la ligne sur laquelle se situe le chemin. */
    private String lineName;
    /** Horaires de passage des trains sur ce chemin, dans l'ordre. */
    private ArrayList<LocalTime> schedule;
    /** Durée du trajet jusqu'à la prochaine station. */
    private Duration travelDuration;
    /** Distance du trajet jusqu'à la prochaine station. */
    private double travelDistance;
    /** La station vers laquelle mène le chemin. */
    private Station nextStation;

    /**
     * Calcule le temps pour arriver à la prochaine station à partir d'une heure donnée.
     * <p>
     * Additionne le temps du trajet jusqu'à la prochaine station et le temps d'attente jusqu'au prochain train.
     * @param time l'heure de départ
     * @return la durée du trajet
     */
    public Duration totalDuration(LocalTime time) {
        return Duration.ofSeconds(0);
    }

    /**
     * Renvoie le nom de la ligne sur laquelle se situe le chemin.
     * @return le nom de la ligne
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Renvoie les horaires de passage des trains sur ce chemin dans l'ordre.
     * @return les horaires de passage des trains
     */
    public ArrayList<LocalTime> getSchedule() {
        return schedule;
    }

    /** 
     * Renvoie la durée du trajet jusqu'à la prochaine station.
     * @return la durée du trajet
     */
    public Duration getTravelDuration() {
        return travelDuration;
    }

    /**
     * Renvoie la distance du trajet vers la prochaine station en km.
     * @return la distance du trajet
     */
    public double getTravelDistance() {
        return travelDistance;
    }


    /**
     * Renvoie la station vers laquelle mène le chemin.
     * @return la prochaine station
     */
    public Station getNextStation() {
        return nextStation;
    }

}
