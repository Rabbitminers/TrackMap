package com.rabbitminers.trackmap;

import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod {
    public static final String MOD_ID = "trackmap";
    public static final String NAME = "Track Map";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);


    public static void init() {
        ExampleBlocks.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
