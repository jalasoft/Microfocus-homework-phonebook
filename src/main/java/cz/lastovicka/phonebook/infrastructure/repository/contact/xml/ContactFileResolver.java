package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class ContactFileResolver {

    private static final Path DEFAULT_CONTACT_FILE = Paths.get(System.getProperty("user.home"), "contacts.xml");

    static Path resolveContactFile() {
        String contactFilePath = System.getProperty("contactFilePath");

        if (contactFilePath == null) {
            return DEFAULT_CONTACT_FILE;
        }

        Path path = Paths.get(contactFilePath);

        if (Files.exists(path) && !Files.isRegularFile(path)) {
            return DEFAULT_CONTACT_FILE;
        }

        return path;
    }
}
