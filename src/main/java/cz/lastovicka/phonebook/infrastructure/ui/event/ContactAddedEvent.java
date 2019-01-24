package cz.lastovicka.phonebook.infrastructure.ui.event;

import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;
import lombok.EqualsAndHashCode;

/**
 * An event that is triggered when a new contact is added to the phone book.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
@EqualsAndHashCode
public final class ContactAddedEvent {

    private ContactDescription contact;

    /**
     *
     * @param contact must not be null
     * @throws IllegalArgumentException if contact is null
     */
    public ContactAddedEvent(ContactDescription contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null.");
        }
        this.contact = contact;
    }

    /**
     *
     * @return never null
     */
    public ContactDescription contact() {
        return contact;
    }
}
