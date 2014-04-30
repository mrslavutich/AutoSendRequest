package javafxapp.crypto;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class TransformDoc {


    public static String writeXml(Document doc) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String signedRequest = "";
        try {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(os);

            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            signedRequest = IOUtils.toString(os.toByteArray(), "UTF-8");
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
        return signedRequest;
    }



    public static Document readXml(String request) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setIgnoringComments(true);
            dbf.setValidating(false);

            DocumentBuilder dBuilder = dbf.newDocumentBuilder();

            System.out.println(request);
            doc = dBuilder.parse(IOUtils.toInputStream(request,  "UTF-8"));
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
