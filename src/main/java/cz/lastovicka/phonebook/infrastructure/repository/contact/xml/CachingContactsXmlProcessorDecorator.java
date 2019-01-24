package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.ContactRepository;
import cz.lastovicka.phonebook.domain.model.contact.ContactRepositoryException;
import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is a decorator that caches loaded contacts to
 * improve performance in case there ale lot of contacts read from
 * XML file.
 *
 * It uses last modified flag of the source file distinguishes whether
 * to reload file or not.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
final class CachingContactsXmlProcessorDecorator extends ContactsXmlProcessor {

    private final ContactsXmlProcessor decorated;

    private long lastModified;
    private Collection<Contact> cached;

    public CachingContactsXmlProcessorDecorator(Path target, ContactsXmlProcessor decorated) {
        super(target);
        this.decorated = decorated;
    }

    @Override
    Collection<Contact> read() throws ContactRepositoryException {
        reloadIfNecessary();
        return cached;
    }


    @Override
    void write(Collection<Contact> contacts) throws ContactRepositoryException {
        this.cached = contacts;
        this.decorated.write(contacts);
        this.lastModified = currentLastModified();
    }

    private void reloadIfNecessary() throws ContactRepositoryException {
        long currentLastModified = currentLastModified();

        if (currentLastModified > lastModified) {
            lastModified = currentLastModified;
            this.cached = new ArrayList<>(decorated.read());
        }
    }

    private long currentLastModified() throws ContactRepositoryException {
        try {
            return Files.getLastModifiedTime(target()).toMillis();
        } catch (IOException exc) {
            throw new ContactRepositoryException("An error occurred during reading attributes of " + target() + ".", exc);
        }
    }
}
