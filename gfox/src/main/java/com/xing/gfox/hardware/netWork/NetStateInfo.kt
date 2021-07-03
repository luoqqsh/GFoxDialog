package com.xing.gfox.hardware.netWork

import java.io.Serializable

class NetStateInfo : Serializable {
    var tag: String = ""
    var netWorkType = ""
    var state = NetWorkState.UNKNOWN
        set(state) {
            if (state != NetWorkState.CONNECT && state != NetWorkState.AVAILABLE) {
                blocked = false
                dns = ""
                privateDnsServerName = ""
                ipAddress = ""
            }
            if (state != NetWorkState.CONNECT && field != NetWorkState.AVAILABLE) {
                field = state
            }
        }
    var blocked = false
    var dns: String? = ""
    var privateDnsServerName: String? = ""
    var ipAddress: String? = ""

    fun isConnect(): Boolean {
        return state == NetWorkState.CONNECT || state == NetWorkState.AVAILABLE
    }
}