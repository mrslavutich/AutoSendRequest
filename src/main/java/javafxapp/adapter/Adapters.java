package javafxapp.adapter;

import java.util.List;

public class Adapters {

    public static String nameAdapter;
    public static List<String> requests;

    public static String getNameAdapter() {
        return nameAdapter;
    }

    public static void setNameAdapter(String nameAdapter) {
        Adapters.nameAdapter = nameAdapter;
    }

    public static List<String> getRequests() {
        return requests;
    }

    public static void setRequests(List<String> requests) {
        Adapters.requests = requests;
    }
}
