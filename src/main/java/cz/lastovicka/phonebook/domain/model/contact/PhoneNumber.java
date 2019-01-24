package cz.lastovicka.phonebook.domain.model.contact;

import java.util.regex.Pattern;

/**
 * A value class that serves as a description of the
 * aggregate Contact. It represents one phone number.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class PhoneNumber {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\+\\d{3}\\s\\d{3}\\s\\d{6}");

    /**
     * Creates a new phone number from string by parsing a
     * string that must conform to a pattern +XXX XXX XXXXXX
     * where XXX is a digit.
     *
     * @param value must not be null or empty and must conform to the pattern
     * @return never null
     * @throws IllegalArgumentException if value is null, empty or does not conform to the pattern
     */
    public static PhoneNumber parse(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Phone number must not be null or empty.");
        }

        if (!PHONE_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Phone number must match format: " + PHONE_NUMBER_PATTERN);
        }

        return new PhoneNumber(value);
    }

    //-------------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------------

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PhoneNumber)) {
            return false;
        }

        PhoneNumber that = (PhoneNumber) obj;

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
        return "PhoneNumber[" + value + "]";
    }
}
