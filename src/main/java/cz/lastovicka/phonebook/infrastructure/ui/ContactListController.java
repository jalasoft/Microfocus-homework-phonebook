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

/**
 * This controller represents a list view of all the
 * contacts that can be sees on the left of the screen.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class ContactListController {

    private final EventBus eventBus;
    private final PhoneBookService service;
    private final ListView<ContactDescription> contactList;


    ContactListController(EventBus eventBus, PhoneBookService service, ListView<ContactDescription> contactList) {
        this.eventBus = eventBus;
        this.service = service;
        this.contactList = contactList;
    }

    void initialize() {

        this.contactList.setCellFactory(p -> new ContactDescriptionListCell());
        this.contactList.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n == null) {
                return;
            }
            eventBus.post(new ContactSelectedEvent(n));
        });
        this.eventBus.register(this);
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
            this.eventBus.post(LogEvent.error(exc.getMessage()));
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
