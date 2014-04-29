package javafxapp.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafxapp.adapter.domain.Settings;
import javafxapp.crypto.WSSTool;
import javafxapp.db.DatabaseUtil;
import javafxapp.handleFault.FaultsUtils;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class SettingsController extends Pane implements Initializable{


    @FXML
    public static TextField keyAlias;
    @FXML
    public static TextField certAlias;
    @FXML
    public static TextField password;
    @FXML
    public Button idSaveSettings;




    @FXML
    public void saveSettings(ActionEvent event) throws ParseException {
        try {
            if (!certAlias.getText().isEmpty() && !keyAlias.getText().isEmpty() && !password.getText().isEmpty()) {
                if (WSSTool.loadCertificate(certAlias.getText()) != null ) {
                    if (WSSTool.loadKey(keyAlias.getText(), password.getText().toCharArray()) != null) {
                        DatabaseUtil.saveSettings();
                    }else ErrorController.showDialog(FaultsUtils.modifyMessage("key not found"));
                }else ErrorController.showDialog(FaultsUtils.modifyMessage("certificate not found"));
            }else ErrorController.showDialog("Заполните все поля по данным для сертификата");
        } catch (Exception e) {
            ErrorController.showDialog(FaultsUtils.modifyMessage(e.getMessage()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseUtil.createDB();
        Settings settings = DatabaseUtil.getSettings();
        certAlias.setText(settings.getCertAlias());
        keyAlias.setText(settings.getKeyAlias());
        password.setText(settings.getPassword());
    }
}
