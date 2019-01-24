package cz.lastovicka.phonebook.infrastructure.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cz.lastovicka.phonebook.infrastructure.ui.event.ClosePhoneBookEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.ExitAppEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.NewPhoneBookEvent;
import cz.lastovicka.phonebook.infrastructure.ui.event.OpenPhoneBookEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * A controller of a menu - offering basic actions like
 * open, close, exit or new.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
final class MenuController {

    private final MenuBar menuBar;
    private final EventBus eventBus;

    private MenuItem newItem;
    private MenuItem openItem;
    private MenuItem closeItem;
    private MenuItem exitItem;

    MenuController(EventBus eventBus, MenuBar menuBar) {
        this.eventBus = eventBus;
        this.menuBar = menuBar;
    }

    void initialize() {
        newItem = newFile();
        openItem = open();
        closeItem = close();
        exitItem = exit();

        Menu menu = new Menu("PhoneBook");
        menu.getItems().addAll(
                newItem,
                new SeparatorMenuItem(),
                openItem,
                closeItem,
                new SeparatorMenuItem(),
                exitItem
        );

        menuBar.getMenus().addAll(
                menu
        );

        eventBus.register(this);
    }

    @Subscribe
    void onOpenPhoneBook(OpenPhoneBookEvent event) {
        newItem.setDisable(true);
        openItem.setDisable(true);
        closeItem.setDisable(false);
    }

    @Subscribe
    void onNewPhoneBook(NewPhoneBookEvent event) {
        newItem.setDisable(true);
        openItem.setDisable(true);
        closeItem.setDisable(false);
    }

    @Subscribe
    void onClosePhoneBook(ClosePhoneBookEvent event) {
        newItem.setDisable(false);
        openItem.setDisable(false);
        closeItem.setDisable(true);
    }

    private MenuItem newFile() {
        MenuItem item = new MenuItem("New...");
        item.setOnAction(e -> eventBus.post(new NewPhoneBookEvent()));

        return item;
    }

    private MenuItem open() {
        MenuItem item = new MenuItem("Open...");
        item.setOnAction(e -> eventBus.post(new OpenPhoneBookEvent()));

        return item;
    }

    private MenuItem close() {
        MenuItem item = new MenuItem("Close");
        item.setDisable(true);
        item.setOnAction(e -> eventBus.post(new ClosePhoneBookEvent()));

        return item;
    }

    private MenuItem exit() {
        MenuItem item = new MenuItem("Exit");
        item.setOnAction(e -> eventBus.post(new ExitAppEvent()));

        return item;
    }

}
