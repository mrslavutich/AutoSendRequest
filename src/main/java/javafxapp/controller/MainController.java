package javafxapp.controller;

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
import javafxapp.adapter.domain.AdapterDetails;
import javafxapp.adapter.domain.Settings;
import javafxapp.adapter.fns.FNS;
import javafxapp.db.DatabaseUtil;
import javafxapp.handleFault.FaultsUtils;
import javafxapp.service.SendDataService;
import javafxapp.utils.ReadExcelFile;
import javafxapp.utils.XMLParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    public static TextField filePath;

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
    private Node idLoadData;

    @FXML
    public void handleSubmitSendRequests(ActionEvent event) throws Exception {

        try {
            new FileOutputStream(filePath.getText());
        } catch (IOException e) {
            ErrorController.showDialogWithException("Закройте файл: " + filePath.getText());
        }
        if (checkboxFNS.isSelected()) {
            if (addressFNS.getText() == null || addressFNS.getText().isEmpty()){
                ErrorController.showDialog("Укажите адрес сервиса ФНС");
            }else {
                checkAccessService(addressFNS.getText(), Register.FNS.foiv);
                int countSentReqIp = sendFNSReq("07");
                int countSentReqUl = sendFNSReq("07_2");
                countFNSSentReq.setText(String.valueOf(countSentReqIp + countSentReqUl));
                countFNSRequests.setText("0");
                DatabaseUtil.saveAddressService(addressFNS.getText(), "07");
            }
        }

    }

    private void checkAccessService(String address, String foiv) throws MalformedURLException {
        URL url = new URL(address);
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.getInputStream();
        }catch (Exception e){
            ErrorController.showDialogWithException(FaultsUtils.modifyMessage(e.getMessage()), foiv);
        }
    }

    private int sendFNSReq(String id210fz) throws Exception {
        int i = 0;
        List<Adapter> adapters = DatabaseUtil.getRequest(id210fz);
        for(Adapter adapter: adapters) {
            i++;
            String responseXml = SendDataService.sendDataToSMEV(adapter.getRequestXml(), addressFNS.getText());
            String respStatus = XMLParser.getResponseStatus(responseXml);
            adapter.setResponseXml(responseXml);
            adapter.setResponseStatus(respStatus);
            adapter.setId210fz(id210fz);
            DatabaseUtil.saveResponse(adapter);
        }
        ReadExcelFile.writeFNSStatus(adapters, filePath.getText());
        return i;
    }



    @FXML
    public void handleSubmitLoadData(ActionEvent event){
        if (filePath != null && !filePath.getText().isEmpty()) {
            List<FNS> fnsList = null;
            try {
                fnsList = ReadExcelFile.readFNSData(filePath.getText());
            } catch (IOException e) {
                ErrorController.showDialog("Невозможно прочитать файл");
            }
            if (fnsList != null) {
                List<Adapter> adapterFns = BuilderRequest.buildRequestByTemplate(fnsList);
                DatabaseUtil.insertRequests(adapterFns);

                countFNSRequests.setText(String.valueOf(adapterFns.size()));
                countFNSSentReq.setText("0");
            }
        }
    }

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
            }
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
        Settings settings = DatabaseUtil.getSettings();

        if (settings != null && settings.getPathFile() != null && !settings.getPathFile().equals("")) {
            filePath.setText(settings.getPathFile());
        }
        /*else idLoadData.setDisable(true);*/

        List<AdapterDetails> adapterDetailsList = DatabaseUtil.getAdapterDetails();
        for (AdapterDetails adapterDetails: adapterDetailsList){
            addressFNS.setText(adapterDetails.getSmevAddress());
            addressMVD.setText(adapterDetails.getSmevAddress());
            addressPFR.setText(adapterDetails.getSmevAddress());
        }


    }
}
