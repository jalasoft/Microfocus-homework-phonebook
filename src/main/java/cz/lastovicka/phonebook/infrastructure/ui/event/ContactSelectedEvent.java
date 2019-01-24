package cz.lastovicka.phonebook.infrastructure.ui.event;

import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;

/**
 * An event that is triggered when the user selects a contact
 * in UI in order to see its details.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
public final class ContactSelectedEvent {

    private final ContactDescription contact;

    /**
     *
     * @param contact must not be null
     * @throws IllegalArgumentException if contact is null
     */
    public ContactSelectedEvent(ContactDescription contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null");
        }
        this.contact = contact;
    }

    public ContactDescription contact() {
        return contact;
    }
}
