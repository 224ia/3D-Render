package Util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter TIME_FORMAT =
                DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static boolean debugMode = true;

    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
    }

    private static void log(Object message, LogLevel logLevel) {
        if (message == null || logLevel == null) {
            throw new IllegalArgumentException("Message and log level can't be null");
        }
        System.out.printf("[%s] [%s] %s%n",
                LocalTime.now().format(TIME_FORMAT), logLevel.levelName, message);
    }

    public static void info(Object message) {
        log(message, LogLevel.INFO);
    }

    public static void warn(Object message) {
        log(message, LogLevel.WARN);
    }

    public static void error(Object message) {
        log(message, LogLevel.ERROR);
    }

    public static void debug(Object message) {
        if (debugMode) {
            log(message, LogLevel.DEBUG);
        }
    }

    private enum LogLevel {
        INFO("Info"),
        WARN("Warn"),
        ERROR("Error"),
        DEBUG("Debug");

        private final String levelName;
        LogLevel(String levelName) {
            this.levelName = levelName;
        }
    }
}