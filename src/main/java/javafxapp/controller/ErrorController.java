package javafxapp.controller;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * User: vmaksimov
 */
public class ErrorController {

    public static void showDialog(String message) throws Exception{

        Label label = new Label(message);
        label.setWrapText(true);
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(label);
        Scene secondScene = new Scene(secondaryLayout, 500, 100);
        Stage secondStage = new Stage();
        secondStage.setTitle("Ошибка");
        secondStage.setScene(secondScene);
        secondStage.show();
    }
}
