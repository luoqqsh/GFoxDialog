package com.xing.gfox.base.provider;

import androidx.core.content.FileProvider;

import com.xing.gfox.base.app.AppInit;

public class FileContext extends FileProvider {
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            AppInit.setContext(getContext());
        }
        return super.onCreate();
    }
}
