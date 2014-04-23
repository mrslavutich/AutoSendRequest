package javafxapp.handleFault;

import javafxapp.utils.XMLParser;

public final class FaultsUtils {


    public static String findFaultsInResponse(String xmlResponse) throws Exception {
        String fault;
        if (xmlResponse.equals(Fault.SEND_MESSAGE.message))
            return xmlResponse;

        fault = XMLParser.getFaultElement(xmlResponse);
        if (fault != null)
            return fault;

        return null;
    }
}
