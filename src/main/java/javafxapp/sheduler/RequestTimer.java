package javafxapp.sheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RequestTimer extends Thread implements IRequestTimer {

    public int time = 10;
    public String measure = "days";
    private static volatile Boolean started;

    @Override
    public void run() {
        started = true;

        while (true) {
            try {
                synchronized (started) {
                    while (!started) {
                        this.sleep(10000);
                    }
                }
                System.out.println("++++++++++++++++++");
                if (measure.equals("min")) time = time * 60;
                if (measure.equals("hours")) time = time * 60 * 60;
                if (measure.equals("days")) time = time * 60 * 60 * 24;
                sendRequests();
                this.sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Could not parse time");
                started = false;
            }

        }
    }


    @Override
    public synchronized void startRequest(int time, String measure) {
        this.time = time;
        this.measure = measure;
        this.started = true;
        if (this.getState().equals(Thread.State.NEW)) {
            this.start();
        }

    }

    @Override
    public synchronized void stopRequest() {
        this.started = false;
    }


    @Override
    public void sendRequests() {
        HashMap<String, TimerRequests> requests = new HashMap<String, TimerRequests>();
        requests.putAll(TimerCache.getInstance().requestsList());
        if (requests != null) {
            Set<String> keys = requests.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    if (requests.get(key).getRequestXml() != null) {
                       /* String result = sendDataService.sendStatusByTimer(requests.get(key));
                        String status = loadAdapterDefinition.requestStatusByXml(result);
                        if (status.equals("Получен ответ")) {
                            RequestXML requestXML = requestService.findXML(key);
                            requestXML.setResponse(result);
                            requestXML.setResponseJSON(sendDataService.simpleJsonForPdf(requestXML));
                            requestXML.setStatus(status);
                            requestXML.setData(DateUtil.YYYY_MM_DD_HH_MM_SS_S.format(new java.util.Date()));
                            requestService.save(requestXML);
                            TimerCache.getInstance().deleteRequest(key);
                        }*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
