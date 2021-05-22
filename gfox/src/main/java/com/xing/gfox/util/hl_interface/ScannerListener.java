package com.xing.gfox.util.hl_interface;

import android.net.Uri;

public interface ScannerListener {
    void oneComplete(String path, Uri uri);

    void allComplete(String[] filePaths);

}
