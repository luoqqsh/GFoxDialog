package com.xing.gfox.hardware.netWork;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.xing.gfox.log.ViseLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class U_wifi {

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiMgr.setWifiEnabled(false);
        if (wifiMgr.isWifiEnabled()) {
            return wifiMgr.getConnectionInfo();
        } else {
            return null;
        }
    }

    public static void printWifiInfo(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        StringBuilder str = new StringBuilder("网络信息\n");
        if (wifiInfo != null) {
            str.append("wifi名：").append(wifiInfo.getSSID()).append("\n");
            str.append("wifi-macAddress：").append(U_wifi.getMacAddress(context)).append("\n");
            str.append("wifi-Ip：").append(U_net.longToIp(wifiInfo.getIpAddress())).append("\n");
            str.append("wifi速度：").append(wifiInfo.getLinkSpeed()).append(WifiInfo.LINK_SPEED_UNITS).append("\n");
            int strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
            str.append("wifi信号强度：").append(strength);
        } else {
            str.append("未连接WIFI");
        }
        ViseLog.d(str.toString());
    }

    public static String getMacAddress(Context context) {
        String marshmallowMacAddress = "02:00:00:00:00:00";
        WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String macAddress = "";
        if (wifiInf != null) {
            if (marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
                String result;
                try {
                    result = getAddressMacByInterface();
                    if (TextUtils.isEmpty(result)) {
                        result = getAddressMacByFile(wifiMan);
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                macAddress = wifiInf.getMacAddress();
                if (macAddress != null) {
                    return macAddress;
                } else {
                    return "";
                }
            }
            return marshmallowMacAddress;
        }
        return macAddress;
    }

    private static String getAddressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();
        wifiMan.setWifiEnabled(true);
        File fl = new File("/sys/class/net/wlan0/address");
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();
        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, StandardCharsets.UTF_8));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }
}
