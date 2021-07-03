// MyEventBusService.aidl
package com.xing.gfox.hl_study.Hermes.annotion;

// Declare any non-default types here with import statements
import com.xing.gfox.hl_study.Hermes.annotion.Request;
import com.xing.gfox.hl_study.Hermes.annotion.Responce;
interface MyEventBusService {
    Responce send(in Request request);
}
