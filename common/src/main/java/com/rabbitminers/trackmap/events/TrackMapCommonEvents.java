package com.rabbitminers.trackmap.events;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.HttpServer;
import com.rabbitminers.trackmap.http.TrackMapServer;
import com.rabbitminers.trackmap.http.routes.*;
import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.util.UUID;

public class TrackMapCommonEvents {
    public static void onWorldLoad(LevelAccessor levelAccessor) {
        try {
            HttpServer server = new HttpServer(8080);
            ConnectionRoutes.init(server);
            NodeRoutes.init(server);
            SiteRoutes.init(server);
            NetworkRoutes.init(server);
            TrainRoutes.init(server);
            server.start();
        } catch (IOException e) {
            TrackMap.LOGGER.error("Failed to start server " + e);
        }
    }
}
