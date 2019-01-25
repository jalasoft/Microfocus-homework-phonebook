package cz.lastovicka.phonebook.infrastructure.ui.event;

/**
 * An event that indicates that the user is looking for
 * a contact.
 *
 * @author Jan Lastovicka
 * @since 2019-01-25
 */
public final class SearchContactEvent {

    private final String text;

    /**
     *
     * @param text must not be null, might be empty.
     * @throws IllegalArgumentException if text is null.
     */
    public SearchContactEvent(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }
        this.text = text;
    }

    /**
     *
     * @return never null, might be empty
     */
    public String text() {
        return text;
    }
}
