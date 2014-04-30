package javafxapp.adapter;

/**
 * User: vmaksimov
 */
public enum Register {


    id07UL_short("Сведения из ЕГРЮЛ(краткие)", "07UL_short"),
    id07FL_short("Сведения из ЕГРИП(краткие)", "07FL_short"),
    id07UL_full("Сведения из ЕГРЮЛ(полные)", "07UL_full"),
    id07FL_full("Сведения из ЕГРИП(полные)", "07FL_full"),
    id410("Сведения о судимости", "410");

    private String nameAdapter;
    private String id210fz;

    public String getId210fz() {
        return id210fz;
    }

    public void setId210fz(String id210fz) {
        this.id210fz = id210fz;
    }

    public String getNameAdapter() {
        return nameAdapter;
    }

    public void setNameAdapter(String nameAdapter) {
        this.nameAdapter = nameAdapter;
    }


    public enum foiv {
        FNS("ФНС"),
        MVD("МВД");
        private String value;
        private foiv(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    Register(String nameAdapter, String id210fz) {
        this.nameAdapter = nameAdapter;
        this.id210fz = id210fz;
    }

    public static String findId210fz(String nameAdapter) {
        for (Register register : Register.values()) {
            if (nameAdapter.contains(register.nameAdapter)) return register.id210fz;
        }
        return "";

    }

    public static String findNameAdapter(String id210fz) {
        for (Register register : Register.values()) {
            if (id210fz.contains(register.getId210fz())) return register.nameAdapter;
        }
        return "";

    }
}
