package com.rabbitminers.trackmap.events;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.TrackMapServer;
import net.minecraft.world.level.LevelAccessor;

public class TrackMapCommonEvents {
    public static void onWorldLoad(LevelAccessor levelAccessor) {
        TrackMap.server = TrackMapServer.newInstance(8080);
        TrackMap.server.start();
    }
}
