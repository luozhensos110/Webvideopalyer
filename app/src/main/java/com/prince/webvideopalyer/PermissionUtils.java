package com.prince.webvideopalyer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 关于申请授权
 * 只需要在主界面申请一次即可
 * 在其他子activity，自动授权
 * */
class PermissionUtils {
    //这是要申请的权限
    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    /**
     * &#x89e3;&#x51b3;&#x5b89;&#x5353;6.0&#x4ee5;&#x4e0a;&#x7248;&#x672c;&#x4e0d;&#x80fd;&#x8bfb;&#x53d6;&#x5916;&#x90e8;&#x5b58;&#x50a8;&#x6743;&#x9650;&#x7684;&#x95ee;&#x9898;
     *
     * @param activity
     * @param requestCode
     * @return
     */
    static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            //检测是否有权限，如果没有权限，就需要申请
            //申请权限
            //activity.requestPermissions(PERMISSIONS_CAMERA_AND_STORAGE, requestCode);
            //返回false。说明没有授权
            return storagePermission == PackageManager.PERMISSION_GRANTED &&
                    cameraPermission == PackageManager.PERMISSION_GRANTED;
        }
        //说明已经授权
        return true;
    }
}
