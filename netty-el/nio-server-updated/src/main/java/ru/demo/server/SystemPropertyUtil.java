package ru.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemPropertyUtil {
    private static final Logger log = LoggerFactory.getLogger(SystemPropertyUtil.class);

    private SystemPropertyUtil() {
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to {@code null} if the property access fails.
     *
     * @return the property value or {@code null}
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     * {@code def} if there's no such property or if an access to the
     * specified property is not allowed.
     */
    public static String get(final String key, String def) {
        checkNonEmpty(key, "key");

        String value = null;
        try {
            value = System.getProperty(key);
        } catch (SecurityException e) {
            log.warn("Unable to retrieve a system property '{}'; default values will be used.", key, e);
        }

        if (value == null) {
            return def;
        }

        return value;
    }


    public static int getInt(String key, int def) {
        String value = get(key);
        if (value == null) {
            return def;
        }

        value = value.trim();
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            // Ignore
        }

        log.warn(
                "Unable to parse the integer system property '{}':{} - using the default value: {}",
                key, value, def
        );

        return def;
    }

    private static void checkNonEmpty(String value, String name) {
        if (checkNotNull(value, name).isEmpty()) {
            throw new IllegalArgumentException("Param '" + name + "' must not be empty");
        }
    }

    private static <T> T checkNotNull(T arg, String text) {
        if (arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }
}
