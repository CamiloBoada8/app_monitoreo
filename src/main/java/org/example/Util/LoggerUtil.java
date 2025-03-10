package org.example.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    /**
     * Obtiene un logger para la clase especificada.
     *
     * @param clazz La clase para la cual se creará el logger.
     * @return Un logger configurado.
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Registra un mensaje de nivel INFO.
     *
     * @param clazz La clase desde donde se registra el mensaje.
     * @param message El mensaje a registrar.
     */
    public static void logInfo(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    /**
     * Registra un mensaje de nivel ERROR.
     *
     * @param clazz La clase desde donde se registra el mensaje.
     * @param message El mensaje a registrar.
     * @param throwable La excepción asociada (opcional).
     */
    public static void logError(Class<?> clazz, String message, Throwable throwable) {
        getLogger(clazz).error(message, throwable);
    }

    /**
     * Registra un mensaje de nivel DEBUG.
     *
     * @param clazz La clase desde donde se registra el mensaje.
     * @param message El mensaje a registrar.
     */
    public static void logDebug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    /**
     * Registra un mensaje de nivel WARN.
     *
     * @param clazz La clase desde donde se registra el mensaje.
     * @param message El mensaje a registrar.
     */
    public static void logWarn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }
}