package cz.lastovicka.phonebook.application.service;

import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;

/**
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class PhoneBookException extends Exception {

    private final ContactsSource source;

    PhoneBookException(ContactsSource source, String cause) {
        super(cause);

        this.source = source;
    }

    PhoneBookException(ContactsSource source, Exception exc) {
        super(exc);

        this.source = source;
    }

    public ContactsSource source() {
        return source;
    }
}
