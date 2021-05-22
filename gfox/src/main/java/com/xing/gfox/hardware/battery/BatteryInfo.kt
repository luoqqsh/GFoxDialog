package com.xing.gfox.hardware.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import java.io.Serializable

class BatteryInfo : Serializable {
    var voltage = 0 // 当前电池的电压
    var level = 0 // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
    var scale = 100 // 电池电量的最大值
    var chargeCounter = 0 // 充电次数
    var temperature = 0 //电池温度
    var health = 0 // 电池的健康状态
        set(health) {
            field = health
            when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> healthStr = "正常"
                BatteryManager.BATTERY_HEALTH_COLD -> healthStr = "过冷"
                BatteryManager.BATTERY_HEALTH_DEAD -> healthStr = "良好"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> healthStr = "过热"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> healthStr = "电压过高"
                BatteryManager.BATTERY_HEALTH_UNKNOWN, BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> healthStr =
                    "未知"
                else -> {
                    healthStr = "未知"
                }
            }
        }
    var healthStr = "未知" // 电池的健康状态
    var pluged = 0 // 当前手机使用的是哪里的电源
        set(pluged) {
            field = pluged
            when (pluged) {
                BatteryManager.BATTERY_PLUGGED_AC -> plugedStr = "电源充电"
                BatteryManager.BATTERY_PLUGGED_USB -> plugedStr = "USB充电"
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> plugedStr = "无线充电"
            }
        }
    var plugedStr = "未知" // 当前手机使用的是哪里的电源
    var status = 0 //当前状态
        set(status) {
            field = status
            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> statusStr = "正在充电"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> statusStr = "放电中"
                BatteryManager.BATTERY_STATUS_FULL -> statusStr = "充满"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> statusStr = "没有充电"
                BatteryManager.BATTERY_STATUS_UNKNOWN -> statusStr = "未知"
                else -> {
                }
            }
        }
    var statusStr = "未知" //当前状态
    var technology = "未知" //电池技术
    var max_charging_voltage = 0 //充电器支持的最大充电电压，单位微伏
    var max_charging_current = 0 //充电器支持的最大充电电流，单位为微安
    var batteryIcon = 0 //电池图标
    var seq = 0//电池序列号

    override fun toString(): String {
        return """
            电池电压:${voltage / 1000.0f}V
            电池健康状态:$healthStr
            当前电量:$level
            最大电量:$scale
            充电方式:$plugedStr
            充电状态:$statusStr
            电池技术:$technology
            充电次数:$chargeCounter
            电池序列号:$seq
            充电器支持最大电流:${max_charging_current / 1000000f}A
            充电器支持最大电压:${max_charging_voltage / 1000000f}V
            电池温度:${temperature / 10f}℃
            """.trimIndent()
    }

    companion object {
        private const val serialVersionUID = 7195732595127362203L

        @JvmStatic
        fun getSystemBattery(context: Context): Int {
            var level = 0
            val batteryInfoIntent = context.applicationContext.registerReceiver(
                null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            level = batteryInfoIntent!!.getIntExtra("level", 0)
            val batterySum = batteryInfoIntent.getIntExtra("scale", 100)
            return 100 * level / batterySum
        }
    }
}