package cz.lastovicka.phonebook.infrastructure.ui.event;

import com.google.common.base.Objects;
import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;

/**
 * An event that is triggered when an existing contact has been deleted.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDeletedEvent that = (ContactDeletedEvent) o;
        return Objects.equal(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contact);
    }
}
