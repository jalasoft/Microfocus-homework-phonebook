package cz.lastovicka.phonebook.functional;

import cz.lastovicka.phonebook.Utils;
import cz.lastovicka.phonebook.application.service.PhoneBookService;
import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;
import cz.lastovicka.phonebook.domain.model.contact.ContactName;
import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;
import cz.lastovicka.phonebook.infrastructure.repository.contact.ContactRepositoryFactory;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlFileContactsSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static cz.lastovicka.phonebook.ContactMatcher.contact;
import static cz.lastovicka.phonebook.Contacts.JOHN_CONTACT;
import static cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlFileContactsSource.Mode.CREATE_IF_NOT_EXISTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;
import static java.util.Arrays.*;

/**
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public class PhoneBookServiceTest {

    private Path xmlFile;
    private PhoneBookService service;

    @BeforeClass
    public void init() {
        ContactRepositoryFactory repoFactory = new ContactRepositoryFactory();
        service = new PhoneBookService(repoFactory);
    }

    @BeforeMethod
    public void initFile() throws IOException {
        xmlFile = Files.createTempFile("test1", ".xml");
    }

    @AfterMethod
    public void cleanFile() throws IOException {
        Files.deleteIfExists(xmlFile);
    }

    @Test
    public void newPhoneBookCanBeFilledWithContactsAndRead() throws Exception {

        XmlFileContactsSource source = XmlFileContactsSource.newSource(xmlFile, CREATE_IF_NOT_EXISTS);

        service.openPhoneBook(source);

        assertTrue(service.isOpen());

        Collection<ContactDescription> noDescriptions = service.allContactDescriptions();
        assertNotNull(noDescriptions);
        assertThat(noDescriptions, is(empty()));


        service.addContact(ContactName.fromFullName("Josef Svejk"), asList(PhoneNumber.parse("+420 456 243243")));
        service.addContact(ContactName.fromFullName("Jiri Novak"), asList(PhoneNumber.parse("+420 321 777888")));
        service.addContact(ContactName.fromFullName("Antonin Ponozka"), asList(PhoneNumber.parse("+420 689 576908")));

        Collection<ContactDescription> descriptions = service.allContactDescriptions();
        assertNotNull(descriptions);
        assertEquals(descriptions.size(), 3);

        Collection<Contact> found = service.contactByName(ContactName.fromFullName("Josef Svejk"));
        assertNotNull(found);
        assertEquals(found.size(), 1);

        Contact actual = found.iterator().next();

        assertThat(actual, is(contact()
                .name("Josef Svejk")
                .number("+420 456 243243")));

        service.closePhoneBook();

        assertFalse(service.isOpen());
    }

    @Test
    public void newPhoneBookCanBeFiledClosedAndOpenedAgain() throws Exception {

        XmlFileContactsSource source = XmlFileContactsSource.newSource(xmlFile, CREATE_IF_NOT_EXISTS);

        service.openPhoneBook(source);
        service.addContact(ContactName.fromFullName("Antonin Ponozka"), asList(PhoneNumber.parse("+420 689 576908")));
        service.closePhoneBook();

        service.openPhoneBook(source);

        Collection<Contact> contacts = service.contactByName(ContactName.fromFullName("Antonin Ponozka"));
        assertNotNull(contacts);
        assertEquals(contacts.size(), 1);

        Contact contact = contacts.iterator().next();
        assertThat(contact, is(contact().name("Antonin Ponozka").number("+420 689 576908")));

        service.closePhoneBook();

        //assert that the xml file physically exists and it is not empty
        assertTrue(Files.exists(xmlFile));
        assertTrue(Files.size(xmlFile) > 0);
    }

    @Test
    public void anExistingFileCanBeOpenedReadAndEdited() throws Exception {
        Path sourceFile = Utils.prepareExampleXml();

        service.openPhoneBook(XmlFileContactsSource.newSource(sourceFile));

        try {
            Collection<ContactDescription> descriptions = service.allContactDescriptions();
            assertNotNull(descriptions);
            assertEquals(descriptions.size(), 6);

            service.removeContact(JOHN_CONTACT.id());

            Collection<ContactDescription> descriptionsAfterRemoval = service.allContactDescriptions();
            assertEquals(descriptionsAfterRemoval.size(), 5);

            Collection<Contact> shoudBeEmpty = service.contactByName(JOHN_CONTACT.name());
            assertNotNull(shoudBeEmpty);
            assertTrue(shoudBeEmpty.isEmpty());

            service.addContact(ContactName.fromFullName("Dave Gahan"), asList(PhoneNumber.parse("+001 354 287756")));

            Collection<Contact> found = service.contactByName(ContactName.fromFullName("Dave Gahan"));
            assertNotNull(found);
            assertEquals(found.size(), 1);

            Contact dave = found.iterator().next();

            assertThat(dave, is(contact().name("Dave Gahan").number("+001 354 287756")));

        } finally {
            service.closePhoneBook();

            Files.deleteIfExists(sourceFile);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void notExistingPhoneBookCannotBeOpened() throws Exception {

        Path notExistingFile = Paths.get("/home/tonda/1233333");

        //we do not explicitly want to create a new file if it odes not exist.
        service.openPhoneBook(XmlFileContactsSource.newSource(notExistingFile));
    }
}
