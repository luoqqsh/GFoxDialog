package com.xing.gfox.hardware.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.text.TextUtils
import com.xing.gfox.liveEventBus.LiveEventBus
import com.xing.gfox.log.ViseLog


/**
 * 初始化
IntentFilter filter = new IntentFilter();
filter.addAction(Intent.ACTION_BATTERY_CHANGED);
filter.addAction(Intent.ACTION_BATTERY_LOW);
filter.addAction(Intent.ACTION_BATTERY_OKAY);
batteryChangedReceiver = new BatteryChangedReceiver();
context.registerReceiver(batteryChangedReceiver, filter);
//unregisterReceiver(batteryChangedReceiver);

 * 获取数据
LiveEventBus.get("batteryInfo", BatteryInfo::class.java)
.observe(mActivity, Observer { info -> mainInfo.text = info.toString() })

 * 获取低电量变化
LiveEventBus.get("lowPower", Boolean::class.java)
.observe(mActivity, Observer { isLowPower -> ViseLog.d(isLowPower) })
 */
class BatteryChangedReceiver : BroadcastReceiver() {
    private var batteryInfo: BatteryInfo? = null
    private var batteryInfoStr: String? = null
    fun register(context: Context) {
        if (batteryChangedReceiver != null) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_BATTERY_CHANGED)
            filter.addAction(Intent.ACTION_BATTERY_LOW)
            filter.addAction(Intent.ACTION_BATTERY_OKAY)
            context.applicationContext.registerReceiver(batteryChangedReceiver, filter)
        }
    }

    fun unregister(context: Context) {
        if (batteryChangedReceiver != null) {
            context.applicationContext.unregisterReceiver(batteryChangedReceiver)
            batteryChangedReceiver = null
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (batteryInfo == null) batteryInfo = BatteryInfo()
        when {
            action.equals(Intent.ACTION_BATTERY_CHANGED, ignoreCase = true) -> {
                batteryInfo!!.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
                batteryInfo!!.batteryIcon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1)
                batteryInfo!!.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                batteryInfo!!.chargeCounter = intent.getIntExtra("charge_counter", -1)
                batteryInfo!!.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                batteryInfo!!.seq = intent.getIntExtra("seq", 0)
                batteryInfo!!.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
                        ?: "未知"
                batteryInfo!!.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                batteryInfo!!.max_charging_current = intent.getIntExtra("max_charging_current", -1)
                batteryInfo!!.max_charging_voltage = intent.getIntExtra("max_charging_voltage", -1)
                batteryInfo!!.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                batteryInfo!!.pluged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                batteryInfo!!.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            }
            action.equals(Intent.ACTION_BATTERY_LOW, ignoreCase = true) -> {
                // 表示当前电池电量低
                LiveEventBus.get("lowPower", Boolean::class.java).post(true)
            }
            action.equals(Intent.ACTION_BATTERY_OKAY, ignoreCase = true) -> {
                // 表示当前电池已经从电量低恢复为正常
                LiveEventBus.get("lowPower", Boolean::class.java).post(false)
            }
        }
        LiveEventBus.get("batteryInfo", BatteryInfo::class.java).post(batteryInfo)
        if (TextUtils.isEmpty(batteryInfoStr)) {
            batteryInfoStr = batteryInfo.toString()
            ViseLog.d("电池信息\n$batteryInfoStr")
        }
    }

    companion object {
        private var batteryChangedReceiver: BatteryChangedReceiver? = null

        @JvmStatic
        val instance: BatteryChangedReceiver
            get() {
                if (batteryChangedReceiver == null) {
                    batteryChangedReceiver = BatteryChangedReceiver()
                }
                return batteryChangedReceiver!!
            }
    }
}