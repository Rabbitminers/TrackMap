package com.rabbitminers.trackmap.forge;

import com.rabbitminers.trackmap.TrackMap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TrackMap.MOD_ID)
public class TrackMapForge {
    public TrackMapForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TrackMap.init();
    }
}
