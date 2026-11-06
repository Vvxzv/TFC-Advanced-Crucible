package net.vvxzv.tfcadvancedcrucible.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.client.screen.AdvancedCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.client.screen.LargeAdvancedCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.client.screen.LargeCrucibleScreen;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import net.vvxzv.tfcadvancedcrucible.common.registry.ContainerTypes;

@Mod.EventBusSubscriber(modid = TFCAdvancedCrucible.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ContainerTypes.ADVANCED_CRUCIBLE.get(), AdvancedCrucibleScreen::new);
            MenuScreens.register(ContainerTypes.LARGE_CRUCIBLE.get(), LargeCrucibleScreen::new);
            MenuScreens.register(ContainerTypes.LARGE_ADVANCED_CRUCIBLE.get(), LargeAdvancedCrucibleScreen::new);

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIRE_BARREL.get(), RenderType.cutout());
        });
    }
}
