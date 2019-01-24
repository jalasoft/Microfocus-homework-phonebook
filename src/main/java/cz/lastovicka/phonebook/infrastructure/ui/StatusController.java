package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.infrastructure.ui.event.LogEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.PhoneBookClosedEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.PhoneBookReadyEvent;
import javafx.scene.control.Label;

/**
 * A controller representing a status bar that displays
 * important message regarding status of the application
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class StatusController {

    private final EventBus eventBus;
    private final Label label;

    StatusController(EventBus eventBus, Label label) {
        this.eventBus = eventBus;
        this.label = label;
    }

    void initialize() {
        this.eventBus.register(this);

        label.setText("Ready");
    }

    @Subscribe
    void onLog(LogEvent event) {
        String text = event.severity() + ": " +event.message();
        label.setText(text);
    }

    @Subscribe
    void onPhoneBookReady(PhoneBookReadyEvent event) {
        String description = event.source().description();
        String origin = originSpelled(event);

        String message = "Phone book " + origin + ", " + description;

        label.setText(message);
    }

    private String originSpelled(PhoneBookReadyEvent event) {
        switch(event.origin()) {
            case NEW:
                return "created";

            case EXISTING:
                return "opened";

                default:
                    throw new IllegalStateException("Unexpected origin: " + event.origin());
        }
    }

    @Subscribe
    void onPhoneBookClosed(PhoneBookClosedEvent event) {
        String description = event.source().description();
        label.setText("Phone book closed, " + description);
    }
}
