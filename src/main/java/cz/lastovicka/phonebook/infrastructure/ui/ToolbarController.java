package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.infrastructure.ui.event.ClosePhoneBookEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.DeleteContactEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.NewContactEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.PhoneBookReadyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A controller of a tool bar offering a set of action via buttons
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class ToolbarController {

    private static final String NEW_CONTACT_IMAGE_NAME = "new.png";
    private static final String DELETE_CONTACT_IMAGE_NAME = "delete.png";

    private final EventBus eventBus;
    private final ToolBar toolbar;

    private Button newContact;
    private Button deleteContact;

    ToolbarController(EventBus eventBus, ToolBar toolBar) {
        this.eventBus = eventBus;
        this.toolbar = toolBar;
    }

    void initialize() {

        this.newContact = newContactButton();
        this.deleteContact = deleteContactButton();

        this.toolbar.getItems().addAll(
                newContact,
                deleteContact
        );

        eventBus.register(this);
    }

    private Button newContactButton() {
        Image newImage = new Image(getClass().getClassLoader().getResourceAsStream(NEW_CONTACT_IMAGE_NAME), 20, 20, true, true);
        Button button = new Button("", new ImageView(newImage));
        button.setOnAction(e -> eventBus.post(new NewContactEvent()));
        button.setDisable(true);

        return button;
    }

    private Button deleteContactButton() {
        Image deleteImage = new Image(getClass().getClassLoader().getResourceAsStream(DELETE_CONTACT_IMAGE_NAME), 20, 20, true, true);
        Button button = new Button("", new ImageView(deleteImage));
        button.setOnAction(e -> eventBus.post(new DeleteContactEvent()));
        button.setDisable(true);

        return button;
    }

    @Subscribe
    void onPhoneBookReady(PhoneBookReadyEvent event) {
        deleteContact.setDisable(false);
        newContact.setDisable(false);
    }

    @Subscribe
    void onPhoneBookClose(ClosePhoneBookEvent event) {
        deleteContact.setDisable(true);
        newContact.setDisable(true);
    }
}
