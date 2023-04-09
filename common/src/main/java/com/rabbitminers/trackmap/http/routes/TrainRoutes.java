package com.rabbitminers.trackmap.http.routes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rabbitminers.trackmap.http.HttpServer;
import com.rabbitminers.trackmap.tracks.TrackGraphSerializer;

import java.util.UUID;

public class TrainRoutes {
    public static HttpServer.HttpHandler allTrains = (out, method, params) -> {
        if (method.equals("GET")) {
            JsonArray trains = TrackGraphSerializer.serializeTrains();
            String response = trains.toString();
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

    public static HttpServer.HttpHandler specificTrain = (out, method, params) -> {
        if (method.equals("GET")) {
            UUID trainId;
            try {
                String pathVar = params.get("var1");
                trainId = UUID.fromString(pathVar);
            } catch (RuntimeException e) {
                out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.flush();
                return;
            }
            JsonObject data = TrackGraphSerializer.serializeTrain(trainId);
            if (data == null) {
                out.write("HTTP/1.1 418 I'm a teapot\r\n".getBytes());
                out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
                out.write("\r\n".getBytes());
                out.flush();
                return;
            }
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
        server.addHandler("/trains", TrainRoutes.allTrains);
        server.addHandler("/trains/{uuid}", TrainRoutes.specificTrain);
    }
}
