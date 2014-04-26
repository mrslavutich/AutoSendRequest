package javafxapp.controller;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * User: vmaksimov
 */
public class ErrorController {

    public static void showDialog(String message){
        invokeStage(message, "Ошибка");
    }

    public static void showDialog(String message, String title){
        invokeStage(message, "Ошибка: " + title);
    }

    public static void showDialogWithException(String message){
        showDialog(message);
        throw new RuntimeException();
    }

    public static void showDialogWithException(String message, String title){
        showDialog(message, title);
        throw new RuntimeException();
    }
    private static void invokeStage(String message, String title) {
        Label label = new Label(message);
        label.setWrapText(true);
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(label);
        Scene secondScene = new Scene(secondaryLayout, 500, 100);
        Stage secondStage = new Stage();
        secondStage.setScene(secondScene);
        secondStage.setTitle(title);
        secondStage.show();
    }
}
