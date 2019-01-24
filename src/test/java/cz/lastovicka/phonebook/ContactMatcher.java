package cz.lastovicka.phonebook;

import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.ContactId;
import cz.lastovicka.phonebook.domain.model.contact.ContactName;
import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;

/**
 * Hamcrest matcher that allows testing contacts via Matcher
 * and assertThat(...) construct.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 **/
public final class ContactMatcher extends TypeSafeMatcher<Contact> {

    public static ContactMatcher contact(Contact contact) {
        return contact()
                .id(contact.id())
                .name(contact.name())
                .numbers(contact.numbers());
    }

    public static ContactMatcher contact() {
        return new ContactMatcher();
    }

    public ContactMatcher id(ContactId id) {
        this.id = id;
        return this;
    }

    public ContactMatcher id(String id) {
        ContactId contactId = ContactId.from(id);
        this.id = contactId;

        return this;
    }

    public ContactMatcher name(ContactName name) {
        this.name = name;
        return this;
    }

    public ContactMatcher name(String name) {
        ContactName contactName = ContactName.fromFullName(name);
        this.name = contactName;

        return this;
    }

    public ContactMatcher number(PhoneNumber number) {
        this.numbers.add(number);
        return this;
    }

    public ContactMatcher number(String number) {
        PhoneNumber phoneNumber = PhoneNumber.parse(number);
        this.numbers.add(phoneNumber);

        return this;
    }

    public ContactMatcher numbers(Collection<PhoneNumber> numbers) {
        this.numbers.addAll(numbers);
        return this;
    }

    //-----------------------------------------------------------------
    //INSTANCE SCOPE
    //-----------------------------------------------------------------

    private ContactId id;
    private ContactName name;
    private Collection<PhoneNumber> numbers = new ArrayList<>();

    private final List<String> errors = new ArrayList<>();

    @Override
    protected boolean matchesSafely(Contact contact) {

        errors.clear();

        assertId(contact);
        assertName(contact);
        assertNumbers(contact);

        return errors.isEmpty();
    }

    private void assertId(Contact contact) {
        if (id != null) {
            if (!contact.id().equals(id)) {
                errors.add("Expected id: " + id.asString() + ", actual: " + contact.id().asString());
            }
        }
    }

    private void assertName(Contact contact) {
        if (name != null) {
            if (!contact.name().equals(name)) {
                errors.add("Expected name: " + name.fullName() + ", actual: " + contact.name().fullName());
            }
        }
    }

    private void assertNumbers(Contact contact) {
        Set<PhoneNumber> actual = new HashSet<>(contact.numbers());
        Set<PhoneNumber> expected = new HashSet<>(numbers);

        if (!actual.equals(expected)) {
            errors.add("Expected numbers: " + expected + ", actual: " + actual);
        }
    }

    @Override
    public void describeTo(Description description) {
        errors.forEach(description::appendText);
    }
}
