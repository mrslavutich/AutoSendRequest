package javafxapp.service;


import javafxapp.utils.SoapUtils;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;


public class SendDataServiceImpl  {

    private static final String FAULT = "Ошибка при отправке";

    public String sendDataToSMEV(String request, String smevAddress) throws Exception {
        MimeHeaders mime = new MimeHeaders();
        SOAPMessage message = MessageFactory.newInstance().createMessage(mime, new ByteArrayInputStream(request.getBytes("UTF-8")));
        SOAPMessage message2 = MessageFactory.newInstance().createMessage();
        message2.getSOAPPart().setContent(message.getSOAPPart().getContent());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message2.writeTo(out);
        MimeHeaders headers = message2.getMimeHeaders();
        String action = "http://smev.gosuslugi.ru/createRequest";
        headers.addHeader("SOAPAction", action);

        return sendToSMEV(message2, smevAddress);
    }

    public String sendToSMEV(SOAPMessage message2, String smevAddress) throws Exception {
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
            return FAULT;
        } finally {
            soapConnection.close();
        }

    }


}
