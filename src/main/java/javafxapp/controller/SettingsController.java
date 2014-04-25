package javafxapp.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafxapp.adapter.domain.Settings;
import javafxapp.db.DatabaseUtil;
import javafxapp.sheduler.IRequestTimer;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Pane implements Initializable{

    @FXML
    public TextField idDays;
    @FXML
    public TextField idHours;
    @FXML
    public TextField idMinutes;
    @FXML
    public TextField idSeconds;
    @FXML
    public CheckBox autoSend;
    @FXML
    public static TextField keyAlias;
    @FXML
    public static TextField password;
    @FXML
    public Button idSaveSettings;

    IRequestTimer IRequestTimer;

    @FXML
    public void saveSettings(ActionEvent event) throws Exception {
        /*if (autoSend.isSelected()) {
            List<Adapter> adapters = DatabaseUtil.findReqReadyToSend();

            for (Adapter adapter : adapters) {
                TimerCache.getInstance().addRequest(adapter);
            }
//            IRequestTimer.startRequest(Integer.parseInt(frequency), frequencyMeasure);

        } else {
            IRequestTimer.stopRequest();
            idDays.getStyleClass().add("disabled");
        }*/
        Settings settings = new Settings();
        settings.setCertAlias(keyAlias.getText());
        settings.setPassword(password.getText());
        DatabaseUtil.saveSettings(settings);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseUtil.createDB();
        Settings settings = DatabaseUtil.getSettings();
        keyAlias.setText(settings.getKeyAlias());
        password.setText(settings.getPassword());
    }
}
