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
import javafxapp.adapter.domain.AdapterDetails;
import javafxapp.adapter.domain.Settings;
import javafxapp.adapter.fns.Pojo;
import javafxapp.db.DatabaseUtil;
import javafxapp.handleFault.FaultsUtils;
import javafxapp.service.SendDataService;
import javafxapp.utils.AdapterCells;
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
    public static Label countPFRRequests;
    @FXML
    public static Label countFNSRequests;
    @FXML
    public static Label countMVDRequests;

    @FXML
    public static Label countPFRSentRequests;
    @FXML
    public static Label countFNSSentRequests;
    @FXML
    public static Label countMVDSentRequests;

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
                sendReq("07", addressFNS.getText(), Register.FNS.foiv, AdapterCells.Fns.status);
                sendReq("07_2", addressFNS.getText(), Register.FNS.foiv, AdapterCells.Fns.status);
                DatabaseUtil.saveAddressService(addressFNS.getText(), "07");
                DatabaseUtil.saveAddressService(addressFNS.getText(), "07_2");
            }
        }
        if (checkboxMVD.isSelected()) {
            if (addressMVD.getText() == null || addressMVD.getText().isEmpty()){
                ErrorController.showDialog("Укажите адрес сервиса МВД");
            }else {
                checkAccessService(addressMVD.getText(), Register.MVD.foiv);
                sendReq("410", addressMVD.getText(), Register.MVD.foiv, AdapterCells.Mvd.status);
                DatabaseUtil.saveAddressService(addressMVD.getText(), "410");
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

    public static void counter(final String foiv) throws MalformedURLException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (foiv.equals(Register.FNS.foiv)) {
                    countFNSRequests.setText(String.valueOf((Integer.parseInt(countFNSRequests.getText()) - 1)));
                    countFNSSentRequests.setText(String.valueOf((Integer.parseInt(countFNSSentRequests.getText()) + 1)));
                }else if (foiv.equals(Register.MVD.foiv)) {
                    countMVDRequests.setText(String.valueOf((Integer.parseInt(countMVDRequests.getText()) - 1)));
                    countMVDSentRequests.setText(String.valueOf((Integer.parseInt(countMVDSentRequests.getText()) + 1)));
                }else if (foiv.equals(Register.PFR.foiv)) {
                    countPFRRequests.setText(String.valueOf((Integer.parseInt(countPFRRequests.getText()) - 1)));
                    countPFRSentRequests.setText(String.valueOf((Integer.parseInt(countPFRSentRequests.getText()) + 1)));
                }
            }
        });
    }

    private void sendReq(String id210fz, String address, String sheetName, int positionStatus) throws Exception {
        List<Adapter> adapters = DatabaseUtil.getRequest(id210fz);
        for(Adapter adapter: adapters) {
            String responseXml = SendDataService.sendDataToSMEV(adapter.getRequestXml(), address);
            String respStatus = XMLParser.getResponseStatus(responseXml);
            if (respStatus.equals("ACCEPT")){
                counter(sheetName);
            }
            adapter.setResponseXml(responseXml);
            adapter.setResponseStatus(respStatus);
            adapter.setId210fz(id210fz);
            DatabaseUtil.saveResponse(adapter);
        }
        ReadExcelFile.writeFNSStatus(adapters, filePath.getText(), sheetName, positionStatus);
    }



    @FXML
    public void handleSubmitLoadData(ActionEvent event){
        if (filePath != null && !filePath.getText().isEmpty()) {
            List<Pojo> fnsList = null;
            List<javafxapp.adapter.mvd.Pojo> mvdList = null;
            try {
                fnsList = ReadExcelFile.readFNSData(filePath.getText());
                mvdList = ReadExcelFile.readMVDData(filePath.getText());
            } catch (IOException e) {
                ErrorController.showDialog("Невозможно прочитать файл");
            }
            List<Adapter> adapterFns = null, adapterMvd = null;
            List<Adapter> allAdapters = new ArrayList<>();
            if (fnsList != null) {
                adapterFns = BuilderRequest.buildRequestByTemplateFns(fnsList, "fns");

                countFNSRequests.setText(String.valueOf(adapterFns.size()));
                countFNSSentRequests.setText("0");
            }
            if (mvdList != null) {
                adapterMvd = BuilderRequest.buildRequestByTemplateMvd(mvdList, "mvd");

                countMVDRequests.setText(String.valueOf(adapterMvd.size()));
                countMVDSentRequests.setText("0");
            }
            if (adapterFns != null)  allAdapters.addAll(adapterFns);
            if (adapterMvd != null)  allAdapters.addAll(adapterMvd);
            if (allAdapters.size() > 0) DatabaseUtil.insertRequests(allAdapters);

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
        HashMap<String, String> smevFileds = DatabaseUtil.getSmevFields();
        for(Map.Entry<String, String> entry : smevFileds.entrySet()) {
            if (entry.getKey().equals(SmevController.senderCodeFNS.getId())) SmevController.senderCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.senderNameFNS.getId())) SmevController.senderNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.senderCodeMVD.getId())) SmevController.senderCodeMVD.setText(entry.getValue());
            if (entry.getKey().equals(SmevController.senderNameMVD.getId())) SmevController.senderNameMVD.setText(entry.getValue());
        }
        Settings settings = DatabaseUtil.getSettings();

        if (settings != null && settings.getPathFile() != null && !settings.getPathFile().equals("")) {
            filePath.setText(settings.getPathFile());
        }
        /*else idLoadData.setDisable(true);*/

        List<AdapterDetails> adapterDetailsList = DatabaseUtil.getAdapterDetails();
        for (AdapterDetails adapterDetails: adapterDetailsList){
             if (adapterDetails.getFoiv().equals(Register.FNS.foiv)) addressFNS.setText(adapterDetails.getSmevAddress());
            if (adapterDetails.getFoiv().equals(Register.MVD.foiv)) addressMVD.setText(adapterDetails.getSmevAddress());
        }


    }
}
