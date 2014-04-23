package javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafxapp.adapter.Register;
import javafxapp.db.DatabaseUtil;

import java.util.HashMap;


/**
 * User: vmaksimov
 */
public class SmevController {

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
    public void handleSaveSmevFileds(ActionEvent event) throws Exception {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("senderCodeFNS", senderCodeFNS.getText());
        hashMap.put("senderNameFNS", senderNameFNS.getText());
        hashMap.put("recipientCodeFNS", recipientCodeFNS.getText());
        hashMap.put("recipientNameFNS", recipientNameFNS.getText());
        hashMap.put("originatorCodeFNS", originatorCodeFNS.getText());
        hashMap.put("originatorNameFNS", originatorNameFNS.getText());

        DatabaseUtil.saveSmevFields(Register.FNS.foiv, hashMap);

    }
}
