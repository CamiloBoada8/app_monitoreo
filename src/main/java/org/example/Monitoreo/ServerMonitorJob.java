package org.example.Monitoreo;

import org.example.Main;
import org.example.Util.LoggerUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServerMonitorJob implements Job {

    private static final Logger logger = LoggerUtil.getLogger(ServerMonitorJob.class);
    private static final Set<Integer> monitoredPorts = new HashSet<>();

    public static void addMonitoredPort(int port) {
        if (monitoredPorts.add(port)) {
            logger.info("Puerto añadido al monitoreo: " + port);
        }
    }

    @Override
    public void execute(JobExecutionContext context) {
        if (monitoredPorts.isEmpty()) {
            logger.warn("No hay puertos registrados para monitorear.");
            return;
        }

        synchronized (monitoredPorts) {
            Iterator<Integer> iterator = monitoredPorts.iterator();

            while (iterator.hasNext()) {
                int port = iterator.next();
                boolean serverUp = false;

                int maxAttempts = 3;
                for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                    if (isServerUp("localhost", port)) {
                        serverUp = true;
                        break;
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            logger.error("Error durante la espera entre intentos: " + e.getMessage());
                        }
                    }
                }

                if (serverUp) {
                    logger.info("Servidor en puerto " + port + ": ACTIVO");
                } else {
                    logger.warn("Servidor en puerto " + port + ": INACTIVO");

                    iterator.remove();

                    SocketServer socketServer = Main.getSocketServerInstance();
                    if (socketServer != null) {
                        socketServer.removeMonitoredPort(port);
                    }

                    sendServerDownMessage(port);
                }
            }
        }
    }

    private boolean isServerUp(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void sendServerDownMessage(int inactivePort) {
        try (Socket socket = new Socket("localhost", 12345);
             OutputStream outputStream = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(outputStream, true)) {

            String message = "SERVER_DOWN " + inactivePort;
            writer.println(message);
            logger.info("Mensaje enviado al puerto 12345: " + message);

        } catch (IOException e) {
            logger.error("Error enviando mensaje al puerto 12345: " + e.getMessage());
        }
    }
}