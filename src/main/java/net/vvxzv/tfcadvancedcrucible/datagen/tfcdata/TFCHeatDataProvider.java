package net.vvxzv.tfcadvancedcrucible.datagen.tfcdata;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TFCHeatDataProvider extends TFCDataProvider {
    public TFCHeatDataProvider(PackOutput pOutput) {
        super(pOutput, TFCDataTypes.HEAT);
    }

    @Override
    protected void buildData(Consumer<FinishedTFCData> pWriter) {
        heat(
                pWriter,
                TFCAdvancedCrucible.MODID + ":" + "unfired_large_crucible",
                Ingredient.of(ModItems.UNFIRED_LARGE_CRUCIBLE.get()), 1
        );
    }

    public void heat(Consumer<FinishedTFCData> pWriter, String id, Ingredient ingredient, float heat_capacity) {
        heat(pWriter, ResourceLocation.parse(id), ingredient, heat_capacity);
    }

    public void heat(Consumer<FinishedTFCData> pWriter, String id, Ingredient ingredient, float heat_capacity, @Nullable Float forging_temperature, @Nullable Float welding_temperature) {
        heat(pWriter, ResourceLocation.parse(id), ingredient, heat_capacity, forging_temperature, welding_temperature);
    }

    public void heat(Consumer<FinishedTFCData> pWriter, ResourceLocation id, Ingredient ingredient, float heat_capacity) {
        pWriter.accept(new TFCHeatDataBuilder(id, ingredient, heat_capacity));
    }

    public void heat(Consumer<FinishedTFCData> pWriter, ResourceLocation id, Ingredient ingredient, float heat_capacity, @Nullable Float forging_temperature, @Nullable Float welding_temperature) {
        pWriter.accept(new TFCHeatDataBuilder(id, ingredient, heat_capacity, forging_temperature, welding_temperature));
    }
}
