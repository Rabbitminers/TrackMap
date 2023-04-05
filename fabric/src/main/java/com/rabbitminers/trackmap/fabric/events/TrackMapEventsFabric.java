package com.rabbitminers.trackmap.fabric.events;

import com.rabbitminers.trackmap.events.TrackMapCommonEvents;
import com.simibubi.create.events.CommonEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.level.LevelAccessor;

import java.util.concurrent.Executor;

public class TrackMapEventsFabric {
    public static void onLoadWorld(Executor executor, LevelAccessor world) {
        TrackMapCommonEvents.onWorldLoad(world);
    }

    public static void register() {
        ServerWorldEvents.LOAD.register(TrackMapEventsFabric::onLoadWorld);
    }
}
