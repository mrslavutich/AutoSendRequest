package javafxapp.utils;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.helpers.XMLUtils;

import javax.xml.soap.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: a.petkevich
 * Date: 18.12.13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class SoapUtils {
    private static final String ENCODING_TYPE = "UTF-8";

    public static void addAttachmetToSoapMesage(SOAPMessage soapResponse, AttachmentPart attachment, SOAPFactory soapFactory) throws SOAPException, IOException {
        SOAPBody body = soapResponse.getSOAPBody();
        Name appDocName = soapFactory.createName("AppDocument", "", "");
        SOAPBodyElement bodyElement = body.addBodyElement(appDocName);
        Name binaryName = soapFactory.createName("BinaryData");
        SOAPElement element = bodyElement.addChildElement(binaryName);
        element.addTextNode(contentToString(attachment.getBase64Content()));

    }

    public static String soapMessageToString(SOAPEnvelope soapEnvelope) throws IOException {
        String envelope = XMLUtils.toString(soapEnvelope);
        String strMsg = IOUtils.toString(envelope.getBytes(), ENCODING_TYPE);
        return strMsg;
    }

    public static String contentToString(InputStream inputStream) throws IOException {
       return IOUtils.toString(inputStream).replaceAll("(\r\n|\n)", "");

    }
}
