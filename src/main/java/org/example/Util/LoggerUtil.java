package org.example.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    /**
     * Obtiene un logger para la clase especificada.
     *
     * @param clazz La clase para la cual se creará el logger.
     * @return Un logger configurado.
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}