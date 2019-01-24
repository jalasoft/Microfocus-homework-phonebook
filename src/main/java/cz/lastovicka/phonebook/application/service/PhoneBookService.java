package cz.lastovicka.phonebook.application.service;

import cz.lastovicka.phonebook.domain.model.contact.*;
import cz.lastovicka.phonebook.infrastructure.repository.contact.ContactRepositoryFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * This class represents a gate to the application login.
 * All user user cases flow through this class.
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

    public PhoneBookService(ContactRepositoryFactory factory) {
        this.factory = factory;
    }

    public void openPhoneBook(ContactsSource resource) {
        if (this.repository != null) {
            throw new IllegalStateException("Another phone book is opened. Close this one first.");
        }

        this.repository = this.factory.repository(resource);
    }

    public ContactsSource source() {
        return repository.source();
    }

    public boolean isOpen() {
        return this.repository != null;
    }

    public Collection<ContactDescription> allContactDescriptions() throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            Collection<ContactDescription> contacts = this.repository.allDescriptions();
            return contacts;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    public Collection<Contact> contactByName(ContactName name) throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            Collection<Contact> contacts = repository.byName(name);
            return contacts;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    public void addContact(ContactName name, PhoneNumber number) throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            Contact contact = repository.newContact(name, number);
            repository.add(contact);
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    public Contact addContact(ContactName name, Collection<PhoneNumber> numbers) throws PhoneBookException {
        checkPhoneBookOpened();

        try {
            Contact contact = repository.newContact(name, numbers);
            repository.add(contact);
            return contact;
        } catch (ContactRepositoryException exc) {
            throw new PhoneBookException(repository.source(), exc);
        }
    }

    public void removeContact(ContactId id) throws PhoneBookException {
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

    public Contact contactById(ContactId id) throws PhoneBookException {

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
