package com.rabbitminers.trackmap.fabric;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import com.rabbitminers.trackmap.ExampleBlocks;
import com.rabbitminers.trackmap.ExampleMod;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
        ExampleMod.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), ExampleMod.NAME);
        // on fabric, Registrates must be explicitly finalized and registered.
        ExampleBlocks.REGISTRATE.register();
    }
}
