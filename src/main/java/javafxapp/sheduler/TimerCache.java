package javafxapp.sheduler;

import web.domain.RequestXML;

import java.util.HashMap;

public class TimerCache {

    private static TimerCache timerCache;

    private HashMap<String, TimerRequests> requestsToSend;

    private TimerCache(){
        requestsToSend = new HashMap<String, TimerRequests>();
    }

    public static TimerCache getInstance() {
        if (timerCache == null) timerCache = new TimerCache();
        return timerCache;
    }

    public synchronized void addRequest(RequestXML requestXML){
        TimerRequests timerRequests = new TimerRequests();
        timerRequests.setRequestXml(requestXML.getStatusRequest());
        timerRequests.setSmevAddress(requestXML.getAdapter().getSmevaddress());
        timerRequests.setAdapterId(requestXML.getAdapter().getId210fz());
        requestsToSend.put(String.valueOf(requestXML.getId()), timerRequests);

    }

    public synchronized void deleteRequest(String idRequest){
        requestsToSend.remove(idRequest);
    }

    public HashMap<String, TimerRequests> requestsList(){
        return requestsToSend;
    }


}
