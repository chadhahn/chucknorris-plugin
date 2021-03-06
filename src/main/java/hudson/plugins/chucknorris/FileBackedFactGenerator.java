package hudson.plugins.chucknorris;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.apache.commons.io.FileUtils.readLines;

public class FileBackedFactGenerator extends FactGenerator {

    private static final Logger LOGGER = Logger.getLogger(FileBackedFactGenerator.class.getName());
    private static final String FACTS_FILE = "/facts.txt";

    private static List<String> facts = null;

    @Override
    public String random() {
        if (facts == null) {
            try {
                facts = getFactsFromFile();
            } catch (final IOException e) {
                LOGGER.warning("'${HUDSON_HOME}/plugins/chucknorris/WEB-INF/classes/facts.txt' not found " + e);
                facts = Collections.emptyList();
            }
        }
        return getRandomFact();
    }

    @SuppressWarnings("unchecked")
    protected List<String> getFactsFromFile() throws IOException {
        final File file = getFileFromUrl();
        return readLines(file);
    }

    private File getFileFromUrl() throws IOException {
        final URL url = getClass().getResource(FACTS_FILE);
        if (url == null) {
            throw new IOException();
        }
        try {
            return new File(url.toURI());
        } catch (final URISyntaxException e) {
            throw new IOException(e);
        }
    }

    protected String getRandomFact() {
        if (facts.isEmpty()) {
            return super.random();
        } else {
            return facts.get(RANDOM.nextInt(facts.size()));
        }
    }

}