package javafxapp.crypto;

import javafxapp.controller.SettingsController;
import org.w3c.dom.Document;
import ru.atc.smev.crypto.XmlSignatureTool;
import ru.gosuslugi.smev.signaturetool.xsd.Part4SignType;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

public class WSSTool {
    public static final String SMEV_ACTOR = "http://smev.gosuslugi.ru/actors/smev";

    public static String PKEY_ALIAS = "RaUser-2908cdc2-4aff-47c6-9636-d2a98ba3d2b5";
    public static String CERT_ALIAS = "RaUser-2908cdc2-4aff-47c6-9636-d2a98ba3d2b5";
    public static String PKEY_PASSWORD = "1234567890";


    public static String signSoapRequest(String request) {

        XmlSignatureTool tool = new XmlSignatureTool();

        Part4SignType part4Sign = new Part4SignType();
        part4Sign.setName("Body");
        part4Sign.setNamespace("http://schemas.xmlsoap.org/soap/envelope/");
        List<Part4SignType> parts = Collections.singletonList(part4Sign);

        String signedRequest = "";
        final Document doc = TransformDoc.readXml(request);
        try {
            final Document signDoc = tool.signMessage(doc, parts, SMEV_ACTOR, SettingsController.keyAlias.getText(), SettingsController.keyAlias.getText(), SettingsController.password.getText());

            signedRequest = TransformDoc.writeXml(signDoc);

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return signedRequest;
    }
}
