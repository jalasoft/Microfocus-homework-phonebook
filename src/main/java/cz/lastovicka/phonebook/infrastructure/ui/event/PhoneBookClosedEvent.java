package cz.lastovicka.phonebook.infrastructure.ui.event;

import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;

/**
 * This event is triggered when a phone book that was opened has
 * been closed.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
public final class PhoneBookClosedEvent {

    private final ContactsSource source;

    /**
     *
     * @param source must not be null
     * @throws IllegalArgumentException if source is null
     */
    public PhoneBookClosedEvent(ContactsSource source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null.");
        }
        this.source = source;
    }

    public ContactsSource source() {
        return source;
    }
}
