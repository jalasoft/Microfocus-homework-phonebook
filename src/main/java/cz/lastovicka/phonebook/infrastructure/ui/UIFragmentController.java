package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;

/**
 * A parent of all controllers
 *
 * @author Jan Lastovicka
 * @since 2019-01-25
 */
abstract class UIFragmentController {

    private final EventBus eventBus;

    /**
     *
     * @param eventBus must not be null
     * @throws IllegalArgumentException if eventBus is null
     */
    UIFragmentController(EventBus eventBus) {
        if (eventBus == null) {
            throw new IllegalArgumentException("Event bus must not be null.");
        }

        this.eventBus = eventBus;
    }

    /**
     * initializes the controller.
     */
    void initialize() {
        eventBus.register(this);
    }

    final void postEvent(Object object) {
        eventBus.post(object);
    }
}
