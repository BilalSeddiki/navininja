package csv;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public abstract class CsvData<T extends CsvData<?>> {
    
    /**
     * 
     * @param path Le chemin vers le fichier CSV à parser
     * @return La liste d'instance de {@code T} décrit par le fichier CSV donné en argument
     * @throws IOException if the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @SuppressWarnings("unchecked")
    public List<T> readCSV(Reader reader) throws IOException {
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withType((Class<? extends T>) getClass()).withSeparator(';').withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();
        reader.close();
        return csvToBean.parse();
    }

    public List<T> readCSVFile(Path path) throws IOException {
        FileReader reader = new FileReader(path.toString(), StandardCharsets.UTF_8);
        return readCSV(reader);
    }


    public List<T> readCSVString(String text) throws IOException {
        StringReader reader = new StringReader(text);
        return readCSV(reader);
    }
}
