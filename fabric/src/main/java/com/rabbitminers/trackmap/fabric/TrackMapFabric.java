package com.rabbitminers.trackmap.fabric;

import com.rabbitminers.trackmap.TrackMap;
import net.fabricmc.api.ModInitializer;

public class TrackMapFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TrackMap.init();
    }
}
