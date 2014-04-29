package javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafxapp.adapter.Register;
import javafxapp.adapter.domain.AdapterDetails;
import javafxapp.db.DatabaseUtil;
import javafxapp.handleFault.FaultsUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * User: vmaksimov
 */
public class SmevController extends Accordion implements Initializable{

    @FXML
    public static TextField senderCodeFNS;
    @FXML
    public static TextField senderNameFNS;

    @FXML
    public static TextField senderCodeMVD;
    @FXML
    public static TextField senderNameMVD;

    @FXML
    public TextField addressFNS;
    @FXML
    public TextField addressMVD;

    @FXML
    public void handleSaveSmevFileds(ActionEvent event) throws Exception {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("senderCodeFNS", senderCodeFNS.getText());
        hashMap.put("senderNameFNS", senderNameFNS.getText());
        hashMap.put("senderCodeMVD", senderCodeMVD.getText());
        hashMap.put("senderNameMVD", senderNameMVD.getText());

        if (checkAccessService(addressFNS.getText(), Register.foiv.FNS.getValue())){
            DatabaseUtil.saveAddressService(addressFNS.getText(), "07");
        }
        if (checkAccessService(addressMVD.getText(), Register.foiv.MVD.getValue())){
            DatabaseUtil.saveAddressService(addressMVD.getText(), "410");
        }

        DatabaseUtil.saveSmevFields(hashMap);

    }

    public static boolean checkAccessService(String address, String foiv) throws MalformedURLException {
        URL url = new URL(address);
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.getInputStream();
            return true;
        }catch (Exception e){
            ErrorController.showDialogWithException(FaultsUtils.modifyMessage(e.getMessage()), foiv);
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseUtil.createDB();
        HashMap<String, String> smevFileds = DatabaseUtil.getSmevFields();
        for(Map.Entry<String, String> entry : smevFileds.entrySet()) {
            if (entry.getKey().equals(senderCodeFNS.getId())) senderCodeFNS.setText(entry.getValue());
            if (entry.getKey().equals(senderNameFNS.getId())) senderNameFNS.setText(entry.getValue());
            if (entry.getKey().equals(senderCodeMVD.getId())) senderCodeMVD.setText(entry.getValue());
            if (entry.getKey().equals(senderNameMVD.getId())) senderNameMVD.setText(entry.getValue());
        }
        List<AdapterDetails> adapterDetailsList = DatabaseUtil.getAdapterDetails();
        for (AdapterDetails adapterDetails: adapterDetailsList){
            if (adapterDetails.getFoiv().equals(Register.foiv.FNS.getValue())) addressFNS.setText(adapterDetails.getSmevAddress());
            if (adapterDetails.getFoiv().equals(Register.foiv.MVD.getValue())) addressMVD.setText(adapterDetails.getSmevAddress());
        }
    }
}
