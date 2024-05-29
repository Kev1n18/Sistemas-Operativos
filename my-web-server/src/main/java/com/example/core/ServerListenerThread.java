package com.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private final int port;
    private final String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) {
        this.port = port;
        this.webroot = webroot;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server started on port " + port);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    LOGGER.info("Connection accepted: " + socket.getInetAddress());
                    new HttpConnectionWorkerThread(socket, webroot).start();
                } catch (IOException e) {
                    LOGGER.error("Problem accepting connection", e);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not start server on port " + port, e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    LOGGER.error("Error closing server socket", e);
                }
            }
        }
    }
}
