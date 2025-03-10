package org.example.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {
    private static final Logger logger = LoggerFactory.getLogger(Configuracion.class);
    private static final Properties props = new Properties();

    public static String SERVER_HOST;
    public static int SERVER_PORT;
    public static String RESTART_SCRIPT_PATH;
    public static final int MONITOR_INTERVAL_SECONDS = Integer.parseInt(props.getProperty("monitor.interval.seconds", "30"));

    static {
        init();
    }

    public static void init() {
        logger.info("Cargando configuración...");

        try (InputStream input = Configuracion.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);

            SERVER_HOST = props.getProperty("server.host", "localhost");
            SERVER_PORT = Integer.parseInt(props.getProperty("server.port", "54321"));
            RESTART_SCRIPT_PATH = props.getProperty("restart.script.path", "/ruta/default/restart_server.sh");

            logConfiguracion();

        } catch (Exception e) {
            logger.error("Error cargando configuración", e);
            setDefaultValues();
        }
    }

    private static void logConfiguracion() {
        logger.info("Host del servidor: {}", SERVER_HOST);
        logger.info("Puerto del servidor: {}", SERVER_PORT);
        logger.info("Script de reinicio: {}", RESTART_SCRIPT_PATH);
    }

    private static void setDefaultValues() {
        SERVER_HOST = "localhost";
        SERVER_PORT = 54321;
        RESTART_SCRIPT_PATH = "/ruta/default/restart_server.sh";
        logger.warn("Usando valores por defecto debido a error de configuración");
    }
}