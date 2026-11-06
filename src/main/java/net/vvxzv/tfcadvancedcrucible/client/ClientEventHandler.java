package net.vvxzv.tfcadvancedcrucible.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.client.screen.AdvancedCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.client.screen.LargeAdvancedCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.client.screen.LargeCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.common.registry.ContainerTypes;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCAdvancedCrucible.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIRE_BARREL.get(), RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ContainerTypes.ADVANCED_CRUCIBLE.get(), AdvancedCrucibleScreen::new);
        event.register(ContainerTypes.LARGE_CRUCIBLE.get(), LargeCrucibleScreen::new);
        event.register(ContainerTypes.LARGE_ADVANCED_CRUCIBLE.get(), LargeAdvancedCrucibleScreen::new);
    }
}
