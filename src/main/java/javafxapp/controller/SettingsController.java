package javafxapp.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafxapp.sheduler.IRequestTimer;
import javafxapp.sheduler.TimerCache;

public class SettingsController {

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
    public void saveSettingsAutoSendByPeriodOfTime(ActionEvent event) throws Exception {
        if (autoSend.isSelected()) {
            List<RequestXML> requestXMLList = requestService.findByStatusOrStatus("В обработке", "Ошибка");
            for (RequestXML requestXML : requestXMLList) {
                TimerCache.getInstance().addRequest(requestXML);
            }
            IRequestTimer.startRequest(Integer.parseInt(frequency), frequencyMeasure);

        } else {
            IRequestTimer.stopRequest();
            idDays.getStyleClass().add("disabled");
        }
    }
}
