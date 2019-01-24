package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import com.thoughtworks.xstream.XStream;
import cz.lastovicka.phonebook.domain.model.contact.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

/**
 * This class servers as a source of data read from XML file
 * with help of {@link XStream} library.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class XStreamContactsXmlProcessor extends ContactsXmlProcessor {

    private final XStream stream;

    XStreamContactsXmlProcessor(Path target) {
        super(target);

        this.stream = new XStream();

        this.stream.addImplicitCollection(Contact.class, "numbers");
        this.stream.addImplicitCollection(ContactsXmlRootWrapper.class, "contacts");
        this.stream.alias("contacts", ContactsXmlRootWrapper.class);
        this.stream.alias("contact", Contact.class);
        this.stream.alias("number", PhoneNumber.class);
        this.stream.registerConverter(new ContactIdConverter());
        this.stream.registerConverter(new ContactNameConverter());
        this.stream.registerConverter(new PhoneNumberConverter());
    }

    Collection<Contact> read() throws ContactRepositoryException {

        try {
            long size = Files.size(target());

            if (size == 0) {
                return Collections.emptyList();
            }

            try (Reader reader = Files.newBufferedReader(target())) {
                ContactsXmlRootWrapper c = (ContactsXmlRootWrapper) this.stream.fromXML(reader);
                return c.contacts();
            }

        } catch (IOException exc) {
            throw new ContactRepositoryException("An error occurred during reading contacts from file " + target() + ".", exc);
        }
    }

    void write(Collection<Contact> contacts) throws ContactRepositoryException {

        ContactsXmlRootWrapper root = new ContactsXmlRootWrapper(contacts);
        String content = this.stream.toXML(root);

        try {
            Files.write(target(), content.getBytes());
        } catch (IOException exc) {
            throw new ContactRepositoryException("An error occurred during writing contacts to a file " + target() + ".", exc);
        }
    }
}
