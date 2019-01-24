package cz.lastovicka.phonebook.domain.model.contact;

/**
 * A value object representing name of a contact. It is a part
 * of Contact aggregate. It describes the contact with owner name.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactName {

    /**
     * Creates a new name object from first name and last name
     * @param firstName must not be null or empty.
     * @param lastName must not be null or empty.
     * @return never null
     * @throws IllegalArgumentException if firstNamr and lastName is null or empty
     */
    public static ContactName from(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name must not be null or empty.");
        }

        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name must not be null or empty.");
        }

        return new ContactName(firstName, lastName);
    }

    /**
     * Creates a new contact name from full name which must consist of two
     * words - first name and last name.
     *
     * @param fullname must one null or empty.
     * @return never null
     * @throws IllegalArgumentException if fullname is null, empty or does not
     * consists of two words.
     */
    public static ContactName fromFullName(String fullname) {
        if (fullname == null || fullname.isEmpty()) {
            throw new IllegalArgumentException("Fullname must not be null or empty.");
        }

        String[] parts = fullname.split("\\s");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Unexpected format parse full name. It should consist parse two parts - first name and last name separated by s apce.");
        }

        return new  ContactName(parts[0], parts[1]);
    }

    //-----------------------------------------------------------------
    //INSTANCE SCOPE
    //-----------------------------------------------------------------

    private final String firstName;
    private final String lastName;

    private ContactName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactName)) {
            return false;
        }

        ContactName that = (ContactName) obj;

        return this.fullName().equals(that.fullName());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + fullName().hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "ContactName[" + fullName() + "]";
    }
}
