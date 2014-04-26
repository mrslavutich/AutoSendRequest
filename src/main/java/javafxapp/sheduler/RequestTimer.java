package javafxapp.sheduler;

import javafxapp.adapter.domain.Adapter;
import javafxapp.db.DatabaseUtil;
import javafxapp.service.SendDataService;
import javafxapp.utils.XMLParser;

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
        if (requests.size() > 0) {
            Set<String> keys = requests.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    TimerRequests timerRequests = requests.get(key);
                    if (timerRequests.getRequestXml() != null) {
                        String responseXml = SendDataService.sendDataToSMEV(timerRequests.getRequestXml(), timerRequests.getSmevAddress());
                        String respStatus = XMLParser.getResponseStatus(responseXml);
                        if (respStatus.equals("ACCEPT")) {
                            Adapter adapter = new Adapter();
                            adapter.setId(Integer.parseInt(key));
                            adapter.setResponseXml(responseXml);
                            adapter.setResponseStatus(respStatus);
                            DatabaseUtil.saveResponse(adapter);
                            TimerCache.getInstance().deleteRequest(key);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
