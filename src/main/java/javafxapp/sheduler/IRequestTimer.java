package javafxapp.sheduler;

import java.util.Date;

public interface IRequestTimer extends Runnable {
    @Override
    void run();

    void startRequest(String days, String hours, String minutes, String seconds, Date startTime, Date endTime);

    void stopRequest();

    void sendRequests() throws Exception;
}
