package org.example.Monitoreo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ServerMonitorScheduler {

    public static void main(String[] args) throws SchedulerException {
        String serverHost = "localhost";
        int serverPort = 12345;

        JobDetail job = JobBuilder.newJob(ServerMonitorJob.class)
                .withIdentity("serverMonitorJob", "monitorGroup")
                .usingJobData("serverHost", serverHost)
                .usingJobData("serverPort", serverPort)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("serverMonitorTrigger", "monitorGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5) //realizar monitoreo cada 30 seg
                        .repeatForever())
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        scheduler.scheduleJob(job, trigger);

        System.out.println("Monitoreo del servidor iniciado. Presiona Ctrl+C para detener.");
    }
}