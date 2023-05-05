public final class MainWrapper {

    private MainWrapper() {
    }

    /**
     * wrapper autour du vraie main.
     * @param args les arguments a envoyé a main
     */
    public static void main(final String[] args) {
        Main.main(args);
    }
}
