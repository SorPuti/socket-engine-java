package br.com.github.puti.engine.socket.logger;

import java.util.Arrays;

public class Console {
    private static String prefix = "[Console]";

    public static void log(String... messages) {
        for (String message : messages)
            System.out.println(prefix + " " + message);
    }

    public static void printException(Exception exception) {

        log(
                "",
                " ! SocketEngine Exception !",
                " Message: " + exception.getMessage(),
                " Localized Message: " + exception.getLocalizedMessage(),
                " Cause: " + exception.getCause(),
                " Suppressed: " + Arrays.toString(exception.getSuppressed()),
                " StackTrace: " + Arrays.toString(exception.getStackTrace()),
                "");

    }

    public static String getPrefix() {
        return prefix;
    }

}
