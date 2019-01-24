package cz.lastovicka.phonebook.domain.model.contact;

/**
 * An exception represnting an error that occurred during
 * accession contact repository
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactRepositoryException extends Exception {

    public ContactRepositoryException(String reason, Exception cause) {
        super(cause);
    }

    public ContactRepositoryException(String reason) {
        super(reason);
    }

}
