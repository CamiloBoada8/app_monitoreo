package org.example.Monitoreo;

import org.example.Util.LoggerUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerMonitorJob implements Job {

    private final ServerHealthChecker healthChecker;
    private static final Logger logger = LoggerUtil.getLogger(ServerMonitorJob.class);

    public ServerMonitorJob() {
        this.healthChecker = new ServerHealthChecker("localhost", 12345);
    }

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("Monitoreando el servidor...");

        int maxAttempts = 3;
        boolean serverUp = false;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (healthChecker.isServerUp()) {
                logger.info("El servidor esta funcionando correctamente.");
                serverUp = true;
                break;
            } else {
                logger.warn("Intento " + attempt + ": El servidor no esta respondiendo.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.error("Error durante la espera entre intentos: " + e.getMessage());
                }
            }
        }

        if (!serverUp) {
            logger.warn("El servidor no respondio despues de " + maxAttempts + " intentos. Intentando levantarlo...");
            restartServer();
        }
    }

    private void restartServer() {
        try {
            String scriptPath = "C:/Users/camil/Documents/sistemas_distribuidos/bancoTCP-IP/servidror-tcp/scripts/restart_server.bat";

            Process process = Runtime.getRuntime().exec(scriptPath);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
            System.out.println("Servidor reiniciado.");
        } catch (Exception e) {
            System.err.println("Error al reiniciar el servidor: " + e.getMessage());
        }
    }
}