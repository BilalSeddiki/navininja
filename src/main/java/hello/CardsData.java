package hello;

public class CardsData {

    @CsvBindByName(column = "stationA")
    private final String stationA;
    private final String stationB;
    private final Coordinates coordinatesA;
    private final Coordinates coordinatesB;
    private final double distance;
    private final int duration;

    public CardsData(String stationA, String stationB, Coordinates coordinatesA, Coordinates coordinatesB, double distance, int duration) {
        this.stationA = stationA;
        this.stationB = stationB;
        this.coordinatesA = coordinatesA;
        this.coordinatesB = coordinatesB;
        this.distance = distance;
        this.duration = duration;
    }

    public class Coordinates {
        private final double x;
        private final double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
    
}
