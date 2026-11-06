package net.vvxzv.tfcadvancedcrucible.datagen;

import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import net.vvxzv.tfcadvancedcrucible.common.utils.TagKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TFCAdvancedCrucible.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(TFCTags.Blocks.FORGE_INVISIBLE_WHITELIST)
                .add(ModBlocks.ADVANCED_CRUCIBLE.get())
                .add(ModBlocks.LARGE_CRUCIBLE.get())
                .add(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ADVANCED_CRUCIBLE.get())
                .add(ModBlocks.FIRE_BARREL.get())
                .add(ModBlocks.LARGE_CRUCIBLE.get())
                .add(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.ADVANCED_CRUCIBLE.get())
                .add(ModBlocks.FIRE_BARREL.get())
                .add(ModBlocks.LARGE_CRUCIBLE.get())
                .add(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get());

        this.tag(TagKeys.Blocks.CRUCIBLE)
                .add(ModBlocks.ADVANCED_CRUCIBLE.get())
                .add(ModBlocks.LARGE_CRUCIBLE.get())
                .add(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get());
    }
}
