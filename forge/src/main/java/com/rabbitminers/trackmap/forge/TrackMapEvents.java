package com.rabbitminers.trackmap.forge;

import com.rabbitminers.trackmap.TrackMap;
import com.rabbitminers.trackmap.events.TrackMapCommonEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrackMap.MOD_ID)
public class TrackMapEvents {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        LevelAccessor level = event.getWorld();
        TrackMapCommonEvents.onWorldLoad(level);
    }
}
