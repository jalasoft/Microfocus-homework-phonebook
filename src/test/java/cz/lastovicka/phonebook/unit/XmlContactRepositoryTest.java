package cz.lastovicka.phonebook.unit;

import cz.lastovicka.phonebook.Utils;
import cz.lastovicka.phonebook.domain.model.contact.*;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlContactRepository;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlFileContactsSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static cz.lastovicka.phonebook.Contacts.*;
import static org.testng.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static cz.lastovicka.phonebook.ContactMatcher.*;

/**
 * Unit tests that make me sure the repository works as expected.
 * All expected use cases are tested.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 **/
public class XmlContactRepositoryTest {

    private Path tempFile;
    private ContactRepository repository;

    @DataProvider
    public Object[][] existingNamesAndContactsProvider() {
        return new Object[][] {
                { PAUL_CONTACT.name(), PAUL_CONTACT},
                { JOHN_CONTACT.name(), JOHN_CONTACT},
                { GEORGE_CONTACT.name(), GEORGE_CONTACT}
        };
    }

    @DataProvider
    public Object[][] notExistingNamesAndContactsProvider() {
        return new Object[][] {
                { NOT_EXISTING_CONTACT.name()}
        };
    }

    @BeforeMethod
    void prepareFile() throws IOException, URISyntaxException {

        //prepare test data file from resources and copy it to temp in order
        //to modify the file - add/remove contacts
        tempFile = Utils.prepareExampleXml();

        //prepare tested object
        repository = new XmlContactRepository(XmlFileContactsSource.newSource(tempFile));
    }

    @AfterMethod
    void cleanTempFile() throws IOException {
        Files.delete(tempFile);
    }

    @Test
    public void allContactDescriptionsAreFetched() throws Exception {

        Collection<ContactDescription> descriptions = repository.allDescriptions();

        assertNotNull(descriptions);
        assertEquals(descriptions.size(),6);

        ContactDescription expected = GEORGE_CONTACT.description();

        assertThat(descriptions, hasItem(expected));
    }

    @Test
    public void allContactsAreFetched() throws Exception {

        Collection<Contact> all = repository.all();

        assertNotNull(all);
        assertEquals(all.size(), 6, "Unexpected number of contacts.");

        //just test that one of the items is really in the list
        assertThat(all, hasItem(contact()
                .id("00000000-0000-0000-0000-000000000002")
                .name("Paul McCartney")
                .number("+888 333 333333")
                .number("+444 444 444444")));

        //in the same fashion I could test the others.....but....this is not real project...
    }

    @Test(dataProvider = "existingNamesAndContactsProvider")
    public void aContactCanBeFoundByName(ContactName name, Contact expectedContact) throws Exception {

        Collection<Contact> contacts = repository.byName(name);

        assertNotNull(contacts);
        assertEquals(contacts.size(), 1);

        Contact actual = contacts.iterator().next();

        assertThat(actual, is(contact(expectedContact)));
    }

    @Test(dataProvider = "notExistingNamesAndContactsProvider")
    public void notExistingContactCannotBeFoundByName(ContactName name) throws Exception {

        Collection<Contact> contacts = repository.byName(name);

        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    @Test
    public void anExistingContactIsRemovedAndNoMoreAvailable() throws Exception {

        boolean result = repository.remove(JOHN_CONTACT.id());

        assertTrue(result);

        Collection<Contact> contacts = repository.byName(JOHN_CONTACT.name());

        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    @Test
    public void notExistingContactCannotBeRemoved() throws Exception {

        boolean result = repository.remove(NOT_EXISTING_CONTACT.id());

        assertFalse(result);
    }

    @Test
    public void newContactCanBeAdded() throws Exception {

        Contact contact = repository.newContact(ContactName.fromFullName("Honza Lastovicka"), PhoneNumber.parse("+420 602 662158"));
        repository.add(contact);

        Collection<Contact> contacts = repository.byName(contact.name());

        assertNotNull(contact);
        assertEquals(contacts.size(), 1);

        Contact me = contacts.iterator().next();

        assertThat(me, is(contact(contact)));
    }

}
