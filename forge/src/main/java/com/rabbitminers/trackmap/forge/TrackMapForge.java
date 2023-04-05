package com.rabbitminers.trackmap.forge;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.events.TrackMapCommonEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(TrackMap.MOD_ID)
public class TrackMapForge {
    public TrackMapForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TrackMap.init();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        LevelAccessor level = event.getWorld();
        TrackMapCommonEvents.onWorldLoad(level);
    }
}
