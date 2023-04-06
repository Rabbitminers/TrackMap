package com.rabbitminers.trackmap;

import com.rabbitminers.trackmap.http.routes.ConnectionsRoute;
import com.rabbitminers.trackmap.http.routes.HomeRoute;
import com.rabbitminers.trackmap.http.TrackMapServer;
import com.rabbitminers.trackmap.http.routes.NodeRoute;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackMap {
    public static Thread server;
    public static final String MOD_ID = "trackmap";
    public static final String NAME = "Track Map";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);


    public static void init() {
        TrackMapServer.addRoutes(new HomeRoute(), new ConnectionsRoute(), new NodeRoute());
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
