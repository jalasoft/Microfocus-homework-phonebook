package cz.lastovicka.phonebook.infrastructure.repository.contact;

import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlFileContactsSource;
import cz.lastovicka.phonebook.domain.model.contact.ContactRepository;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlContactRepository;

/**
 * This class servers a factory of {@link ContactRepository} based on
 * concrete type of {@link ContactsSource}
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactRepositoryFactory {

    /**
     * Gets a new contact repository
     *
     * @param resource must not be null
     * @return never null
     * @throws IllegalArgumentException if resource is null or is not supported.
     */
    public ContactRepository repository(ContactsSource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Phone book resource must not be null.");
        }

        if (resource instanceof XmlFileContactsSource) {
            XmlFileContactsSource xmlResource = (XmlFileContactsSource) resource;
            return new XmlContactRepository(xmlResource);
        }

        throw new IllegalStateException("Unsupported type of phone book resource: " + resource.getClass());
    }
}
