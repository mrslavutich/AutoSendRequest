package javafxapp.sheduler;

public interface IRequestTimer extends Runnable {
    @Override
    void run();

    void startRequest(int time, String measure);

    void stopRequest();

    void sendRequests() throws Exception;
}
