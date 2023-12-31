package utils;

/** Exception utilisée pour le monteur des déplacements. */
public class IllegalTravelException extends Exception {

    /**
     * Construit une exception.
     * @param error Message d'erreur.
     */
    public IllegalTravelException(final String error) {
        super(error);
    }
}
