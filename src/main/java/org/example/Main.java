package org.example;

import org.example.Monitoreo.ApplicationManager;
import org.example.Monitoreo.SocketServer;
import org.example.Util.Configuracion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static SocketServer socketServerInstance;

    public static void main(String[] args) {
        try {
            Configuracion.init();

            socketServerInstance = new SocketServer(Configuracion.SERVER_PORT);

            ApplicationManager appManager = new ApplicationManager(socketServerInstance);
            appManager.start();

        } catch (Exception e) {
            logger.error("Error inesperado", e);
        }
    }

    /**
     * Obtiene la instancia de SocketServer.
     */
    public static SocketServer getSocketServerInstance() {
        return socketServerInstance;
    }
}