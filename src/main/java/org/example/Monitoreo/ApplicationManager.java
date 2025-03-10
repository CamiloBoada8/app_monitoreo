package org.example.Monitoreo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ApplicationManager {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    private final SocketServer socketServer;
    private final Set<Integer> previouslySyncedPorts = new HashSet<>();
    private Thread syncThread;

    public ApplicationManager(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    public void start() {
        try {
            ServerMonitorScheduler.main(new String[]{});

            Thread socketThread = new Thread(socketServer);
            socketThread.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Deteniendo aplicación...");
                try {
                    ServerMonitorScheduler.shutdown();
                    socketThread.interrupt();
                    if (syncThread != null && syncThread.isAlive()) {
                        syncThread.interrupt();
                    }
                } catch (Exception e) {
                    logger.error("Error al detener el scheduler", e);
                }
            }));

            startSynchronizationThread();

        } catch (Exception e) {
            logger.error("Error inesperado durante la inicialización", e);
        }
    }

    private void startSynchronizationThread() {
        syncThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000); // Sincronizar cada 5 segundos

                    Set<Integer> currentPorts = socketServer.getMonitoredPorts();

                    for (int port : currentPorts) {
                        if (!previouslySyncedPorts.contains(port)) {
                            ServerMonitorJob.addMonitoredPort(port);
                            previouslySyncedPorts.add(port);
                        }
                    }

                } catch (InterruptedException e) {
                    logger.error("Error en el hilo de sincronización", e);
                }
            }
        });
        syncThread.start();
    }
}