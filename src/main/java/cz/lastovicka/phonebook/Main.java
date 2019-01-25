package cz.lastovicka.phonebook;

import cz.lastovicka.phonebook.infrastructure.ui.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main entry point to the application Phone Book.
 * This application allows managing contacts via user interface.
 *
 *
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 **/
public final class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    //---------------------------------------------------------
    //INSTANCE SCOPE
    //---------------------------------------------------------

    @Override
    public void start(Stage primaryStage) {

        //configure application components and get UI controller
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        context.getBeanFactory().registerSingleton("mainWindow", primaryStage);

        MainController mainController = context.getBean(MainController.class);
        mainController.populate();

        //show window
        Scene scene = new Scene(mainController.panel());

        primaryStage.setTitle("Phone Book");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
