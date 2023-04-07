package com.rabbitminers.trackmap.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.rabbitminers.trackmap.TrackMap.LOGGER;

public class HttpServer {
    private final ServerSocket serverSocket;
    private final Map<String, HttpHandler> handlers;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
        handlers = new HashMap<>();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleRequest(clientSocket)).start();
                } catch (IOException e) {
                    LOGGER.error("Failed to start webserver " + e);
                }
            }
        }).start();
    }

    private void handleRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String requestLine = reader.readLine();
            if (requestLine == null) {
                sendErrorResponse(clientSocket.getOutputStream(), "400 Bad Request", "Request line is empty");
                return;
            }
            String[] requestLineParts = requestLine.split(" ");
            if (requestLineParts.length != 3) {
                sendErrorResponse(clientSocket.getOutputStream(), "400 Bad Request", "Invalid request line");
                return;
            }
            String method = requestLineParts[0];
            String path = requestLineParts[1];
            String[] pathParts = path.split("/");
            if (pathParts.length < 2) {
                sendErrorResponse(clientSocket.getOutputStream(), "400 Bad Request", "Invalid request path");
                return;
            }

            HttpHandler handler = handlers.get(pathParts[1]);

            System.out.println(path);
            handlers.forEach((name, h) -> System.out.println(name));

            if (handler == null) {
                sendErrorResponse(clientSocket.getOutputStream(), "404 Not Found", "No handler found for path: " + path);
                return;
            }
            Map<String, String> pathParams = getPathParams(pathParts);
            handler.handleRequest(clientSocket.getOutputStream(), method, pathParams);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close socket " + e);
            }
        }
    }

    private Map<String, String> getPathParams(String[] pathParts) {
        Map<String, String> pathParams = new HashMap<>();
        for (int i = 2; i < pathParts.length; i++) {
            String pathPart = pathParts[i];
            if (pathPart.startsWith("{") && pathPart.endsWith("}")) {
                String pathParamName = pathPart.substring(1, pathPart.length() - 1);
                pathParams.put(pathParamName, pathParts[i + 1]);
            }
        }
        return pathParams;
    }

    private void sendErrorResponse(OutputStream outputStream, String status, String message) throws IOException {
        String response = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n" +
                "\r\n" +
                message;
        outputStream.write(response.getBytes());
        outputStream.flush();
    }

    public void addHandler(String path, HttpHandler handler) {
        handlers.put(path, handler);
    }

    @FunctionalInterface
    public interface HttpHandler {
        void handleRequest(OutputStream outputStream, String method, Map<String, String> pathParams) throws IOException;
    }
}
