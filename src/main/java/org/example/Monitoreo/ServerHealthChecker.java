package org.example.Monitoreo;

import java.io.IOException;
import java.net.Socket;

public class ServerHealthChecker {

    private final String serverHost;
    private final int serverPort;

    public ServerHealthChecker(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public boolean isServerUp() {
        try (Socket socket = new Socket(serverHost, serverPort)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}