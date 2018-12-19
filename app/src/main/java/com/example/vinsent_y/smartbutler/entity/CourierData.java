package com.example.vinsent_y.smartbutler.entity;

public class CourierData {

    private String dateTime;
    private String remark;
    private String zone;

    public CourierData() {
    }

    public CourierData(String dateTime, String remark, String zone) {
        this.dateTime = dateTime;
        this.remark = remark;
        this.zone = zone;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "dateTime='" + dateTime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
