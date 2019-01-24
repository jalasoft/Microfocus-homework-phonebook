package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.ContactRepositoryException;

import java.nio.file.Path;
import java.util.Collection;

/**
 * This class is a low level class allowing reading
 * and writing XML contacts. It is an abstract class.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
abstract class ContactsXmlProcessor {

    private final Path target;

    ContactsXmlProcessor(Path target) {
        this.target = target;
    }

    final Path target() {
        return target;
    }

    abstract Collection<Contact> read() throws ContactRepositoryException;

    abstract void write(Collection<Contact> contacts) throws ContactRepositoryException;
}
