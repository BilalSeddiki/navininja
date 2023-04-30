package model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Un mode de transport d'un point à un autre. */
public interface Transport {

    /**
     * Renvoie la durée du trajet d'un point à un autre en fonction du mode de transport.
     * L'heure de départ n'est pas prise en compte dans le calcul du trajet à pied.
     * @param departureTime l'heure de départ
     * @return la durée du trajet
     */
    public Duration getTransportDuration(LocalTime departureTime);
}