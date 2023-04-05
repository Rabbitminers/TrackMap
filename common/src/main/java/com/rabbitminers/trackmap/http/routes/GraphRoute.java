package com.rabbitminers.trackmap.http.routes;

import com.google.gson.JsonArray;
import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.routes.base.HttpHandler;
import com.rabbitminers.trackmap.http.util.HttpHelper;
import com.rabbitminers.trackmap.http.util.HttpMethod;
import com.rabbitminers.trackmap.tracks.TrackGraphSerializer;

import java.io.IOException;
import java.io.OutputStream;

public class GraphRoute extends HttpHandler {
    public GraphRoute() {
        this.handlers.put(HttpMethod.GET, this::handleGet);
    }

    public void handleGet(OutputStream out) {
        try {
            JsonArray data = TrackGraphSerializer.serializeNetworks();
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-Type: application/json\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.write(data.toString().getBytes());
        } catch (IOException e) {
            TrackMap.LOGGER.error("Error handling client connection: " + e.getMessage());
        }
    }

    @Override
    public String getPath() {
        return "/path/";
    }
}
