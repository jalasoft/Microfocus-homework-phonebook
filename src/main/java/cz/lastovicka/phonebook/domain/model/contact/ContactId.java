package cz.lastovicka.phonebook.domain.model.contact;

import java.util.UUID;

/**
 * Unique identifier of a contact.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactId {

    /**
     * Creates a new id from string
     *
     * @param value a string as a surce of the id, must be in UUID format
     * @return never null
     * @throws IllegalArgumentException if value is null, emmpty or does not
     * conform to UUID format
     */
    public static ContactId from(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Contact id must not be null or empty.");
        }
        return from(UUID.fromString(value));
    }

    /**
     * Creates a new id from UUID
     *
     * @param uuid must not be null
     * @return never null
     * @throws IllegalArgumentException if uuid is null
     */
    public static ContactId from(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("ContactId uuid must not be null.");
        }

        return new ContactId(uuid);
    }

    //--------------------------------------------------------
    //INSTANCE SCOPE
    //--------------------------------------------------------

    private final UUID value;

    private ContactId(UUID value) {
        this.value = value;
    }

    public UUID asUuid() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactId)) {
            return false;
        }

        ContactId that = (ContactId) obj;

        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + value.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "ContactId[" + value + "]";
    }
}
