package cz.lastovicka.phonebook.infrastructure.ui.event;

import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;

/**
 * An event triggered when a phone book was loaded and is
 * ready to be used.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
public final class PhoneBookReadyEvent {

    /**
     * Origin of the phone book - new one or existing one
     */
    public enum Origin {
        NEW, EXISTING
    }

    /**
     * Gets a new event representing new phone book
     * @param source must not be null
     * @return never null
     * @throws IllegalArgumentException if source is null
     */
    public static PhoneBookReadyEvent newPhoneBook(ContactsSource source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null.");
        }
        return new PhoneBookReadyEvent(Origin.NEW, source);
    }

    /**
     * Gets a new event represenitng an exisitng phone book
     * @param source must not be null
     * @return never null
     * @throws IllegalArgumentException if source is null
     */
    public static PhoneBookReadyEvent existingPhoneBook(ContactsSource source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null.");
        }
        return new PhoneBookReadyEvent(Origin.EXISTING, source);
    }

    //--------------------------------------------------------------
    //INSTANCE SCOPE
    //--------------------------------------------------------------

    private final Origin origin;
    private final ContactsSource source;

    public PhoneBookReadyEvent(Origin origin, ContactsSource source) {
        this.origin = origin;
        this.source = source;
    }

    public Origin origin() {
        return origin;
    }

    public ContactsSource source() {
        return source;
    }
}
