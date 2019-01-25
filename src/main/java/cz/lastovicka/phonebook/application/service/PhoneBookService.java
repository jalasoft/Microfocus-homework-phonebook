package cz.lastovicka.phonebook.application.service;

import cz.lastovicka.phonebook.domain.model.contact.*;
import cz.lastovicka.phonebook.infrastructure.repository.contact.ContactRepositoryFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * This class represents a gate to the application logic.
 * All user use cases flow through this class.
 *
 * This application service coordinates backend, like working
 * with repository etc.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class PhoneBookService {

    private final ContactRepositoryFactory factory;

    private ContactRepository repository;

    /**
     *
     * @param factory must not be null
     * @throws IllegalArgumentException if factory is null
     */
    public PhoneBookService(ContactRepositoryFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        this.factory = factory;
    }

    /**
     * Opens a phone book (or creates a new one if defined within {@link ContactsSource}
     *
     * @param resource a source of phone book, must not be null
     * @throws IllegalArgumentException if source is null
     * @throws IllegalStateException if there is another repository opened.
     */
    public void openPhoneBook(ContactsSource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Contacts source must not be null.");
        }
        if (this.repository != null) {
            throw new IllegalStateException("Another phone book is opened. Close this one first.");
        }

        this.repository = this.factory.repository(resource);
    }

    /**
     * Gets a description of a contacts source.
     * @return never null
     * @throws IllegalStateException if no phone book is opened
     */
    public ContactsSource source() {
        checkPhoneBookOpened();

        return repository.source();
    }

    /**
     * Checks whether a phone book is opened.
     * @return
     */
    public boolean isOpen() {
        return this.repository != null;
    }

    /**
     * Gets all contacts descriptions (lightweight contacts)
     * @return never null, might be empty
     * @throws PhoneBookException if an error occurred during accessing persistence
     * @throws IllegalStateException if there is no phone book opened
     */
    public Collection<ContactDescription> allContactDescriptions() throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            Collection<ContactDescription> contacts = this.repository.allDescriptions();
            return contacts;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    /**
     * Gets all contacts of given name
     * @param name must not be null
     * @return never null, might be empty
     * @throws PhoneBookException if an error occurred during accessing persistence store
     * @throws IllegalArgumentException if name is null
     * @throws IllegalStateException if there is no phone book opened.
     */
    public Collection<Contact> contactByName(ContactName name) throws PhoneBookException {
        if (name == null) {
            throw new IllegalArgumentException("Contact name must not be null.");
        }
        checkPhoneBookOpened();

        try {
            Collection<Contact> contacts = repository.byName(name);
            return contacts;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    /**
     * Adds a new contact
     *
     * @param name must not be null
     * @param numbers must not be null
     * @return never null, new contact
     * @throws PhoneBookException if an error occurred during accessing the persistence store
     * @throws IllegalArgumentException if name is null or numbers is null or empty
     * @throws IllegalStateException if there is no phone opened
     */
    public Contact addContact(ContactName name, Collection<PhoneNumber> numbers) throws PhoneBookException {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null.");
        }

        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("Numbers must not be null or empty.");
        }
        checkPhoneBookOpened();

        try {
            Contact contact = repository.newContact(name, numbers);
            repository.add(contact);
            return contact;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    /**
     * Removes an existing contact
     * @param id
     * @throws PhoneBookException if an error occurred during accessing the persistence store or there is no contact of given id
     * @throws IllegalStateException if no phone book is opened
     * @throws IllegalArgumentException if id is null
     */
    public void removeContact(ContactId id) throws PhoneBookException {
        if (id == null) {
            throw new IllegalArgumentException("Contact id must not be null.");
        }
        checkPhoneBookOpened();

        try {
            boolean result = repository.remove(id);

            if (!result) {
                throw new PhoneBookException(repository.source(), "Could not remove contact " + id.asString() + ", it does not exist.");
            }
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    /**
     * Gets a contact by its id.
     * @param id must not be null
     * @return never null
     * @throws PhoneBookException if an error occurred during accessing persistence store
     * @throws IllegalArgumentException if id is null
     * @throws IllegalStateException if there is no phone book opened.
     */
    public Contact contactById(ContactId id) throws PhoneBookException {
        if (id == null) {
            throw new IllegalArgumentException("Contact id must not be null.");
        }

        checkPhoneBookOpened();

        try {
            Optional<Contact> maybeContact = repository.byId(id);
            if (!maybeContact.isPresent()) {
                throw new PhoneBookException(repository.source(), "No contact exists for id " + id.asString());
            }

            return maybeContact.get();
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    /**
     * Closes currently opened phone book
     * @throws PhoneBookException if an error occurred during closing persistence store
     * @throws IllegalStateException if there is no phone book opened.
     */
    public void closePhoneBook() throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            repository.close();
            repository = null;
        } catch (IOException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    private void checkPhoneBookOpened() {
        if (this.repository == null) {
            throw new IllegalStateException("No phone book is opened.");
        }
    }
}
