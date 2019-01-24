package cz.lastovicka.phonebook.domain.model.contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * An aggregate - central concept representing complete
 * contact - name and phone numbers
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class Contact {

    private ContactId id;
    private ContactName name;
    private ArrayList<PhoneNumber> numbers;

    public Contact(ContactId id, ContactName name, PhoneNumber... numbers) {
        this(id, name, Arrays.asList(numbers));
    }

    public Contact(ContactId id, ContactName name, Collection<PhoneNumber> numbers) {
        this.id = id;
        this.name = name;
        this.numbers = new ArrayList<>(numbers);
    }

    private Contact() {}

    public ContactDescription description() {
        return new ContactDescription(id, name);
    }

    public ContactId id() {
        return id;
    }

    public ContactName name() {
        return name;
    }

    public Collection<PhoneNumber> numbers() {
        return Collections.unmodifiableCollection(numbers);
    }
}
