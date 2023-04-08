package com.rabbitminers.trackmap.http.routes;

import com.rabbitminers.trackmap.http.HttpServer;
import com.rabbitminers.trackmap.http.util.HttpHelper;

import java.io.InputStream;

public class SiteRoutes {
    public static HttpServer.HttpHandler home = (out, method, params) -> {
        if (method.equals("GET")) {
            InputStream fileStream = SiteRoutes.class.getClassLoader().getResourceAsStream("web/index.html");
            if (fileStream == null) {
                HttpHelper.writeFileNotFound(out);
                return;
            }
            byte[] fileBytes = fileStream.readAllBytes();
            fileStream.close();

            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
            out.write("Content-Type: text/html\r\n".getBytes());
            out.write(("Content-Length: " + fileBytes.length + "\r\n").getBytes());
            out.write("\r\n".getBytes());

            out.write(fileBytes);
        } else {
            out.write("HTTP/1.1 405 Method Not Allowed\r\n".getBytes());
            out.write("Access-Control-Allow-Origin: *\r\n".getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        }
    };

    public static void init(HttpServer server) {
        server.addHandler("/", SiteRoutes.home);
        server.addHandler("/index", SiteRoutes.home);
    }
}
