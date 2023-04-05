package com.rabbitminers.trackmap.events;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.http.TrackMapServer;
import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;

public class TrackMapCommonEvents {
    public static void onWorldLoad(LevelAccessor levelAccessor) {
        new Thread(() -> {
            try {
                new TrackMapServer(8080);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
