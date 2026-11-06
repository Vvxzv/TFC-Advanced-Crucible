package net.vvxzv.tfcadvancedcrucible;

import net.dries007.tfc.common.capabilities.BlockCapabilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AbstractCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.FireBarrelBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.registry.*;
import net.vvxzv.tfcadvancedcrucible.network.PacketHandler;

@Mod(TFCAdvancedCrucible.MODID)
public class TFCAdvancedCrucible {
    public static final String MODID = "tfcadvancedcrucible";

    public TFCAdvancedCrucible(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ContainerTypes.CONTAINERS.register(modEventBus);
        CreativeTab.CREATIVE_MODE_TAB.register(modEventBus);

        modEventBus.addListener(PacketHandler::setup);
        modEventBus.addListener(this::registerCapabilities);
        NeoForgeEventHandler.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntities.ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getItemInventory
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntities.ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getSidedFluidInventory
        );
        event.registerBlockEntity(
                BlockCapabilities.HEAT,
                BlockEntities.ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getHeatInventory
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntities.LARGE_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getItemInventory
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntities.LARGE_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getSidedFluidInventory
        );
        event.registerBlockEntity(
                BlockCapabilities.HEAT,
                BlockEntities.LARGE_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getHeatInventory
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntities.LARGE_ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getItemInventory
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntities.LARGE_ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getSidedFluidInventory
        );
        event.registerBlockEntity(
                BlockCapabilities.HEAT,
                BlockEntities.LARGE_ADVANCED_CRUCIBLE.get(),
                AbstractCrucibleBlockEntity::getHeatInventory
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntities.BARREL.get(),
                FireBarrelBlockEntity::getInventoryHandler
        );
    }
}
