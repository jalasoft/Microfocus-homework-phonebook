package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.application.service.PhoneBookException;
import cz.lastovicka.phonebook.application.service.PhoneBookService;
import cz.lastovicka.phonebook.domain.model.contact.*;
import cz.lastovicka.phonebook.infrastructure.repository.contact.xml.XmlFileContactsSource;
import cz.lastovicka.phonebook.infrastructure.ui.event.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A main controller of frontend. This class represents
 * a driver of the application. It mainly contains event
 * handlers to coordinate operations triggered by the user.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class MainController {

    static final String MAIN_FXML_RESOURCE = "main.fxml";

    //---------------------------------------------------------------
    //INJECTED CONTROLS
    //---------------------------------------------------------------

    @FXML
    private BorderPane mainPanel;

    @FXML
    private SplitPane splitPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ToolBar toolBar;

    @FXML
    private ListView<ContactDescription> contactList;

    @FXML
    private Label statusLbl;

    @FXML
    private TextField searchTxt;

    //---------------------------------------------------------------
    //
    //---------------------------------------------------------------

    private final Stage mainWindow;
    private final EventBus eventBus;
    private final PhoneBookService service;

    private ContactListController contactListController;
    private ContactDetailController contactDetailController;
    private MenuController menuController;
    private ToolbarController toolbarController;
    private StatusController statusController;
    private NewContactDialogController newContactController;
    private SearchContactController searchContactController;

    public MainController(Stage mainWindow, EventBus eventBus, PhoneBookService service) {
        this.mainWindow = mainWindow;
        this.eventBus = eventBus;
        this.service = service;
    }

    @FXML
    public void initialize() {

        this.contactListController = new ContactListController(eventBus, service, contactList);
        this.contactListController.initialize();

        this.contactDetailController = new ContactDetailController(eventBus, service);
        this.contactDetailController.initialize();
        this.splitPane.getItems().add(contactDetailController.panel());

        this.toolbarController = new ToolbarController(eventBus, toolBar);
        this.toolbarController.initialize();

        this.menuController = new MenuController(eventBus, menuBar);
        this.menuController.initialize();

        this.statusController = new StatusController(eventBus, statusLbl);
        this.statusController.initialize();

        this.newContactController = new NewContactDialogController();
        this.newContactController.initialize();

        this.searchContactController = new SearchContactController(eventBus, searchTxt);
        this.searchContactController.initialize();

        this.eventBus.register(this);
    }

    public void populate() {
        //load UI xml descriptor
        URL fxmlResource = getClass().getClassLoader().getResource(MAIN_FXML_RESOURCE);

        //load UI based on the xml descriptor
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(fxmlResource);

        try {
            loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public Parent panel() {
        return mainPanel;
    }

    @Subscribe
    void onNewContactEvent(NewContactEvent event) {

        Optional<NewContactDialogController.ContactData> maybeData = newContactController.showAndWait();

        if (!maybeData.isPresent()) {
            return;
        }

        NewContactDialogController.ContactData data = maybeData.get();

        ContactName name = ContactName.from(data.getFirstName(), data.getLastName());
        Set<PhoneNumber> numbers = data.getPhoneNumbers().stream().map(PhoneNumber::parse).collect(Collectors.toSet());

        try {
            Contact newContact = service.addContact(name, numbers);
            eventBus.post(new ContactAddedEvent(newContact.description()));
        } catch (PhoneBookException exc) {
            exc.printStackTrace();
            eventBus.post(LogEvent.error(exc.getMessage()));
        }
    }

    @Subscribe
    void onDeleteContact(DeleteContactEvent event) {

        try {
            ContactDescription contact = contactListController.selectedContact();
            if (contact == null) {
                return;
            }

            service.removeContact(contact.id());
            eventBus.post(new ContactDeletedEvent(contact));
        } catch (PhoneBookException exc) {
            exc.printStackTrace();
            eventBus.post(LogEvent.error(exc.getMessage()));
        }
    }

    @Subscribe
    void onOpenPhoneBook(OpenPhoneBookEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select contacts file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));

        File selectedFile = chooser.showOpenDialog(mainWindow);

        if (selectedFile == null) {
            return;
        }

        ContactsSource source = XmlFileContactsSource.newSource(selectedFile.toPath());
        service.openPhoneBook(source);

        this.eventBus.post(PhoneBookReadyEvent.existingPhoneBook(service.source()));
    }

    @Subscribe
    void onNewPhoneBook(NewPhoneBookEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select location for new file.");
        File file = chooser.showSaveDialog(mainWindow);

        if (file == null) {
            return;
        }

        ContactsSource source = XmlFileContactsSource.newSource(file.toPath(), XmlFileContactsSource.Mode.CREATE_IF_NOT_EXISTS);
        service.openPhoneBook(source);

        this.eventBus.post(PhoneBookReadyEvent.newPhoneBook(service.source()));
    }

    @Subscribe
    void onClose(ClosePhoneBookEvent event) {
        try {
            ContactsSource source = service.source();
            service.closePhoneBook();
            contactList.getItems().clear();
            eventBus.post(new PhoneBookClosedEvent(source));
        } catch (PhoneBookException exc) {
            eventBus.post(LogEvent.error(exc.getMessage()));
        }
    }

    @Subscribe
    void onExit(ExitAppEvent event) {
        Platform.exit();
    }
}
