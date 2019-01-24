package cz.lastovicka.phonebook.domain.model.contact;

/**
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactDescription {

    private final ContactId id;
    private final ContactName name;

    public ContactDescription(ContactId id, ContactName name) {
        this.id = id;
        this.name = name;
    }

    public ContactId id() {
        return id;
    }

    public ContactName name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactDescription)) {
            return false;
        }

        ContactDescription that = (ContactDescription) obj;

        if (!this.id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + id.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "ContactDescription[" + id.asString() + ":" + name.fullName() + "]";
    }
}
