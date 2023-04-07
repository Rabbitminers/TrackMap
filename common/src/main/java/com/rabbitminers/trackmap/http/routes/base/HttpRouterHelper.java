package com.rabbitminers.trackmap.http.routes.base;

import com.rabbitminers.trackmap.TrackMap;

import java.io.IOException;
import java.io.OutputStream;

public class HttpRouterHelper {

    public static void writeResponse(OutputStream out, int statusCode, String statusMessage, String body) {
        try {
            out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
            out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(body.getBytes());
            out.flush();
        } catch (IOException e) {
            TrackMap.LOGGER.error("Failed to write response: " + e);
        }
    }

    public static void writeOkResponse(OutputStream out, String response) {
        try {
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write(("Content-Length: " + response.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            TrackMap.LOGGER.error("Failed to write response: " + e);
        }
    }

    public static void writeMethodNotAllowed(OutputStream out) throws IOException {
        try {
            out.write("HTTP/1.1 405 Method Not Allowed\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        } catch (IOException e) {
            TrackMap.LOGGER.error("Failed to write response: " + e);
        }
    }
}
