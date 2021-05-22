package com.xing.gfox.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class VolumeDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int keyCode = intent.getIntExtra("KEY_EVENT_EXTRA", KeyEvent.KEYCODE_UNKNOWN);
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
        }
    }
}
