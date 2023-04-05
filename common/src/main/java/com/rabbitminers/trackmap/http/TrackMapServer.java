package com.rabbitminers.trackmap.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackMapServer implements Closeable {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public boolean isOpen = true;
    final int port;
    final ServerSocket serverSocket;

    public static Thread newInstance(int port) {
        return new Thread(() -> {
            try {
                new TrackMapServer(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public TrackMapServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server running on port " + port);

        while (this.isOpen) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                this.executorService.submit(() -> handleConnection(clientSocket));
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }

        serverSocket.close();
        System.out.println("Server stopped");
    }

    private static void handleConnection(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();

            String request = in.readLine();
            if (request == null) {
                return;
            }
            System.out.println("Request: " + request);

            String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello, world!";
            out.write(response.getBytes());

            System.out.println("Response sent");
        } catch (IOException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.isOpen = false;
    }
}
