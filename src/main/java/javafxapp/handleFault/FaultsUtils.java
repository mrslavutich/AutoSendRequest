package javafxapp.handleFault;

import javafxapp.utils.XMLParser;

public final class FaultsUtils {


    public static String findFaultsInResponseXML(String xmlResponse) throws Exception {
        String fault;
        if (xmlResponse.equals(Fault.SEND_MESSAGE.message))
            return xmlResponse;

        fault = XMLParser.getFaultElement(xmlResponse);
        if (fault != null)
            return fault;

        return null;
    }

    public static String modifyMessage(String keyError) {
           Fault fault = Fault.forValue(keyError);
           if (fault != null) {
               return fault.modifiedMessage;
           } else {
               return keyError;
           }
       }
}
