package javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafxapp.adapter.Register;
import javafxapp.db.DatabaseUtil;

import java.util.HashMap;


/**
 * User: vmaksimov
 */
public class SmevController extends Accordion {

    @FXML
    public static TextField senderCodeFNS;
    @FXML
    public static TextField senderNameFNS;
    @FXML
    public static TextField recipientCodeFNS;
    @FXML
    public static TextField recipientNameFNS;
    @FXML
    public static TextField originatorCodeFNS;
    @FXML
    public static TextField originatorNameFNS;

    @FXML
    public static TextField senderCodePFR;
    @FXML
    public static TextField senderNamePFR;
    @FXML
    public static TextField recipientCodePFR;
    @FXML
    public static TextField recipientNamePFR;
    @FXML
    public static TextField originatorCodePFR;
    @FXML
    public static TextField originatorNamePFR;

    @FXML
    public static TextField senderCodeMVD;
    @FXML
    public static TextField senderNameMVD;
    @FXML
    public static TextField recipientCodeMVD;
    @FXML
    public static TextField recipientNameMVD;
    @FXML
    public static TextField originatorCodeMVD;
    @FXML
    public static TextField originatorNameMVD;

    @FXML
    public void handleSaveSmevFileds(ActionEvent event) throws Exception {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("senderCodeFNS", senderCodeFNS.getText());
        hashMap.put("senderNameFNS", senderNameFNS.getText());
        hashMap.put("senderCodeMVD", senderCodeMVD.getText());
        hashMap.put("senderNameMVD", senderNameMVD.getText());

        DatabaseUtil.saveSmevFields(hashMap);

    }

}
