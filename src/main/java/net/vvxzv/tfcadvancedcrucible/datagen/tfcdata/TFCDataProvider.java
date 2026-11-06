package net.vvxzv.tfcadvancedcrucible.datagen.tfcdata;

import com.google.common.collect.Sets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class TFCDataProvider implements DataProvider {
    protected final PackOutput.PathProvider dataPath;
    protected final TFCDataTypes type;

    public TFCDataProvider(PackOutput pOutput, TFCDataTypes type) {
        this.dataPath = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/" + type.getDataName());
        this.type = type;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildData(data -> {
            if (!set.add(data.getId())) {
                throw new IllegalStateException("Duplicate data " + data.getId());
            } else {
                list.add(DataProvider.saveStable(pOutput, data.serialize(), this.dataPath.json(data.getId())));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull String getName() {
        return type.getDataName();
    };

    protected abstract void buildData(Consumer<FinishedTFCData> pWriter);

    public enum TFCDataTypes {
        CLIMATE("climate_range"),
        DEPOSIT("deposit"),
        DRINK("drinkable"),
        ENTITY_DAMAGE("entity_damage_resistance"),
        FAUNA("fauna"),
        FERTILIZER("fertilizer"),
        FLUID_HEAT("fluid_heat"),
        FOOD("food"),
        FUEL("fuel"),
        ITEM_DAMAGE("item_damage_resistance"),
        HEAT("item_heat"),
        SIZE("item_size"),
        KNAPPING("knapping_type"),
        LAMP("lamp_fuel"),
        SUPPORTS("support");

        private final String dataName;

        TFCDataTypes(String dataName) {
            this.dataName = dataName;
        }

        public String getDataName() {
            return this.dataName;
        }
    }
}
