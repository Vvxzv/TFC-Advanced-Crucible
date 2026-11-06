package net.vvxzv.tfcadvancedcrucible.datagen.tfcdata;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class TFCHeatDataBuilder implements FinishedTFCData {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final float heat_capacity;
    @Nullable
    private final Float forging_temperature;
    @Nullable
    private final Float welding_temperature;

    public TFCHeatDataBuilder(ResourceLocation id, Ingredient ingredient, float heat_capacity, @Nullable Float forging_temperature,  @Nullable Float welding_temperature) {
        this.id = id;
        this.ingredient = ingredient;
        this.heat_capacity = heat_capacity;
        this.forging_temperature = forging_temperature;
        this.welding_temperature = welding_temperature;
    }

    public TFCHeatDataBuilder(ResourceLocation id, Ingredient ingredient, float heat_capacity) {
        this(id, ingredient, heat_capacity, null, null);
    }

    @Override
    public void serializeData(JsonObject pJson) {
        pJson.add("ingredient", this.ingredient.toJson());
        pJson.addProperty("heat_capacity", this.heat_capacity);
        if(this.forging_temperature != null) {
            pJson.addProperty("forging_temperature", this.forging_temperature);
        }
        if(this.welding_temperature != null) {
            pJson.addProperty("welding_temperature", this.welding_temperature);
        }
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
