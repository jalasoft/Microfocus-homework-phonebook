package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.application.service.PhoneBookException;
import cz.lastovicka.phonebook.application.service.PhoneBookService;
import cz.lastovicka.phonebook.domain.model.contact.Contact;
import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;
import cz.lastovicka.phonebook.infrastructure.ui.event.ContactSelectedEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.LogEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.PhoneBookClosedEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * This controller is a contact detail that can be seen
 * on the right of the screen.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class ContactDetailController {

    private static final String FXML_RESOURCE = "contact_detail.fxml";

    private final EventBus eventBus;
    private final PhoneBookService service;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField surnameTxt;

    @FXML
    private TextArea phonesTxt;

    @FXML
    private GridPane detailPane;

    ContactDetailController(EventBus eventBus, PhoneBookService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    void initialize() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ContactDetailController.class.getClassLoader().getResource(FXML_RESOURCE));
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }

        nameTxt.setEditable(false);
        surnameTxt.setEditable(false);
        phonesTxt.setEditable(false);
        phonesTxt.setPrefRowCount(5);
        eventBus.register(this);
    }

    @Subscribe
    void onPhoneBookClosed(PhoneBookClosedEvent event) {
        nameTxt.setText("");
        surnameTxt.setText("");
        phonesTxt.setText("");
    }

    @Subscribe
    void onContactSelected(ContactSelectedEvent event) {

        try {
            Contact contact = service.contactById(event.contact().id());
            showContact(contact);
        } catch (PhoneBookException exc) {
            exc.printStackTrace();
            eventBus.post(LogEvent.error(exc.getMessage()));
        }
    }


    private void showContact(Contact contact) {
        nameTxt.setText(contact.name().firstName());
        surnameTxt.setText(contact.name().lastName());

        String phonesAsText = contact.numbers()
                .stream()
                .map(PhoneNumber::value)
                .collect(Collectors.joining("\n"));

        phonesTxt.setText(phonesAsText);
    }

    Pane panel() {
        return detailPane;
    }
}
