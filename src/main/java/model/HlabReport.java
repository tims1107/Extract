package model;

public class HlabReport {

    private String lname;
    private String fname;
    private String pstate;
    private String requisition;
    private String pcity;
    private String pzip;
    private String fstate;
    private String fcity;
    private String fzip;

    public HlabReport(String lname, String fname, String pstate, String requisition,
                      String pcity, String pzip, String fstate, String fcity, String fzip) {
        this.lname = lname;
        this.fname = fname;
        this.pstate = pstate;
        this.requisition = requisition;
        this.pcity = pcity;
        this.pzip = pzip;
        this.fstate = fstate;
        this.fcity = fcity;
        this.fzip = fzip;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getPstate() {
        return pstate;
    }

    public String getRequisition() {
        return requisition;
    }

    public String getPcity() {
        return pcity;
    }

    public String getPzip() {
        return pzip;
    }

    public String getFstate() {
        return fstate;
    }

    public String getFcity() {
        return fcity;
    }

    public String getFzip() {
        return fzip;
    }
}
