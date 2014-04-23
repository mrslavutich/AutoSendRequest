package javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafxapp.Main;
import javafxapp.adapter.Register;
import javafxapp.adapter.Adapters;
import javafxapp.adapter.fns.FNS;
import javafxapp.crypto.WSSTool;
import javafxapp.db.DatabaseUtil;
import javafxapp.service.SendDataServiceImpl;
import javafxapp.utils.ReadExcelFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController extends SmevController implements Initializable {

    @FXML
    public TextField addressPFR;
    @FXML
    public TextField addressFNS;
    @FXML
    public TextField addressMVD;

    @FXML
    public Button sendRequests;
    @FXML
    public TextField filePath;

    @FXML
    public Label countPFRRequests;
    @FXML
    public Label countFNSRequests;
    @FXML
    public Label countMVDRequests;

    @FXML
    public Label countPFRSentReq;
    @FXML
    public Label countFNSSentReq;
    @FXML
    public Label countMVDSentReq;

    @FXML
    public CheckBox checkboxPFR;
    @FXML
    public CheckBox checkboxMVD;
    @FXML
    public CheckBox checkboxFNS;

    @FXML
    public void handleSubmitSendRequests(ActionEvent event) throws Exception {

        if (checkboxFNS.isSelected()) {
            int i = 0;
            for (String strRequest : Adapters.getRequests()) {
                strRequest = WSSTool.signSoapRequest(strRequest);
                SendDataServiceImpl sendDataService = new SendDataServiceImpl();
                sendDataService.sendDataToSMEV(strRequest, addressFNS.getText());
                ++i;
            }
            countFNSSentReq.setText(String.valueOf(i));
            countFNSSentReq.getStyleClass().add("fontBold");
            countFNSRequests.getStyleClass().add("fontNormal");
            countFNSRequests.setText("0");
        }

    }

    @FXML
    public void handleSubmitLoadData(ActionEvent event) throws Exception {
        List<FNS> fnsList = new ArrayList<>();
        ReadExcelFile.read(filePath.getText(), fnsList);
        List<String> requests = BuilderRequest.buildRequestByTemplate(fnsList);
        DatabaseUtil.insertRequests(Register.FNS.foiv, requests);

        Adapters.setNameAdapter(Register.FNS.foiv);
        Adapters.setRequests(requests);
        countFNSRequests.setText(String.valueOf(requests.size()));
        countFNSRequests.getStyleClass().add("fontBold");
    }

    @FXML
    public void handleFileChooser(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        File dir = null;
        try {
            dir = new File(new File(filePath.getText()).getParent());
        }catch (Exception e) {
            e.printStackTrace();
        }

        if (filePath.getText() != null && dir != null) fileChooser.setInitialDirectory(dir);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS", "*.xls"));
        File file = fileChooser.showOpenDialog(Main.mainStage);
        try {
            filePath.setText(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseUtil.createDB();
        HashMap<String, String> smevFileds = DatabaseUtil.getSmevFields(Register.FNS.foiv);
        for(Map.Entry<String, String> entry : smevFileds.entrySet()) {
            if (entry.getKey().equals(senderCodeFNS.getId())) senderCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(senderNameFNS.getId())) senderNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(recipientCodeFNS.getId())) recipientCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(recipientNameFNS.getId())) recipientNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(originatorCodeFNS.getId())) originatorCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(originatorNameFNS.getId())) originatorNameFNS.setText(entry.getValue());
        }

    }
}
