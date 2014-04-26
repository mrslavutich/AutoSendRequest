package javafxapp.crypto;

import javafxapp.controller.ErrorController;
import javafxapp.controller.SettingsController;
import org.w3c.dom.Document;
import ru.atc.smev.crypto.XmlSignatureTool;
import ru.gosuslugi.smev.signaturetool.xsd.Part4SignType;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

public class WSSTool {
    public static final String SMEV_ACTOR = "http://smev.gosuslugi.ru/actors/smev";

    public static String signSoapRequest(String request) {

        XmlSignatureTool tool = new XmlSignatureTool();

        Part4SignType part4Sign = new Part4SignType();
        part4Sign.setName("Body");
        part4Sign.setNamespace("http://schemas.xmlsoap.org/soap/envelope/");
        List<Part4SignType> parts = Collections.singletonList(part4Sign);

        String signedRequest = "";
        final Document doc = TransformDoc.readXml(request);
        try {
            final Document signDoc = tool.signMessage(doc, parts, SMEV_ACTOR, SettingsController.certAlias.getText(), SettingsController.keyAlias.getText(), SettingsController.password.getText());

            signedRequest = TransformDoc.writeXml(signDoc);

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            ErrorController.showDialog("Невозможно подписать запрос: " + e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return signedRequest;
    }
    public static PrivateKey loadKey(String keyName, char[] password)
            throws Exception {
        final KeyStore hdImageStore = loadStore();
        return (PrivateKey) hdImageStore.getKey(keyName, password);
    }

    public static Certificate loadCertificate(String certName)
             throws Exception {
        final KeyStore hdImageStore = loadStore();
         return hdImageStore.getCertificate(certName);
     }

    private static KeyStore loadStore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        final KeyStore hdImageStore = KeyStore.getInstance("HDImageStore");
        hdImageStore.load(null, null);
        return hdImageStore;
    }


}
