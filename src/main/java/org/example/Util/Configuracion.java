package org.example.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuracion {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 12345;

    public static final String RESTART_SCRIPT_PATH = "/ruta/al/proyecto/scripts/restart_server.sh";

    private static final Logger logger = LoggerFactory.getLogger(Configuracion.class);

    /**
     * Inicializa la configuración del sistema.
     */
    public static void init() {
        logger.info("Inicializando configuración del sistema...");
        logger.info("Servidor escuchando en {}:{}", SERVER_HOST, SERVER_PORT);
        logger.info("Script de reinicio ubicado en: {}", RESTART_SCRIPT_PATH);
    }
}