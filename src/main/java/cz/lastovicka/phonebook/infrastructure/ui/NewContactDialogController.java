package cz.lastovicka.phonebook.infrastructure.ui;

import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.*;

/**
 * A controller of a dialog that pops up when
 * the user wants to create a new contact
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class NewContactDialogController {

    private final static String FXML_RESOURCE = "contact_detail.fxml";

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField surnameTxt;

    @FXML
    private TextArea phonesTxt;

    private Dialog<ContactData> dialog;

    private Button saveButton;
    private Button cancelButton;

    void initialize() {

        dialog = new Dialog<>();
        dialog.setTitle("New contact");

        Parent content = loadContent();
        dialog.getDialogPane().getButtonTypes().clear();

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().clear();


        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            boolean valid = validateInput();

            if (!valid) {
                ae.consume();
            }
        });

        dialog.setResultConverter(btn -> {
            if (btn == cancelButtonType) {
                cleanUpFields();
                //dialog.hide();
                return null;
            }

            Set<String> phones = new HashSet<>(Arrays.asList(phonesTxt.getText().split("\n")));

            ContactData data = new ContactData(
                    nameTxt.getText(),
                    surnameTxt.getText(),
                    new HashSet<>(phones));

            cleanUpFields();
            dialog.hide();
            return data;
        });
    }

    private void cleanUpFields() {
        nameTxt.setText("");
        surnameTxt.setText("");
        phonesTxt.setText("");
    }

    private boolean validateInput() {
        return validateFirstName() &&
        validateLastName() &&
        validatePhoneNumbers();
    }

    private boolean validateFirstName() {
        String name = nameTxt.getText();
        if (name == null || name.isEmpty()) {
            reportValidationError("First name must not be empty.");
            return false;
        }

        return true;
    }

    private boolean validateLastName() {
        String lastName = surnameTxt.getText();
        if (lastName == null || lastName.isEmpty()) {
            reportValidationError("Last name must not be empty.");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumbers() {
        String[] phones = phonesTxt.getText().split("\n");

        for(String number : phones) {
            try {
                PhoneNumber.parse(number);
            } catch (IllegalArgumentException exc) {
                reportValidationError(exc.getMessage());
                return false;
            }
        }
        return true;
    }

    private void reportValidationError(String message) {
        dialog.setHeaderText(message);
    }

    private Parent loadContent() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(FXML_RESOURCE));
        loader.setController(this);

        try {
            return loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    Optional<ContactData> showAndWait() {
        Optional<ContactData> maybeData = dialog.showAndWait();

        return maybeData;
    }

    //------------------------------------------------------------
    //CONTAINER OF INSERTED FIELDS
    //------------------------------------------------------------

    final static class ContactData {

        private final String firstName;
        private final String lastName;
        private final Set<String> phoneNumbers;

        ContactData(String firstName, String lastName, Set<String> phoneNumbers) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumbers = phoneNumbers;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public Set<String> getPhoneNumbers() {
            return phoneNumbers;
        }
    }
}
