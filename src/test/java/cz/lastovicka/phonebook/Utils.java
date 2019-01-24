package cz.lastovicka.phonebook;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class Utils {

    public static Path prepareExampleXml() throws IOException, URISyntaxException {
        Path tempFile = Files.createTempFile("test_contacts", ".xml");

        URL url = Utils.class.getClassLoader().getResource("contacts.xml");
        Path sourceFile = new File(url.toURI()).toPath();

        Files.deleteIfExists(tempFile);
        Files.copy(sourceFile, tempFile);

        return tempFile;
    }

    private Utils() {}
}
