package com.rabbitminers.trackmap.forge;

import com.rabbitminers.trackmap.ExampleMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MOD_ID)
public class ExampleModForge {
    public ExampleModForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ExampleMod.init();
    }
}
