package net.vvxzv.tfcadvancedcrucible.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;

public class TagKeys {
    public static class Items {
        public static final TagKey<Item> ADJUSTMENT_TOOLS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, "adjustment_tools"));
    }

    public static class Blocks {
        public static final TagKey<Block> CRUCIBLE = BlockTags.create(ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, "crucible"));
    }
}
