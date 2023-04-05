package com.rabbitminers.trackmap.http.util;

import com.rabbitminers.trackmap.TrackMap;

import java.io.IOException;
import java.io.OutputStream;

public class HttpHelper {
    public static void writeToOutputStream(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            TrackMap.LOGGER.error("Failed to write to socket");
        }
    }
    
    public static void writeFileNotFound(OutputStream outputStream) {
        HttpHelper.writeToOutputStream(outputStream, "HTTP/1.1 404 Not Found\r\n\r\n");
    }

    public static void writeMethodNotAllowed(OutputStream outputStream) {
        HttpHelper.writeToOutputStream(outputStream, "HTTP/1.1 405 Method Not Allowed\r\n\r\n");
    }
}
