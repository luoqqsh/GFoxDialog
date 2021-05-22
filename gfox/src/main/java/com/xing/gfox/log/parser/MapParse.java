package com.xing.gfox.log.parser;


import com.xing.gfox.log.common.LogConvert;

import java.util.Map;
import java.util.Set;

/**
 * @Description: Map解析器
 */
public class MapParse implements Parser<Map> {
    @Override
    public Class<Map> parseClassType() {
        return Map.class;
    }

    @Override
    public String parseString(Map map) {
        String msg = map.getClass().getName() + " [" + LINE_SEPARATOR;
        Set keys = map.keySet();
        for (Object key : keys) {
            String itemString = "%s -> %s" + LINE_SEPARATOR;
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                } else if (value instanceof Character) {
                    value = "\'" + value + "\'";
                }
            }
            msg += String.format(itemString, LogConvert.objectToString(key),
                    LogConvert.objectToString(value));
        }
        return msg + "]";
    }
}
