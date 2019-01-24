package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import cz.lastovicka.phonebook.domain.model.contact.Contact;

import java.util.ArrayList;
import java.util.Collection;

final class ContactsXmlRootWrapper {

    private ArrayList<Contact> contacts;

    ContactsXmlRootWrapper(Collection<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    ContactsXmlRootWrapper() {}

    Collection<Contact> contacts() {
        return contacts;
    }
}
