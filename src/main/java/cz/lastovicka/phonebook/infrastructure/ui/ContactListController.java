package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.application.service.PhoneBookException;
import cz.lastovicka.phonebook.application.service.PhoneBookService;
import cz.lastovicka.phonebook.domain.model.contact.ContactDescription;
import cz.lastovicka.phonebook.infrastructure.ui.event.*;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This controller represents a list view of all the
 * contacts that can be sees on the left of the screen.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class ContactListController extends UIFragmentController {

    private final PhoneBookService service;
    private final ListView<ContactDescription> contactList;

    ContactListController(EventBus eventBus, PhoneBookService service, ListView<ContactDescription> contactList) {
        super(eventBus);

        this.service = service;
        this.contactList = contactList;
    }

    void initialize() {
        super.initialize();

        this.contactList.setCellFactory(p -> new ContactDescriptionListCell());
        this.contactList.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n == null) {
                return;
            }
            postEvent(new ContactSelectedEvent(n));
        });

        unselect();
    }

    ContactDescription selectedContact() {
        return contactList.getSelectionModel().getSelectedItem();
    }

    @Subscribe
    void onPhoneBookReady(PhoneBookReadyEvent event) {

        try {
            Collection<ContactDescription> contacts = service.allContactDescriptions();
            this.contactList.getItems().addAll(contacts);
        } catch (PhoneBookException exc) {
            postEvent(LogEvent.error(exc.getMessage()));
        }
    }

    @Subscribe
    void onPhoneBookClosed(ClosePhoneBookEvent event) {
        this.contactList.getItems().clear();
    }

    @Subscribe
    void onContactAdded(ContactAddedEvent event) {
        this.contactList.getItems().add(event.contact());
    }

    @Subscribe
    void onContactDeleted(ContactDeletedEvent event) {
        this.contactList.getItems().remove(event.contact());
    }

    @Subscribe
    void onContactSearched(SearchContactEvent event) {
        selectContactIfMatches(event.text());
    }

    private void selectContactIfMatches(String text) {
        if (text.isEmpty()) {
            unselect();
            return;
        }

        List<ContactDescription> contacts = contactList.getItems();
        Optional<ContactDescription> maybeDescription = contacts.
                stream()
                .filter(c -> c.name().fullName().toLowerCase().contains(text.toLowerCase()))
                .findFirst();

        if (!maybeDescription.isPresent()) {
            unselect();
            return;
        }

        ContactDescription contact = maybeDescription.get();

        postEvent(new ContactSelectedEvent(contact));
        contactList.getSelectionModel().select(contact);
    }

    private void unselect() {
        this.contactList.getSelectionModel().select(-1);
    }

    //----------------------------------------------------------------------------------
    //
    //----------------------------------------------------------------------------------

    /**
     * @author Jan Lastovicka
     * @since 2019-01-23
     */
    private final static class ContactDescriptionListCell extends ListCell<ContactDescription> {
        @Override
        protected void updateItem(ContactDescription item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                return;
            }

            String fullName = item.name().fullName();
            setText(fullName);
        }
    }
}
