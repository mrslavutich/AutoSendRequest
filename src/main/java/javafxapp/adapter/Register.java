package javafxapp.adapter;

/**
 * User: vmaksimov
 */
public enum Register {
    FNS("ФНС", "(Сведения из ЕГРИП)", "(Сведения из ЕГРЮЛ)"),
    PFR("ПФР", "(Сведения о страховом номере индивидуального лицевого счета (СНИЛС по данным))"),
    MVD("МВД", "(Сведения о судимости)");

    public String foiv;
    public String adapter;
    public String adapterUL;

    public String getFoiv() {
        return foiv;
    }

    public void setFoiv(String foiv) {
        this.foiv = foiv;
    }

    public String getAdapter() {
        return adapter;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    Register(String foiv, String adapter) {
        this.foiv = foiv;
        this.adapter = adapter;
    }

    Register(String foiv, String adapter, String adapterUL) {
        this.foiv = foiv;
        this.adapter = adapter;
        this.adapterUL = adapterUL;
    }
}
