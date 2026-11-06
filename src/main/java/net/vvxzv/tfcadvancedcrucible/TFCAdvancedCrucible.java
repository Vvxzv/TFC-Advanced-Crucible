package net.vvxzv.tfcadvancedcrucible;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.tfcadvancedcrucible.common.registry.*;
import net.vvxzv.tfcadvancedcrucible.network.PacketHandler;

@Mod(TFCAdvancedCrucible.MODID)
public class TFCAdvancedCrucible {
    public static final String MODID = "tfcadvancedcrucible";

    @SuppressWarnings("removal")
    public TFCAdvancedCrucible() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ContainerTypes.CONTAINERS.register(modEventBus);
        CreativeTab.CREATIVE_MODE_TAB.register(modEventBus);

        ForgeEventHandler.init();
        PacketHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
