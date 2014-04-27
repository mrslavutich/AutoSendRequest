package javafxapp.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.domain.Settings;
import javafxapp.crypto.WSSTool;
import javafxapp.db.DatabaseUtil;
import javafxapp.elements.NumberTextField;
import javafxapp.elements.TimeTextField;
import javafxapp.handleFault.FaultsUtils;
import javafxapp.sheduler.RequestTimer;
import javafxapp.sheduler.TimerCache;
import javafxapp.utils.ReadExcelFile;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    public static TextField certAlias;
    @FXML
    public static TextField password;
    @FXML
    public Button idSaveSettings;
    @FXML
    private TimeTextField startTime;
    @FXML
    private TimeTextField endTime;
    @FXML
    private CheckBox timeWork;
    @FXML
    private NumberTextField countReqField;
    @FXML
    private CheckBox checkBoxCountReq;

    RequestTimer requestTimer;

    List<Adapter> adapters;

    @FXML
    public void saveSettings(ActionEvent event) throws ParseException {
        Date startWork = null, endWork = null;
        int countReq = Integer.MAX_VALUE;
        if (timeWork.isSelected()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            startWork = sdf.parse(startTime.getText());
            endWork = sdf.parse(endTime.getText());
        }

        if (checkBoxCountReq.isSelected()) {
            if (!countReqField.getText().isEmpty())
                countReq = Integer.parseInt(countReqField.getText());
        }

        requestTimer = new RequestTimer();
        if (autoSend.isSelected()) {

            adapters = DatabaseUtil.findReqReadyToSend(countReq);
            for (Adapter adapter : adapters) {
                TimerCache.getInstance().addRequest(adapter);
            }
            requestTimer.startRequest(idDays.getText(), idHours.getText(), idMinutes.getText(), idSeconds.getText(),
                                        startWork, endWork);
        } else {
            if (adapters != null && adapters.size() > 0) {
                List<Adapter> adapterList = DatabaseUtil.getResponseStatus(adapters);
                ReadExcelFile.writeFNSStatus(adapterList, MainController.filePath.getText());
            }
            requestTimer.stopRequest();
        }
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
