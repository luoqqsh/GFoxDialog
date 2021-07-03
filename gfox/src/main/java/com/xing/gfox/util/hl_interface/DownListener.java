package com.xing.gfox.util.hl_interface;

import java.io.File;

public interface DownListener {
    void onSuccess(File file);

    void onFail(String message);

    void downloading(int progress);
}
