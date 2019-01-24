package cz.lastovicka.phonebook.infrastructure.ui.event;

import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;
import lombok.EqualsAndHashCode;

/**
 * An event that is triggered when an existing contact has been deleted.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
@EqualsAndHashCode
public final class ContactDeletedEvent {

    private final ContactDescription contact;

    /**
     *
     * @param contact must not be null
     * @throws IllegalArgumentException if contact is null
     */
    public ContactDeletedEvent(ContactDescription contact) {
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
