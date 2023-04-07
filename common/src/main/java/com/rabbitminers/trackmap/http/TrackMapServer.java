package com.rabbitminers.trackmap.http;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.routes.base.HttpHandler;
import com.rabbitminers.trackmap.http.util.HttpHelper;
import com.rabbitminers.trackmap.http.util.HttpMethod;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Deprecated
public class TrackMapServer implements Closeable {
    private static final Map<String, HttpHandler> routes = new HashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public boolean isOpen = true;
    final int port;
    final ServerSocket serverSocket;

    public static Thread newInstance(int port) {
        return new Thread(() -> {
            try {
                TrackMapServer server = new TrackMapServer(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public TrackMapServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        TrackMap.LOGGER.info("Server running on port " + port);

        while (this.isOpen) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                TrackMap.LOGGER.info("Client connected: " + clientSocket.getInetAddress());

                this.executorService.submit(() -> handleConnection(clientSocket));
            } catch (IOException e) {
                TrackMap.LOGGER.info("Error accepting client connection: " + e.getMessage());
            }
        }

        serverSocket.close();
        TrackMap.LOGGER.info("Server stopped");
    }

    private static void handleConnection(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();

            String request = in.readLine();
            if (request == null)
                return;
            TrackMap.LOGGER.info("Request: " + request);
            String[] requestParts = request.split(" ");
            if (requestParts.length < 2)
                return;
            HttpMethod method = HttpMethod.fromString(requestParts[0]);
            if (method == null) {
                HttpHelper.writeMethodNotAllowed(out);
                return;
            }
            String fullPath = requestParts[1];
            String path = fullPath;

            HttpHandler handler = routes.get(fullPath);

            if (handler != null) {
                handler.handle(out, "", method);
            } else {
                HttpHelper.writeFileNotFound(out);
            }

            TrackMap.LOGGER.info("Response sent");
        } catch (IOException e) {
            TrackMap.LOGGER.error("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                TrackMap.LOGGER.info("Client disconnected");
            } catch (IOException e) {
                TrackMap.LOGGER.error("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public static void addRoutes(HttpHandler... handlers) {
        Arrays.stream(handlers).forEach(TrackMapServer::addRoute);
    }

    public static void addRoute(HttpHandler httpHandler) {
        routes.put(httpHandler.getPath(), httpHandler);
    }

    public static Collection<HttpHandler> getRoutes() {
        return routes.values();
    }

    @Override
    public void close() throws IOException {
        this.isOpen = false;
        TrackMap.LOGGER.info("Ending Webserver");
    }
}
