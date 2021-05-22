package com.xing.gfox.liveEventBus;


import androidx.annotation.NonNull;

import com.xing.gfox.liveEventBus.core.Config;
import com.xing.gfox.liveEventBus.core.LiveEvent;
import com.xing.gfox.liveEventBus.core.LiveEventBusCore;
import com.xing.gfox.liveEventBus.core.Observable;


//https://github.com/JeremyLiao/LiveEventBus
public final class LiveEventBus {

    /**
     * get observable by key with type
     *
     * @param key
     * @param type
     * @param <T>
     * @return Observable<T>
     */
    public static <T> Observable<T> get(@NonNull String key, @NonNull Class<T> type) {
        return LiveEventBusCore.get().with(key, type);
    }

    /**
     * get observable by key
     *
     * @param key
     * @return Observable<Object>
     */
    public static Observable<Object> get(@NonNull String key) {
        return get(key, Object.class);
    }

    /**
     * get observable from eventType
     *
     * @param eventType
     * @param <T>
     * @return Observable<T>
     */
    public static <T extends LiveEvent> Observable<T> get(@NonNull Class<T> eventType) {
        return get(eventType.getName(), eventType);
    }

    /**
     * use the inner class Config to set params
     * first of all, call config to get the Config instance
     * then, call the method of Config to config LiveEventBus
     * call this method in Application.onCreate
     */
    public static Config config() {
        return LiveEventBusCore.get().config();
    }
}
