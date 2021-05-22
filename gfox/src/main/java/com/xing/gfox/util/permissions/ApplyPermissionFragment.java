package com.xing.gfox.util.permissions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.xing.gfox.util.U_permissions;

public class ApplyPermissionFragment extends Fragment {
    private final int REQUEST_CODE_PERMISSIONS = 1211;
    private final int REQUEST_Storage_PERMISSIONS = 1212;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null && getActivity() != null) {
            if ("StoragePermission".equals(getArguments().getString("StoragePermission"))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, REQUEST_Storage_PERMISSIONS);
                }
            } else {
                String[] permissions = getArguments().getStringArray("permissions");
                requestPermissions(permissions, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            getParentFragmentManager().beginTransaction().remove(this).commitNow();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean[] isNoTip = new boolean[permissions.length];
            for (int i = 0; i < permissions.length; i++) {
                isNoTip[i] = !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i]);
            }
            U_permissions.requestPermissionsResult(permissions, grantResults, isNoTip);
            getParentFragmentManager().beginTransaction().remove(this).commitNow();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_Storage_PERMISSIONS && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                U_permissions.requestPermissionsResult(new String[]{"Storage_PERMISSIONS"}, new int[]{PackageManager.PERMISSION_GRANTED}, new boolean[]{false});
            } else {
                U_permissions.requestPermissionsResult(new String[]{"Storage_PERMISSIONS"}, new int[]{PackageManager.PERMISSION_DENIED}, new boolean[]{false});
            }
            getParentFragmentManager().beginTransaction().remove(this).commitNow();
        }
    }
}
