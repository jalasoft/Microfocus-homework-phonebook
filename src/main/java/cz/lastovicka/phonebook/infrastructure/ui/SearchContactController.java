package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import cz.lastovicka.phonebook.infrastructure.ui.event.SearchContactEvent;
import javafx.scene.control.TextField;


/**
 * @author Jan Lastovicka
 * @since 2019-01-25
 */
final class SearchContactController extends UIFragmentController {

    private final TextField searchTxt;

    SearchContactController(EventBus eventBus, TextField searchTxt) {
        super(eventBus);

        this.searchTxt = searchTxt;
    }

    @Override
    void initialize() {
        super.initialize();

        searchTxt.textProperty().addListener(e -> {
            String text = searchTxt.getText();
            postEvent(new SearchContactEvent(text.trim()));
        });
    }
}
