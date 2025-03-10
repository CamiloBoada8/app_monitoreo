package org.example.Monitoreo;

import org.example.Util.LoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class SocketServer implements Runnable {
    private final int port;
    private final Set<Integer> monitoredPorts = ConcurrentHashMap.newKeySet();

    public SocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LoggerUtil.logInfo(SocketServer.class, "Servidor de sockets escuchando en puerto: " + port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            LoggerUtil.logError(SocketServer.class, "Error en el servidor de sockets", e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                LoggerUtil.logInfo(SocketServer.class, "Mensaje recibido del cliente: " + inputLine);

                if (inputLine.startsWith("NEW_SERVER ")) {
                    String[] parts = inputLine.split(" ");
                    if (parts.length == 2) {
                        try {
                            int newPort = Integer.parseInt(parts[1]);
                            if (newPort > 0 && newPort <= 65535) {
                                synchronized (monitoredPorts) {
                                    if (monitoredPorts.add(newPort)) {
                                        LoggerUtil.logInfo(SocketServer.class, "Nuevo puerto registrado para monitoreo: " + newPort);
                                        out.println("SERVER_REGISTERED");
                                    } else {
                                        LoggerUtil.logWarn(SocketServer.class, "Puerto ya registrado: " + newPort);
                                        out.println("ERROR: Puerto ya registrado.");
                                    }
                                }
                            } else {
                                LoggerUtil.logWarn(SocketServer.class, "Puerto inválido recibido: " + parts[1]);
                                out.println("ERROR: Puerto inválido.");
                            }
                        } catch (NumberFormatException e) {
                            LoggerUtil.logWarn(SocketServer.class, "Formato de puerto inválido: " + parts[1]);
                            out.println("ERROR: Formato de puerto inválido.");
                        }
                    } else {
                        LoggerUtil.logWarn(SocketServer.class, "Comando NEW_SERVER malformado: " + inputLine);
                        out.println("ERROR: Comando NEW_SERVER malformado.");
                    }
                } else {
                    out.println("Monitoreo recibio: " + inputLine);
                }
            }
        } catch (IOException e) {
            LoggerUtil.logError(SocketServer.class, "Error manejando cliente", e);
        }
    }

    public Set<Integer> getMonitoredPorts() {
        return new HashSet<>(monitoredPorts);
    }

    /**
     * Elimina un puerto del conjunto de puertos monitoreados.
     */
    public void removeMonitoredPort(int port) {
        synchronized (monitoredPorts) {
            if (monitoredPorts.remove(port)) {
                LoggerUtil.logInfo(SocketServer.class, "Puerto eliminado del monitoreo: " + port);
            } else {
                LoggerUtil.logWarn(SocketServer.class, "Intento de eliminar un puerto no registrado: " + port);
            }
        }
    }
}