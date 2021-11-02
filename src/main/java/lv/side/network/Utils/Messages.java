package lv.side.network.Utils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;

public class Messages {
    private static Map<String, String> messageMap = new HashMap<>();
    private static ConfigurationSection configuration = null;

    public static void load(ConfigurationSection messages) {
        configuration = messages;
        Collection<String> keys = messages.getKeys(true);
        for (String key : keys) {
            if (messages.isString(key)) {
                String value = messages.getString(key);
                value = ColorUtils.color(StringEscapeUtils.unescapeHtml(value));
                messageMap.put(key, value);
            }
        }
    }

    public static String get(String key) {
        if (messageMap.containsKey(key)) {
            return messageMap.get(key);
        }
        return "";
    }

    public static List<String> getList(String key) {
        if (configuration.contains(key) && !configuration.getString(key).equals("")) {
            return ColorUtils.color(Arrays.asList(configuration.getString(key).split("\n")));
        }
        return null;
    }

    public static String paramsReplace(String msg, String[] paramsName, String[] paramsValue) {
        for (int i = 0; i < paramsValue.length; i++) {
            if (i < paramsName.length)
                msg = msg.replace(paramsName[i], paramsValue[i]);
        }

        return msg;
    }

    public static String getParam(String key, String paramName, String paramValue) {
        return ColorUtils.color(get(key).replace(paramName, paramValue));
    }

    public static String getParam(String key, String[] paramsName, String[] paramsValue) {
        return ColorUtils.color(paramsReplace(get(key), paramsName, paramsValue));
    }

    public static List<String> getListParam(String key, String paramName, String paramValue) {
        if (configuration.contains(key) && !configuration.getString(key).equals("")) {
            return ColorUtils.color(Arrays.asList(configuration.getString(key).replace(paramName, paramValue).split("\n")));
        }
        return null;
    }

    public static List<String> getListParam(String key, String[] paramsName, String[] paramsValue) {
        if (configuration.contains(key) && !configuration.getString(key).equals("")) {
            return ColorUtils.color(Arrays.asList(paramsReplace(configuration.getString(key), paramsName, paramsValue).split("\n")));
        }
        return null;
    }
}