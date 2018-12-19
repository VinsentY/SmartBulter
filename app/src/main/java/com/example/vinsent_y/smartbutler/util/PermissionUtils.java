package com.example.vinsent_y.smartbutler.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtils {
    //九个危险权限
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 7;
    public static final int CODE_SMS_RECEIVE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_SMS_RECEIVE = Manifest.permission.RECEIVE_SMS;

    private static Map<String, Integer> requestPermissions = new HashMap<>();

    static {
        requestPermissions.put(PERMISSION_RECORD_AUDIO, CODE_RECORD_AUDIO);
        requestPermissions.put(PERMISSION_GET_ACCOUNTS, CODE_GET_ACCOUNTS);
        requestPermissions.put(PERMISSION_READ_PHONE_STATE, CODE_READ_PHONE_STATE);
        requestPermissions.put(PERMISSION_CALL_PHONE, CODE_CALL_PHONE);
        requestPermissions.put(PERMISSION_CAMERA, CODE_CAMERA);
        requestPermissions.put(PERMISSION_ACCESS_FINE_LOCATION, CODE_ACCESS_FINE_LOCATION);
        requestPermissions.put(PERMISSION_ACCESS_COARSE_LOCATION, CODE_ACCESS_COARSE_LOCATION);
        requestPermissions.put(PERMISSION_WRITE_EXTERNAL_STORAGE, CODE_WRITE_EXTERNAL_STORAGE);
        requestPermissions.put(PERMISSION_SMS_RECEIVE, CODE_SMS_RECEIVE);
    }

    public abstract static class PermissionGrant {
        private List<String> mPermissionList = new ArrayList<>();

        public abstract void execute();
    }

    public static void requestPermission(Activity activity, String[] permissions, PermissionGrant permissionGrant) {
        for (String permission : permissions) {
            //如果权限没有申请
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionGrant.mPermissionList.add(permission);
            }
        }

        if (permissionGrant.mPermissionList.isEmpty()) {
            //方法与权限申请捆绑在一起
            //执行方法
            permissionGrant.execute();
        } else if (permissionGrant.mPermissionList.size() == 1) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionGrant.mPermissionList.get(0)}, requestPermissions.get(permissionGrant.mPermissionList.get(0)));
        } else {
            ActivityCompat.requestPermissions(activity,
                    permissionGrant.mPermissionList.toArray(new String[]{}), CODE_MULTI_PERMISSION);
        }

    }

    public static void requestPermissionInFragment(Fragment fragment, String[] permissions, PermissionGrant permissionGrant) {
        for (String permission : permissions) {
            //如果权限没有申请
            if (ContextCompat.checkSelfPermission(fragment.getActivity(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionGrant.mPermissionList.add(permission);
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{permission}, requestPermissions.get(permission));
            }
        }

        if (permissionGrant.mPermissionList.isEmpty()) {
            //方法与权限申请捆绑在一起
            //执行方法
            permissionGrant.execute();
        } else if (permissionGrant.mPermissionList.size() == 1) {
            fragment.requestPermissions(
                    new String[]{permissionGrant.mPermissionList.get(0)}, requestPermissions.get(permissionGrant.mPermissionList.get(0)));
        } else {
            fragment.requestPermissions(
                    permissionGrant.mPermissionList.toArray(new String[]{}), CODE_MULTI_PERMISSION);
        }
    }

    public static void requestPermissionsResult(Context context, String[] permissions, int requestCode, int[] grantResults, PermissionGrant permissionGrant) {
        if (permissions.length == 1) {
            if (requestCode == requestPermissions.get(permissions[0])) {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    permissionGrant.execute();
                } else {
                    Toast.makeText(context, "权限申请被拒绝", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (requestCode == CODE_MULTI_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    permissionGrant.execute();
                } else {
                    Toast.makeText(context, "权限申请被拒绝", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
