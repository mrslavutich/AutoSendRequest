package javafxapp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafxapp.Main;
import javafxapp.adapter.Register;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.domain.Settings;
import javafxapp.adapter.fns.Pojo;
import javafxapp.db.DatabaseUtil;
import javafxapp.elements.NumberTextField;
import javafxapp.elements.TimeTextField;
import javafxapp.sheduler.RequestTimer;
import javafxapp.sheduler.TimerCache;
import javafxapp.utils.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController extends VBox implements Initializable {



    @FXML
    public static Node sendRequests;
    @FXML
    public static TextField filePath;
    public static String newFilePath;
    @FXML
    public static Label countFNSRequests;
    @FXML
    public static Label countMVDRequests;

    @FXML
    public static Label countFNSSentRequests;
    @FXML
    public static Label countMVDSentRequests;

    @FXML
    public CheckBox checkboxMVD;
    @FXML
    public CheckBox checkboxFNS;

    @FXML
    public TextField idDays;
    @FXML
    public TextField idHours;
    @FXML
    public TextField idMinutes;
    @FXML
    public TextField idSeconds;

    @FXML
    public Node idLoadData;

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
    public static List<Adapter> adapters;
    private static final String textButtonOnSend = "Включить отправку запросов";
    private static final String textButtonOffSend = "Выключить отправку запросов";


    @FXML
    public void handleFileChooser(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        if (filePath != null && !filePath.getText().equals("")) {
            File dir = null;
            try {
                dir = new File(new File(filePath.getText()).getParent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dir != null && dir.exists()) fileChooser.setInitialDirectory(dir);
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS", "*.xls"));
        File file = fileChooser.showOpenDialog(Main.mainStage);
        try {
            if (file != null) {
                DatabaseUtil.savePathFile(file.getCanonicalPath());
                filePath.setText(file.getCanonicalPath());
                idLoadData.setDisable(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSubmitLoadData(ActionEvent event){
        if (filePath != null && !filePath.getText().isEmpty()) {
            HashMap<String, List<Pojo>> map = new HashMap<>();
            List<Pojo> fnsList = null;
            List<javafxapp.adapter.mvd.Pojo> mvdList = null;

            try {
                for (Register register: Register.values()){
                    if (!register.getId210fz().equals("410")) {
                        fnsList = ExcelReader.readFNSData(filePath.getText(), register.getNameAdapter());
                        map.put(register.getId210fz(), fnsList);
                    }
                }
                mvdList = ExcelReader.readMVDData(filePath.getText());

            } catch (IOException e) {
                ErrorController.showDialog("Невозможно прочитать файл");
            }
            List<Adapter> adapterFns = null, adapterMvd = null;
            List<Adapter> allAdapters = new ArrayList<>();
            if (fnsList != null) {
                adapterFns = BuilderRequest.buildRequestByTemplateFns(map);

                countFNSRequests.setText(String.valueOf(adapterFns.size()));
                countFNSSentRequests.setText("0");
            }
            if (mvdList != null) {
                adapterMvd = BuilderRequest.buildRequestByTemplateMvd(mvdList);

                countMVDRequests.setText(String.valueOf(adapterMvd.size()));
                countMVDSentRequests.setText("0");
            }
            if (adapterFns != null)  allAdapters.addAll(adapterFns);
            if (adapterMvd != null)  allAdapters.addAll(adapterMvd);
            if (allAdapters.size() > 0) {
                DatabaseUtil.insertRequests(allAdapters);
                sendRequests.setDisable(false);
            }

        }
    }

    @FXML
    public void handleSubmitSendRequests(ActionEvent event) throws Exception {

        if (((Button)sendRequests).getText().equals(textButtonOnSend)) {
            changeTextOffSendButton();

            ExcelReader.copyFile(new File(filePath.getText()));

            if (checkboxFNS.isSelected() || checkboxMVD.isSelected()) {

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


                adapters = DatabaseUtil.findReqReadyToSend(countReq, checkboxFNS.isSelected(), checkboxMVD.isSelected());
                if (adapters != null && adapters.size() > 0 ){
                    try {
                        for (Adapter adapter : adapters) {
                            TimerCache.getInstance().addRequest(adapter);
                        }

                        requestTimer.startRequest(idDays.getText(), idHours.getText(), idMinutes.getText(), idSeconds.getText(),
                                startWork, endWork);
                    } catch (Exception e) {
                        ErrorController.showDialogWithException("Закройте файл: " + newFilePath);
                    }
                }
            }
        } else {
            if (TimerCache.getInstance().requestsList().size() > 0)
                writeStatusInExcelFromDB();
            if (requestTimer != null) {
                requestTimer.stopRequest();
                TimerCache.getInstance().deleteRequests();
            }
            changeTextOnSendButton();
        }

    }

    private void changeTextOffSendButton() {
        ((Button)sendRequests).setText(textButtonOffSend);
    }

    public static void changeTextOnSendButton() {
        ((Button) sendRequests).setText(textButtonOnSend);
    }


    public synchronized static void writeStatusInExcelFromDB() {
        if (adapters != null && adapters.size() > 0) {
            List<Adapter> adapterList = DatabaseUtil.getResponseStatus(adapters);
            ExcelReader.writeStatus(adapterList, MainController.newFilePath);
            adapters = null;
        }
    }

    public static void counter(final String foiv) throws MalformedURLException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (foiv.equals(Register.foiv.FNS.getValue())) {
                    countFNSRequests.setText(String.valueOf((Integer.parseInt(countFNSRequests.getText()) - 1)));
                    countFNSSentRequests.setText(String.valueOf((Integer.parseInt(countFNSSentRequests.getText()) + 1)));
                }else if (foiv.equals(Register.foiv.MVD.getValue())) {
                    countMVDRequests.setText(String.valueOf((Integer.parseInt(countMVDRequests.getText()) - 1)));
                    countMVDSentRequests.setText(String.valueOf((Integer.parseInt(countMVDSentRequests.getText()) + 1)));
                }
            }
        });
    }


    @FXML
    public void checkAccessTimeWork(ActionEvent event) {
        if (timeWork.isSelected()) {
            startTime.setDisable(false);
            endTime.setDisable(false);
        }else{
            startTime.setDisable(true);
            endTime.setDisable(true);
        }
    }

    @FXML
    public void checkAccessCountReq(ActionEvent actionEvent) {
        if (checkBoxCountReq.isSelected()) {
            countReqField.setDisable(false);
        }else{
            countReqField.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Settings settings = DatabaseUtil.getSettings();

        if (settings != null && settings.getPathFile() != null && !settings.getPathFile().equals("")) {
            filePath.setText(settings.getPathFile());
        }
        if (filePath.getText().isEmpty()) idLoadData.setDisable(true);
        sendRequests.setDisable(true);
    }

}
