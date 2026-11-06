package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TFCAdvancedCrucible.MODID);

    public static final DeferredHolder<Item, Item> UNFIRED_LARGE_CRUCIBLE = ITEMS.register("unfired_large_crucible", () -> new Item(new Item.Properties()));
}
