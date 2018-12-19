package com.example.vinsent_y.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.MapData;
import com.example.vinsent_y.smartbutler.util.PermissionUtils;

import static com.example.vinsent_y.smartbutler.util.PermissionUtils.*;

public class LocationActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private MapView mv_baidu;
    private TextView tv_location;
    private BaiduMap baiduMap;

    private MapData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mv_baidu.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_baidu.onPause();
    }

    private void initView() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        tv_location = findViewById(R.id.tv_location);
        mv_baidu = findViewById(R.id.mv_baidu);
        baiduMap = mv_baidu.getMap();
        baiduMap.setMyLocationEnabled(true);
        PermissionUtils.requestPermission(this,new String[]{PERMISSION_ACCESS_FINE_LOCATION,PERMISSION_READ_PHONE_STATE,PERMISSION_WRITE_EXTERNAL_STORAGE},start);
    }

    private PermissionUtils.PermissionGrant start = new PermissionUtils.PermissionGrant() {
        @Override
        public void execute() {
            initLocation();
            mLocationClient.start();
        }
    };

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        //传感器模式
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);

    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            data = new Data(bdLocation);
////            tv_location.setText(data.toString());
            navigateTo(bdLocation);
        }
    }

    private void navigateTo(BDLocation location) {
        //纬度 经度
        MapStatusUpdate update = null;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

        //显示我的位置
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        baiduMap.setMyLocationData(locationBuilder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mv_baidu.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this,permissions,requestCode,grantResults,start);
    }
}
