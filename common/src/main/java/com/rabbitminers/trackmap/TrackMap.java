package com.rabbitminers.trackmap;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackMap {
    public static Thread server;
    public static final String MOD_ID = "trackmap";
    public static final String NAME = "Track Map";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);


    public static void init() {

    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
