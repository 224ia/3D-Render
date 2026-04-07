package Util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static boolean debugMode = false;

    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
    }

    public static void info(String message) {
        System.out.printf("[%s] [INFO] %s%n",
                LocalTime.now().format(TIME_FORMAT), message);
    }

    public static void warn(String message) {
        System.out.printf("[%s] [WARN] %s%n",
                LocalTime.now().format(TIME_FORMAT), message);
    }

    public static void error(String message) {
        System.err.printf("[%s] [ERROR] %s%n",
                LocalTime.now().format(TIME_FORMAT), message);
    }

    public static void debug(String message) {
        if (debugMode) {
            System.out.printf("[%s] [DEBUG] %s%n",
                    LocalTime.now().format(TIME_FORMAT), message);
        }
    }
}