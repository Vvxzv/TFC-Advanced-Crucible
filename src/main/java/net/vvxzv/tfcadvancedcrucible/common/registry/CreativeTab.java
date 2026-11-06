package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCAdvancedCrucible.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TFCAdvancedCrucible_TAB;

    static {
        TFCAdvancedCrucible_TAB = CREATIVE_MODE_TAB.register(
                "tfcadvancedcrucible_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("tfcadvancedcrucible.tab.name"))
                        .icon(() -> new ItemStack(ModBlocks.ADVANCED_CRUCIBLE.get()))
                        .displayItems((pParameters, pOutput) -> {
                            ModItems.ITEMS.getEntries().forEach(item -> {
                                pOutput.accept(item.get());
                            });
                        })
                        .build()
        );
    }

}
