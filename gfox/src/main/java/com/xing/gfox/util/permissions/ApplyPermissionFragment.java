package com.xing.gfox.util.permissions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.xing.gfox.util.U_permissions;

import java.util.HashMap;
import java.util.Map;

public class ApplyPermissionFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if (getArguments() != null && getActivity() != null) {
            if ("StoragePermission".equals(getArguments().getString("StoragePermission"))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                        Map<String, Boolean> results = new HashMap<>();
                        results.put("Storage_PERMISSIONS", Environment.isExternalStorageManager());
                        U_permissions.requestPermissionsResult(results);
                        getParentFragmentManager().beginTransaction().remove(this).commit();
                    });
                    launcher.launch(intent);
                }
            } else {
                String[] permissions = getArguments().getStringArray("permissions");
                ActivityResultLauncher<String[]> launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    U_permissions.requestPermissionsResult(result);
                    getParentFragmentManager().beginTransaction().remove(this).commit();
                });
                launcher.launch(permissions);
            }
        } else {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
