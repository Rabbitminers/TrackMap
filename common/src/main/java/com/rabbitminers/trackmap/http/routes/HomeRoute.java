package com.rabbitminers.trackmap.http.routes;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.util.HttpHelper;
import com.rabbitminers.trackmap.http.util.HttpMethod;
import com.rabbitminers.trackmap.http.routes.base.HttpHandler;
import com.rabbitminers.trackmap.http.TrackMapServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HomeRoute extends HttpHandler {

    public HomeRoute() {
        this.handlers.put(HttpMethod.GET, this::handleGet);
    }

    public void handleGet(OutputStream out) {
        try {
            InputStream fileStream = TrackMapServer.class.getClassLoader().getResourceAsStream("web/index.html");
            if (fileStream == null) {
                HttpHelper.writeFileNotFound(out);
                return;
            }
            byte[] fileBytes = fileStream.readAllBytes();
            fileStream.close();

            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-Type: text/html\r\n".getBytes());
            out.write(("Content-Length: " + fileBytes.length + "\r\n").getBytes());
            out.write("\r\n".getBytes());

            out.write(fileBytes);
        } catch (IOException e) {
            TrackMap.LOGGER.error("Error handling client connection: " + e.getMessage());
        }
    }

    @Override
    public String getPath() {
        return "/";
    }
}
