package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/* Note : Pour implémenter les chemins à pied, on pourra créer une interface implémentée par Path et une nouvelle classe pour la marche à pied */

/** Un chemin jusqu'à une prochaine station. */
public class Path {
    /** Nom de la ligne sur laquelle se situe le chemin. */
    private String lineName;
    /** Numéro de variant de la ligne sur laquelle se situe le chemin */
    private int variant;
    /** Horaires de passage des trains sur ce chemin, dans l'ordre. */
    private ArrayList<LocalTime> schedule;
    /** Durée du trajet jusqu'à la prochaine station. */
    private Duration travelDuration;
    /** Distance du trajet jusqu'à la prochaine station en km. */
    private double travelDistance;
    /** Station depuis laquelle part le chemin. */
    private Station source;
    /** Station vers laquelle mène le chemin. */
    private Station destination;

    /**
     * Construit un chemin jusqu'à une prochaine station
     * @param lineName le nom de la ligne sur laquelle se situe le chemin
     * @param variant le variant de la ligne sur laquelle se situe le chemin
     * @param schedule les horaires de passage des trains sur ce chemin, dans l'ordre
     * @param travelDuration la durée du trajet jusqu'à la prochaine station
     * @param travelDistance la distance du trajet jusqu'à la prochaine station en km
     * @param source la depuis laquelle part le chemin
     * @param destination la station vers laquelle mène le chemin
     */
    public Path(String lineName, int variant, ArrayList<LocalTime> schedule, Duration travelDuration,
            double travelDistance,
            Station source, Station destination) {
        this.lineName = lineName;
        this.variant = variant;
        this.schedule = schedule;
        this.travelDuration = travelDuration;
        this.travelDistance = travelDistance;
        this.source = source;
        this.destination = destination;
    }

    /**
     * Renvoie l'heure du prochain départ de train dans cette direction à partir de l'heure indiquée.
     * @param from l'heure depuis laquelle calculer le prochain départ
     * @return l'heure du prochain départ
     */
    public LocalTime nextTrainDeparture(LocalTime from) {
        for(int i = 0; i < schedule.size(); i++) {
            if(schedule.get(i).isAfter(from)) {
                return schedule.get(i);
            }
        }
        if(schedule.size() > 0) {
            return schedule.get(0);
        }
        return LocalTime.of(0, 0);
    }

    /**
     * Calcule le temps pour arriver à la prochaine station à partir d'une heure donnée.
     * <p>
     * Additionne le temps du trajet jusqu'à la prochaine station et le temps d'attente jusqu'au prochain train.
     * @param time l'heure de départ
     * @return la durée du trajet
     */
    public Duration totalDuration(LocalTime time) {
        LocalTime nextTrain = this.nextTrainDeparture(time);
        Duration waitingTime = Duration.between(time, nextTrain);
        if(waitingTime.isNegative()) {
            Duration toMidnight = Duration.between(time, LocalTime.MAX);
            Duration fromMidnight = Duration.between(LocalTime.MIDNIGHT, nextTrain);
            waitingTime = toMidnight.plus(fromMidnight).plusNanos(1);
        }
        Duration totalDuration = waitingTime.plus(this.travelDuration);
        return totalDuration;
    }

    /**
     * Renvoie le nom de la ligne sur laquelle se situe le chemin.
     * @return le nom de la ligne
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Renvoie le numéro de variant de la ligne sur laquelle se situe le chemin.
     * @return le numéro de variant
     */
    public int getVariant() {
        return variant;
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
     * Renvoie la station depuis laquelle part le chemin.
     * @return la station de départ
     */
    public Station getSource() {
        return source;
    }

    /**
     * Renvoie la station vers laquelle mène le chemin.
     * @return la prochaine station
     */
    public Station getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof Path p &&
                this.variant == p.variant &&
                this.travelDistance == p.travelDistance &&
                this.lineName.equals(p.lineName) &&
                this.schedule.equals(p.schedule) &&
                this.travelDuration.equals(p.travelDuration) &&
                this.source.equalsNonRecursive(p.source) &&
                this.destination.equalsNonRecursive(p.destination);
    }

}
