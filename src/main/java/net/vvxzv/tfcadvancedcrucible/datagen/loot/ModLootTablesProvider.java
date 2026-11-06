package net.vvxzv.tfcadvancedcrucible.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTablesProvider extends LootTableProvider {
    private static final List<SubProviderEntry> SUB_PROVIDERS = List.of(
            new SubProviderEntry(
                    ModBlockLootTablesProvider::new,
                    LootContextParamSets.BLOCK
            )
    );

    public ModLootTablesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, Collections.emptySet(), SUB_PROVIDERS, registries);
    }
}
