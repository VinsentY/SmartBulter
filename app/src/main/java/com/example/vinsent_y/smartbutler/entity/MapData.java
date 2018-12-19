package com.example.vinsent_y.smartbutler.entity;

import com.baidu.location.BDLocation;

public class MapData {
    private double latitude;
    private double longitude;
    private String country;
    private String province;
    private String city;
    //区
    private String district;
    private String street;
    boolean useGPS;

    public MapData(BDLocation location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        city = location.getCity();
        country = location.getCountry();
        province = location.getProvince();
        district = location.getDistrict();
        street = location.getStreet();
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            useGPS = true;
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            useGPS = false;
        }
    }

    @Override
    public String toString() {

        StringBuffer sbf = new StringBuffer();
        sbf.append("纬度： ").append(latitude).append("\n")
                .append("经线").append(longitude).append("\n")
                .append("国家：").append(country).append("\n")
                .append("省：").append(province).append("\n")
                .append("市: ").append(city).append("\n")
                .append("区: ").append(district).append("\n")
                .append("街道: ").append(street).append("\n")
                .append("定位方式: ");
        if (useGPS) sbf.append("GPS");
        else sbf.append("网络");
        return sbf.toString();
    }
}
