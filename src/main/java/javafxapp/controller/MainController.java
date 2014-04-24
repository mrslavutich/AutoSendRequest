package javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafxapp.Main;
import javafxapp.adapter.Adapter;
import javafxapp.adapter.Register;
import javafxapp.adapter.fns.FNS;
import javafxapp.crypto.WSSTool;
import javafxapp.db.DatabaseUtil;
import javafxapp.handleFault.FaultsUtils;
import javafxapp.service.SendDataService;
import javafxapp.utils.ReadExcelFile;
import javafxapp.utils.XMLParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController extends VBox implements Initializable {

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

        boolean isFileOpen;
        try {
            new FileOutputStream(filePath.getText());
            isFileOpen = true;
        } catch (IOException e) {
            isFileOpen = false;
        }
        if (isFileOpen) {
            if (checkboxFNS.isSelected()) {
                int countSentReqIp = sendFNSReq(Adapter.getRequestsIp(), Register.FNS.adapter);
                int countSentReqUl = sendFNSReq(Adapter.getRequestsUl(), Register.FNS.adapterUL);
                countFNSSentReq.setText(String.valueOf(countSentReqIp + countSentReqUl));
                /*countFNSSentReq.getStyleClass().add("fontBold");
                countFNSRequests.getStyleClass().add("fontNormal");*/
                countFNSRequests.setText("0");
            }
        }else{
            ErrorController.showDialog("Закройте файл: " + filePath.getText());
        }

    }

    private int sendFNSReq(List<String> adapter, String type) throws Exception {
        int i = 0;
        List<String> listStatus = new ArrayList<>();
        for (String requestXml : adapter) {
            i++;
            requestXml = WSSTool.signSoapRequest(requestXml);
            String responseXml = SendDataService.sendDataToSMEV(requestXml, addressFNS.getText());
            System.out.println(responseXml);
            listStatus.add(getResponseStatus(responseXml));
        }
        ReadExcelFile.writeFNSStatus(listStatus, type, filePath.getText());
        return i;
    }

    private String getResponseStatus(String responseXml) throws Exception {
        String responseStatus = FaultsUtils.findFaultsInResponse(responseXml);
        if (responseStatus == null){
            responseStatus = XMLParser.getStatusElement(responseXml);
        }
        if (responseStatus == null){
            responseStatus = "Ошибка";
        }
        return responseStatus;
    }

    @FXML
    public void handleSubmitLoadData(ActionEvent event) throws Exception {
        HashMap<String, List<FNS>> mapFns = null;
        try {
            mapFns = ReadExcelFile.readFNSData(filePath.getText());
        }catch (IOException e){
            ErrorController.showDialog("Невозможно прочитать файл");
        }
        if (mapFns != null) {
            List<String> requestsIp = BuilderRequest.buildRequestByTemplate(mapFns.get(Register.FNS.adapter));
            List<String> requestsUl = BuilderRequest.buildRequestByTemplate(mapFns.get(Register.FNS.adapterUL));
        /*DatabaseUtil.insertRequests(Register.FNS.foiv, requests);*/

            Adapter.setFoiv(Register.FNS.foiv);
            Adapter.setRequestsIp(requestsIp);
            Adapter.setRequestsUl(requestsUl);
            int countFnsReq = requestsIp.size() + requestsUl.size();
            countFNSRequests.setText(String.valueOf(countFnsReq));
        /*countFNSRequests.getStyleClass().remove("fontNormal");
        countFNSRequests.getStyleClass().add("fontBold");
        countFNSSentReq.getStyleClass().remove("fontBold");
        countFNSSentReq.getStyleClass().add("fontNormal");*/
            countFNSSentReq.setText("0");
        }
    }

    @FXML
    public void handleFileChooser(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        if (filePath.getText() != null) {
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
            DatabaseUtil.savePathFile(file.getCanonicalPath());
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
            if (entry.getKey().equals(SmevController.senderCodeFNS.getId())) SmevController.senderCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.senderNameFNS.getId())) SmevController.senderNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.recipientCodeFNS.getId())) SmevController.recipientCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.recipientNameFNS.getId())) SmevController.recipientNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.originatorCodeFNS.getId())) SmevController.originatorCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.originatorNameFNS.getId())) SmevController.originatorNameFNS.setText(entry.getValue());
        }
        String pathFile = DatabaseUtil.getPathFile();
        filePath.setText(pathFile);

    }
}
