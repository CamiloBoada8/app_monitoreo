package org.example;

import org.example.Monitoreo.ServerMonitorScheduler;
import org.example.Util.Configuracion;
import org.quartz.SchedulerException;

public class Main {
    public static void main(String[] args) throws SchedulerException {
        Configuracion.init();

        ServerMonitorScheduler.main(args);
    }
}