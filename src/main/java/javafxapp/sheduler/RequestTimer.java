package javafxapp.sheduler;

import javafxapp.adapter.domain.Adapter;
import javafxapp.controller.MainController;
import javafxapp.db.DatabaseUtil;
import javafxapp.service.SendDataService;
import javafxapp.utils.XMLParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RequestTimer extends Thread implements IRequestTimer {

    private static volatile Boolean started;
    private int time;
    private String days;
    private String hours;
    private String minutes;
    private String seconds;
    private Date startTime;
    private Date endTime;
    private int countReq;

    @Override
    public void run() {
        started = true;

        while (true) {
            try {
                synchronized (started) {
                    while (!started) {
                        this.sleep(10000);
                    }
                    pingWorkTime();
                }
                System.out.println("++++++++++++++++++");
                if (!seconds.isEmpty()) time = Integer.parseInt(seconds);
                if (!minutes.isEmpty()) time += Integer.parseInt(minutes) * 60;
                if (!hours.isEmpty()) time += Integer.parseInt(hours) * 60 * 60;
                if (!days.isEmpty()) time += Integer.parseInt(days) * 60 * 60 * 24;
                sendRequests();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Could not parse time");
                started = false;
            }

        }
    }

    private void pingWorkTime() throws InterruptedException {
        if (startTime !=null && endTime != null &&
                startTime.after(currentTime()) && endTime.before(currentTime())){
            sleep(30000);
            pingWorkTime();
        }
    }

    private Date currentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
       String currentTimeStr = sdf.format(new Date());
        Date currentTime = null;
        try {
            currentTime = sdf.parse(currentTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    @Override
    public synchronized void startRequest(String days, String hours, String minutes, String seconds, Date startTime, Date endTime) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.startTime = startTime;
        this.endTime = endTime;
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
                        String responseXml = SendDataService.sendDataToSMEV(timerRequests.getRequestXml(), timerRequests.getAdapterId(), timerRequests.getSmevAddress());
                        String respStatus = XMLParser.getResponseStatus(responseXml);
                        Adapter adapter = new Adapter();
                        adapter.setId(Integer.parseInt(key));
                        adapter.setResponseXml(XMLParser.replacer(responseXml));
                        adapter.setResponseStatus(respStatus);
                        DatabaseUtil.saveResponseById(adapter);
                        if (respStatus.equals("ACCEPT")) {
                            TimerCache.getInstance().deleteRequest(key);
                            MainController.counter(timerRequests.getFoiv());
                        }
                        this.sleep(time * 1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else {
            MainController.writeStatusInExcelFromDB();
            stopRequest();
        }
    }
}
