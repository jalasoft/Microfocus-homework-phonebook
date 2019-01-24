package cz.lastovicka.phonebook.domain.model.contact;

import java.io.Closeable;
import java.util.Collection;
import java.util.Optional;

/**
 * An access point to persistence storage of contacts.
 * This interface represents a repository for the aggregate
 * entity Contact.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public interface ContactRepository extends Closeable {

    /**
     * Gets a description of the source repository.
     * @return never null
     */
    ContactsSource source();

    /**
     * Adds a new contact.
     *
     * @param contact must not be null
     * @throws ContactRepositoryException if an error occurred during saving the entity
     * @throws IllegalArgumentException if contact is null
     */
    void add(Contact contact) throws ContactRepositoryException;

    /**
     * Removed an existing contact that is identified by its id
     * @param id
     * @return true if the contact was removed, false if not because it was not in the repo
     * @throws ContactRepositoryException if an error occurred during working with repository.
     * @throws IllegalArgumentException if id is null
     */
    boolean remove(ContactId id) throws ContactRepositoryException;

    /**
     * Gets all contacts
     * @return never null, might be empty.
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Collection<Contact> all() throws ContactRepositoryException;

    /**
     * Gets descripions of all contacts
     * @return never null, might be empty
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Collection<ContactDescription> allDescriptions() throws ContactRepositoryException;

    /**
     * Gets all contacts by a name
     * @param name must be null
     * @return never null, might be empty.
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Collection<Contact> byName(ContactName name) throws ContactRepositoryException;

    /**
     * Gets a contact by its identified.
     * @param id identifier of a contact, must not be null.
     * @return might be empty.
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Optional<Contact> byId(ContactId id) throws ContactRepositoryException;

    /**
     * Creates a new contact from name and a set of phone numbers
     * @param name a name, must not be null, must not be null
     * @param numbers a collection of phone numbers, must not be null or empty.
     * @return never null
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Contact newContact(ContactName name, PhoneNumber... numbers) throws ContactRepositoryException;

    /**
     * Creates a new Contact entity
     * @param name a name, must not be null
     * @param numbers phone numbers, must not be null or empty.
     * @return neverl null, new entity Contact
     * @throws ContactRepositoryException if an error occurred during accessing the repository.
     */
    Contact newContact(ContactName name, Collection<PhoneNumber> numbers) throws ContactRepositoryException;

}
