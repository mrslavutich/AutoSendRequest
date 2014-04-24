package javafxapp.utils;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLParser {

    public static final String rev111111 = "http://smev.gosuslugi.ru/rev111111";
    public static final String rev120315 = "http://smev.gosuslugi.ru/rev120315";
    private static final String soap_envelope = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String FAULTSTRING = "faultstring";
    private static final String STATUS = "Status";


    public static String getStatusElement(String xml) throws Exception {
        Document doc = parseDocFromByte(xml.getBytes());
        Element elem = getDocSmevElement(doc, STATUS);
        if (elem != null) {
            return elem.getTextContent();
        }
        return null;
    }

    public static String getFaultElement(String xml) throws Exception {
        Document doc = parseDocFromByte(xml.getBytes());
        Element elem = getDocElement(doc, FAULTSTRING);
        if (elem != null) {
            return elem.getTextContent();
        }
        return null;
    }

    private static Document parseDocFromByte(byte[] b) {
            try {
                final DocumentBuilder loader = createDocumentBuilderFactory().newDocumentBuilder();
                String xmlContent = IOUtils.toString(b, "UTF-8");

                return loader.parse(IOUtils.toInputStream(xmlContent, "UTF-8"));
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }

    private static DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);

        return factory;
    }

    private static Element getDocSmevElement(Document doc, String elName) {
         Element el = null;
         NodeList nodeList_2_5 = doc.getElementsByTagNameNS(rev120315, elName);
         NodeList nodeList_2_4 = doc.getElementsByTagNameNS(rev111111, elName);
         if (nodeList_2_5 != null)
             el = (Element) nodeList_2_5.item(0);
         if (nodeList_2_4 != null && el == null)
             el = (Element) nodeList_2_4.item(0);
         return el;
     }

    private static Element getDocElementByNS(Document doc, String elName, String ns) {
         Element el = null;
         NodeList nodeList = doc.getElementsByTagNameNS(ns, elName);
         if (nodeList != null)
             el = (Element) nodeList.item(0);
         return el;
     }

    private static Element getDocElement(Document doc, String elName) {
        Element el = null;
        NodeList nodeList = doc.getElementsByTagName(elName);
        if (nodeList != null)
            el = (Element) nodeList.item(0);
        return el;
    }
}
