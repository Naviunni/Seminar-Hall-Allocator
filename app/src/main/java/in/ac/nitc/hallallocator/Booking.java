package in.ac.nitc.hallallocator;

public class Booking {
    private String hallName,date,fid,sname,status;

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Booking(String hallName, String date, String fid, String sname) {
        this.hallName = hallName;
        this.date = date;
        this.fid = fid;
        this.sname = sname;
    }

    public String getHallName() {
        return hallName;
    }

    public Booking(String hallName, String date, String fid, String sname, String status) {
        this.hallName = hallName;
        this.date = date;
        this.fid = fid;
        this.sname = sname;
        this.status = status;
    }

    public String getDate() {
        return date;

    }

    public String getFid() {
        return fid;
    }

    public String getSname() {
        return sname;
    }
}
