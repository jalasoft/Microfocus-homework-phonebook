package cz.lastovicka.phonebook;

import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.ContactId;
import cz.lastovicka.phonebook.domain.model.contact.ContactName;
import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;

/**
 * Contacts that are included in test data file
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 **/
public final class Contacts {

    public static Contact PAUL_CONTACT = new Contact(
            ContactId.from("00000000-0000-0000-0000-000000000002"),
            ContactName.fromFullName("Paul McCartney"),
            PhoneNumber.parse("+888 333 333333"),
            PhoneNumber.parse("+444 444 444444")
    );

    public static Contact GEORGE_CONTACT = new Contact(
            ContactId.from("00000000-0000-0000-0000-000000000003"),
            ContactName.fromFullName("George Harrison"),
            PhoneNumber.parse("+001 555 555555")
    );

    public static Contact JOHN_CONTACT = new Contact(
            ContactId.from("00000000-0000-0000-0000-000000000001"),
            ContactName.fromFullName("John Lennon"),
            PhoneNumber.parse("+222 111 111111"),
            PhoneNumber.parse("+989 222 222222"),
            PhoneNumber.parse("+333 313 131313")
    );

    public static Contact NOT_EXISTING_CONTACT = new Contact(
            ContactId.from("00000000-0000-0000-0000-000000000011"),
            ContactName.fromFullName("John Doe"),
            PhoneNumber.parse("+565 222 444555")
    );

    public static Contact SVEJK_CONTACT = new Contact(
            ContactId.from("00000000-0000-0000-0000-000000000006"),
            ContactName.fromFullName("Josef Svejk"),
            PhoneNumber.parse("+420 567 567567")
    );
}
