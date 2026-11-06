package net.vvxzv.tfcadvancedcrucible.datagen.loot;

import net.dries007.tfc.common.component.TFCComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {

    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        crucibleLoot(ModBlocks.ADVANCED_CRUCIBLE.get());
        crucibleLoot(ModBlocks.LARGE_CRUCIBLE.get());
        crucibleLoot(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get());

        this.dropSelf(ModBlocks.FIRE_BARREL.get());
    }

    private void crucibleLoot(Block pBlock) {
        this.add(pBlock, block ->
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .name("loot_pool")
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(block.asItem())
                                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                .include(DataComponents.CUSTOM_NAME)
                                                .include(TFCComponents.CRUCIBLE.get())
                                )
                                .when(ExplosionCondition.survivesExplosion())
                        )
                )
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
    }
}
