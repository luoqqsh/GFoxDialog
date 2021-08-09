package com.xing.gfox.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.util.permissions.ApplyPermissionActivity;
import com.xing.gfox.util.permissions.ApplyPermissionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态权限申请
 */
public class U_permissions {

    public interface RequestPermissionCallBack {
        void requestPermissionSuccess();

        void requestPermissionFail(List<String> failPermission);
    }

    private static RequestPermissionCallBack mPermissionCallBack;

    public static boolean checkExternalStorageManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        if (permission == null) return false;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void applyWriteStoragePermission(Context context, RequestPermissionCallBack permissionCallBack) {
        applyPermission(context, permissionCallBack, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void applyLocationPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        applyPermission(context, permissionCallBack, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //申请后台定位权限
    public static void applyLocationOnBackPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            applyPermission(context, permissionCallBack, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            List<String> failPermission = new ArrayList<>();
            failPermission.add("android.permission.ACCESS_BACKGROUND_LOCATION");
            permissionCallBack.requestPermissionFail(failPermission);
        }
    }

    public static void applySmsPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        applyPermission(context, permissionCallBack, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS);
    }

    public static void applyCameraPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        applyPermission(context, permissionCallBack, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
    }

    public static void applyWallpaperPermission(Context context, RequestPermissionCallBack permissionCallBack) {
        applyPermission(context, permissionCallBack, Manifest.permission.SET_WALLPAPER, Manifest.permission.SET_WALLPAPER_HINTS, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void applyPermission2(FragmentActivity mActivity, RequestPermissionCallBack permissionCallBack, String... permissionList) {
        applyPermission2(mActivity, permissionList, permissionCallBack);
    }

    //注意：安卓11申请读写其他文件权限，需要在xml的<android>标签加上android:requestLegacyExternalStorage="true"
    public static void applyStoragePermission11(FragmentActivity mActivity, RequestPermissionCallBack permissionCallBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && mActivity != null) {
            if (checkExternalStorageManager()) {
                if (permissionCallBack != null) permissionCallBack.requestPermissionSuccess();
            } else {
                mPermissionCallBack = permissionCallBack;
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ApplyPermissionFragment applyPermissionFragment = (ApplyPermissionFragment) mActivity.getSupportFragmentManager().findFragmentByTag("rxp");
                if (applyPermissionFragment == null) {
                    applyPermissionFragment = new ApplyPermissionFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("StoragePermission", "StoragePermission");
                applyPermissionFragment.setArguments(bundle);
                if (!applyPermissionFragment.isAdded()) {
                    mActivity.getSupportFragmentManager().beginTransaction().add(applyPermissionFragment, "rxp").commitNow();
                }
            }
        } else {
            applyWriteStoragePermission(mActivity, permissionCallBack);
        }
    }

    public static void applyPermission2(FragmentActivity mActivity, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        if (permissionList.length == 0) return;
        //6.0及以上 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mActivity != null) {
            boolean isNeedApply = false;
            for (String permission : permissionList) {
                if (!checkPermission(mActivity, permission)) {
                    isNeedApply = true;
                    break;
                }
            }
            if (isNeedApply) {
                mPermissionCallBack = permissionCallBack;
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ApplyPermissionFragment applyPermissionFragment = (ApplyPermissionFragment) mActivity.getSupportFragmentManager().findFragmentByTag("rxp");
                if (applyPermissionFragment == null) {
                    applyPermissionFragment = new ApplyPermissionFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("permissions", permissionList);
                applyPermissionFragment.setArguments(bundle);
                if (!applyPermissionFragment.isAdded()) {
                    mActivity.getSupportFragmentManager().beginTransaction().add(applyPermissionFragment, "rxp").commitNow();
                }
            } else {
                if (permissionCallBack != null) {
                    permissionCallBack.requestPermissionSuccess();
                }
            }
        } else {
            //6.0以下直接执行
            if (permissionCallBack != null) {
                permissionCallBack.requestPermissionSuccess();
            }
        }
    }

    public static void applyPermission(Context context, RequestPermissionCallBack permissionCallBack, String... permissionList) {
        applyPermission(context, permissionList, permissionCallBack);
    }

    public static void applyPermission(Context context, String[] permissionList, RequestPermissionCallBack permissionCallBack) {
        if (permissionList.length == 0) return;
        //6.0及以上 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            boolean isNeedApply = false;
            for (String permission : permissionList) {
                if (!checkPermission(context, permission)) {
                    isNeedApply = true;
                    break;
                }
            }
            if (isNeedApply) {
                mPermissionCallBack = permissionCallBack;
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ApplyPermissionActivity.launch(context, permissionList);
            } else {
                if (permissionCallBack != null) {
                    permissionCallBack.requestPermissionSuccess();
                }
            }
        } else {
            //6.0以下直接执行
            if (permissionCallBack != null) {
                permissionCallBack.requestPermissionSuccess();
            }
        }
    }

    public static void requestPermissionsResult(Map<String, Boolean> results) {
        List<String> fail = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : results.entrySet()) {
            if (!entry.getValue()) {
                fail.add(entry.getKey());
            }
        }
        if (fail.size() != 0) {//被拒绝了
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionFail(fail);
            }
        } else { //申请成功
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionSuccess();
            }
        }
        mPermissionCallBack = null;
    }

    public static void requestPermissionsResult(String[] permissions, int[] grantResults) {
        List<String> failPermission = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                failPermission.add(permissions[i]);
            }
        }
        if (failPermission.size() != 0) {//被拒绝了
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionFail(failPermission);
            }
        } else {
            //申请成功
            if (mPermissionCallBack != null) {
                mPermissionCallBack.requestPermissionSuccess();
            }
        }
        mPermissionCallBack = null;
    }

    public static boolean checkIsAlwaysFail(Activity activity, String permission) {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
