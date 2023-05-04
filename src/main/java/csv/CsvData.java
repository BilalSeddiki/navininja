package csv;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.geom.Point2D.Double;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import model.Network;
import model.Station;
import model.Path;

public abstract class CsvData<T extends CsvData<?>> {

    @SuppressWarnings("unchecked")
    private List<T> readCSV(final Reader reader) throws IOException {
        CsvToBean<T> csvToBean =
            new CsvToBeanBuilder<T>(reader)
            .withType((Class<? extends T>) getClass())
            .withSeparator(';')
            .withIgnoreEmptyLine(true)
            .withIgnoreLeadingWhiteSpace(true).build();
        List<T> data = csvToBean.parse();
        reader.close();
        return data;
    }

    /**
     * Lis un fichier CSV et renvoie une liste contenant les données parsées.
     * @param path Le chemin vers le fichier CSV à parser
     * @return La liste d'instance décrit par le fichier CSV donné en argument
     * @throws IOException if the named file does not exist, is a directory
     * rather than a regular file, or for some other reason cannot be
     * opened for reading.
     */
    public List<T> readCSVFile(final java.nio.file.Path path)
        throws IOException {
        FileReader reader =
            new FileReader(path.toString(), StandardCharsets.UTF_8);
        return readCSV(reader);
    }

    /**
     * Lis une chaîne de caractères contenant des données CSV et renvoie
     * une liste contenant les données parsées.
     * @param text La chaîne de caractères à parser
     * @return La liste d'instance décrit par la chaîne de caractères donnée en
     *         argument
     * @throws IOException if an I/O error occurs
     */
    public List<T> readCSVString(final String text) throws IOException {
        StringReader reader = new StringReader(text);
        return readCSV(reader);
    }

    /**
     * Crée un réseau à partir de deux fichiers CSV.
     * @param mapFile le nom d'un fichier CSV contenant les informations des
     * chemins du réseau
     * @param scheduleFile le nom d'un fichier CSV contenant les informations
     * des horaires des lignes
     * @return un réseau
     * @throws IOException si la lecture d'un des fichier echoue
     */
    public static Network makeNetwork(final String mapFile,
        final String scheduleFile) throws IOException {
        var schedules =
            new HashMap
            <String, HashMap<String, HashMap<String, ArrayList<LocalTime>>>>();
        var csvSchedules =
            new ScheduleDataCsv()
            .readCSVFile(java.nio.file.Path.of(scheduleFile));

        for (var item : csvSchedules) {
            schedules
                .computeIfAbsent(item.getDepartStation(), k ->
                new HashMap<String, HashMap<String, ArrayList<LocalTime>>>())
                .computeIfAbsent(item.getLine(), k ->
                new HashMap<String, ArrayList<LocalTime>>())
                .computeIfAbsent(item.getVariant(), k ->
                new ArrayList<LocalTime>());
            schedules.get(item.getDepartStation()).get(item.getLine())
                .get(item.getVariant()).add(item.getDepartTime());
        }

        var csvPaths = new CardsDataCsv()
            .readCSVFile(java.nio.file.Path.of(mapFile));
        var stations = new HashMap<String, Station>();
        var paths = new ArrayList<Path>();
        for (CardsDataCsv item : csvPaths) {
            String stationNameA = item.getStationA();
            if (!stations.containsKey(stationNameA)) {
                Double coordinatesA = item.getCoordinatesA();
                Station stationA = new Station(stationNameA, coordinatesA);
                stations.put(stationA.getName(), stationA);
            }

            String stationNameB = item.getStationB();
            if (!stations.containsKey(stationNameB)) {
                Double coordinatesB = item.getCoordinatesB();
                Station stationB = new Station(stationNameB, coordinatesB);
                stations.put(stationB.getName(), stationB);
            }

            var schedule = schedules.getOrDefault(stationNameA, new HashMap<>())
                .getOrDefault(item.getLine(), new HashMap<>())
                .getOrDefault(item.getLineVariant(), new ArrayList<>());

            paths.add(
                new Path(item.getLine(), item.getLineVariant(), schedule,
                item.getDuration(), item.getDistance(),
                stations.get(stationNameA), stations.get(stationNameB)));
        }
        return new Network(new ArrayList<>(stations.values()), paths);
    }
}
