package javafxapp.service;


import javafxapp.handleFault.Fault;
import javafxapp.utils.SoapUtils;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;


public class SendDataService {

    public static String sendDataToSMEV(String request, String adapterId2101fz, String smevAddress) throws Exception {
        MimeHeaders mime = new MimeHeaders();
        SOAPMessage message = MessageFactory.newInstance().createMessage(mime, new ByteArrayInputStream(request.getBytes("UTF-8")));
        SOAPMessage message2 = MessageFactory.newInstance().createMessage();
        message2.getSOAPPart().setContent(message.getSOAPPart().getContent());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message2.writeTo(out);
        MimeHeaders headers = message2.getMimeHeaders();
        String action = soapAction(adapterId2101fz);
        headers.addHeader("SOAPAction", action);

        return sendToSMEV(message2, smevAddress);
    }

    public static String soapAction(String adapterId2101fz) throws IllegalAccessException {
        String result = "";
        for (Field field : SOAPAction.class.getFields()) {
            if (field.getName().equals("action" + "_" + adapterId2101fz)) {
                result = (String) field.get(SOAPAction.class);
            }
        }
        return result;
    }

    public static String sendToSMEV(SOAPMessage message2, String smevAddress) throws Exception {
        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = factory.createConnection();
        SOAPMessage soapResponse;
        SOAPEnvelope soapEnvelope;
        try {
            soapResponse = soapConnection.call(message2, smevAddress);
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            Iterator iterator = soapResponse.getAttachments();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    AttachmentPart attachment = (AttachmentPart) iterator.next();
                    SoapUtils.addAttachmetToSoapMesage(soapResponse, attachment, soapFactory);
                }
            }
            soapEnvelope = soapResponse.getSOAPPart().getEnvelope();
            return SoapUtils.soapMessageToString(soapEnvelope);
        } catch (SOAPException e) {
            return Fault.SEND_MESSAGE.message;
        } finally {
            soapConnection.close();
        }

    }


}
