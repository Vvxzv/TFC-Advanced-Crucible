package net.vvxzv.tfcadvancedcrucible.datagen;

import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import net.vvxzv.tfcadvancedcrucible.common.utils.TagKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, TFCAdvancedCrucible.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(TagKeys.Items.ADJUSTMENT_TOOLS).addOptionalTag(TFCTags.Items.TOOLS_HAMMER);

        this.tag(Tags.Items.BARRELS).add(ModBlocks.FIRE_BARREL.get().asItem());
    }
}
