package javafxapp.adapter;

import java.util.List;

public class Adapter {

    public static String foiv;
    public static String adapterName;
    public static List<String> requestsIp;
    public static List<String> requestsUl;

    public static String getFoiv() {
        return foiv;
    }

    public static void setFoiv(String foiv) {
        Adapter.foiv = foiv;
    }

    public static List<String> getRequestsIp() {
        return requestsIp;
    }

    public static void setRequestsIp(List<String> requestsIp) {
        Adapter.requestsIp = requestsIp;
    }

    public static List<String> getRequestsUl() {
        return requestsUl;
    }

    public static void setRequestsUl(List<String> requestsUl) {
        Adapter.requestsUl = requestsUl;
    }

    public static String getAdapterName() {
        return adapterName;
    }

    public static void setAdapterName(String adapterName) {
        Adapter.adapterName = adapterName;
    }
}
