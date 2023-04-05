package com.rabbitminers.trackmap.fabric;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.fabric.events.TrackMapEventsFabric;
import net.fabricmc.api.ModInitializer;

public class TrackMapFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TrackMap.init();
        TrackMapEventsFabric.register();
    }

}
