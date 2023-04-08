package com.rabbitminers.trackmap.http.routes;

import com.google.gson.JsonArray;
import com.rabbitminers.trackmap.http.HttpServer;
import com.rabbitminers.trackmap.tracks.TrackGraphSerializer;

public class NetworkRoutes {
    public static HttpServer.HttpHandler allNetworks = (out, method, params) -> {
        if (method.equals("GET")) {
            JsonArray data = TrackGraphSerializer.getAllNetworks();
            String response = data.toString();
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
            out.write("Content-Type: application/json\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.write(response.getBytes());
        } else {
            out.write("HTTP/1.1 405 Method Not Allowed\r\n".getBytes());
            out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        }
    };

    public static void init(HttpServer server) {
        server.addHandler("/networks", NetworkRoutes.allNetworks);
    }
}
