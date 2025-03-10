package org.example.Monitoreo;

import org.example.Util.Configuracion;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMonitorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ServerMonitorScheduler.class);
    private static Scheduler scheduler;

    public static void main(String[] args) {
        try {
            String serverHost = Configuracion.SERVER_HOST;
            int serverPort = Configuracion.SERVER_PORT;

            JobDetail job = JobBuilder.newJob(ServerMonitorJob.class)
                    .withIdentity("serverMonitorJob", "monitorGroup")
                    .usingJobData("serverHost", serverHost)
                    .usingJobData("serverPort", serverPort)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("serverMonitorTrigger", "monitorGroup")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(Configuracion.MONITOR_INTERVAL_SECONDS)
                            .repeatForever())
                    .build();

            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

            logger.info("Monitoreo del servidor iniciado. Presiona Ctrl+C para detener.");

        } catch (SchedulerException e) {
            logger.error("Error al programar el trabajo de monitoreo", e);
        }
    }

    /**
     * Detiene el scheduler de manera controlada.
     */
    public static void shutdown() throws SchedulerException {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(true);
            logger.info("Scheduler detenido correctamente.");
        }
    }
}