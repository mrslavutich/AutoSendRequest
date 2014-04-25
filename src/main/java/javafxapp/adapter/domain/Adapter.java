package javafxapp.adapter.domain;

public class Adapter {

    private Integer id;
    private String id210fz;
    private Integer numReq;
    private AdapterDetails adapterDetails;
    private String requestXml;
    private String responseXml;
    private String responseStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumReq() {
        return numReq;
    }

    public void setNumReq(Integer numReq) {
        this.numReq = numReq;
    }

    public String getId210fz() {
        return id210fz;
    }

    public void setId210fz(String id210fz) {
        this.id210fz = id210fz;
    }

    public String getRequestXml() {
        return requestXml;
    }

    public void setRequestXml(String requestXml) {
        this.requestXml = requestXml;
    }

    public String getResponseXml() {
        return responseXml;
    }

    public void setResponseXml(String responseXml) {
        this.responseXml = responseXml;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public AdapterDetails getAdapterDetails() {
        return adapterDetails;
    }

    public void setAdapterDetails(AdapterDetails adapterDetails) {
        this.adapterDetails = adapterDetails;
    }
}
