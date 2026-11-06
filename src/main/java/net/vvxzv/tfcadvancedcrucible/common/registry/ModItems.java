package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCAdvancedCrucible.MODID);

    public static final RegistryObject<Item> UNFIRED_LARGE_CRUCIBLE = ITEMS.register("unfired_large_crucible", () -> new Item(new Item.Properties()));
}
