package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * A descriptor of a contact source that represents an XML file.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class XmlFileContactsSource implements ContactsSource {

    public enum Mode {
        CREATE_IF_NOT_EXISTS
    }

    public static XmlFileContactsSource newSource(Path file, Mode... modes) {

        Set<Mode> modesSet = EnumSet.noneOf(Mode.class);
        modesSet.addAll(Arrays.asList(modes));

        return new XmlFileContactsSource(file, modesSet);
    }

    //-------------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------------

    private final Path file;
    private final Set<Mode> modes;

    private XmlFileContactsSource(Path file, Set<Mode> modes) {
        this.file = file;
        this.modes = modes;
    }

    @Override
    public String description() {
        return "file: " + file;
    }

    public Path file() {
        return file;
    }

    public boolean createIfNotExists() {
        return modes.contains(Mode.CREATE_IF_NOT_EXISTS);
    }
}
