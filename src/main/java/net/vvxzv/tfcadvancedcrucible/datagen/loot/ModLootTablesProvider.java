package net.vvxzv.tfcadvancedcrucible.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class ModLootTablesProvider extends LootTableProvider {
    private static final List<SubProviderEntry> SUB_PROVIDERS = List.of(
            new LootTableProvider.SubProviderEntry(
                    ModBlockLootTablesProvider::new,
                    LootContextParamSets.BLOCK
            )
    );

    public ModLootTablesProvider(PackOutput pOutput) {
        super(pOutput, Collections.emptySet(), SUB_PROVIDERS);
    }
}
