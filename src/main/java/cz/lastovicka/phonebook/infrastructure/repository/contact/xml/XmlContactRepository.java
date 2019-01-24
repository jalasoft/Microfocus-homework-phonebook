package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import cz.lastovicka.phonebook.domain.model.contact.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of {@link ContactRepository} that
 * takes advantage of XML files as a repository.
 *
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class XmlContactRepository implements ContactRepository {

    private final ContactsXmlProcessor processor;
    private final XmlFileContactsSource source;

    public XmlContactRepository(XmlFileContactsSource source) {
        this.source = source;
        this.processor = new CachingContactsXmlProcessorDecorator(source.file(), new XStreamContactsXmlProcessor(source.file()));

        try {
            initializeFile(source);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void initializeFile(XmlFileContactsSource source) throws IOException {
        if (Files.exists(source.file())) {
            if (!Files.isRegularFile(source.file())) {
                throw new IllegalArgumentException("XML file expected: " + source.file());
            }
            return;
        }

        if (source.createIfNotExists()) {
            Files.createFile(source.file());
            return;
        }

        throw new IllegalStateException("No file " + source.file() + " exists and option CREATE_IF_NOT_EXISTS is turned off.");
    }

    @Override
    public ContactsSource source() {
        return source;
    }

    public void add(Contact contact) throws ContactRepositoryException {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null.");
        }

        Collection<Contact> contacts = processor.read();
        contacts.add(contact);
        processor.write(contacts);
    }

    public boolean remove(ContactId id) throws ContactRepositoryException {
        if (id == null) {
            throw new IllegalArgumentException("Contact id must not be null.");
        }
        Collection<Contact> contacts = processor.read();

        Optional<Contact> maybeContact = contacts.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();

        if (!maybeContact.isPresent()) {
            return false;
        }

        Contact contact = maybeContact.get();

        contacts.remove(contact);
        processor.write(contacts);
        return true;
    }

    public Collection<Contact> all() throws ContactRepositoryException {

        Collection<Contact> contacts = processor.read()
                .stream()
                .collect(Collectors.toList());

        return contacts;
    }

    @Override
    public Collection<ContactDescription> allDescriptions() throws ContactRepositoryException {

        return processor.read()
                .stream()
                .map(Contact::description)
                .collect(Collectors.toList());
    }

    public Collection<Contact> byName(ContactName name) throws ContactRepositoryException {
        if (name == null) {
            throw new IllegalArgumentException("Contact name must not be null.");
        }

        return processor.read()
                .stream()
                .filter(c -> c.name().equals(name))
                .collect(Collectors.toList());
    }

    public Optional<Contact> byId(ContactId id) throws ContactRepositoryException {
        if (id == null) {
            throw new IllegalArgumentException("Contact id must not be null.");
        }

        List<Contact> filtered = processor.read()
                .stream()
                .filter(c -> c.id().equals(id))
                .collect(Collectors.toList());

        if (filtered.size() > 1) {
            throw new ContactRepositoryException("More than ona contacts has the same id: " + id.asUuid());
        }

        if (filtered.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(filtered.get(0));
    }

    public Contact newContact(ContactName name, PhoneNumber... numbers) {
        if (name == null) {
            throw new IllegalArgumentException("Contact name must not be null.");
        }

        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Phone numbers must not be null or empty.");
        }
        return newContact(name, Arrays.asList(numbers));
    }

    public Contact newContact(ContactName name, Collection<PhoneNumber> numbers) {
        if (name == null) {
            throw new IllegalArgumentException("Contact name must not be null.");
        }

        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("Phone numbers must not be null or empty.");
        }

        ContactId id = ContactId.from(generateId());

        return new Contact(id, name, numbers);
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }

    @Override
    public void close() throws IOException {
        //nothing needed
    }
}
