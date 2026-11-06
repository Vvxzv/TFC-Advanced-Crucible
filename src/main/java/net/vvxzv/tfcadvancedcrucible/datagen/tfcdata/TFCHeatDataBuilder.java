package net.vvxzv.tfcadvancedcrucible.datagen.tfcdata;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class TFCHeatDataBuilder implements FinishedTFCData {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final float heat_capacity;
    private final float forging_temperature;
    private final float welding_temperature;

    public TFCHeatDataBuilder(ResourceLocation id, Ingredient ingredient, float heat_capacity, float forging_temperature, float welding_temperature) {
        this.id = id;
        this.ingredient = ingredient;
        this.heat_capacity = heat_capacity;
        this.forging_temperature = forging_temperature;
        this.welding_temperature = welding_temperature;
    }

    public TFCHeatDataBuilder(ResourceLocation id, Ingredient ingredient, float heat_capacity) {
        this(id, ingredient, heat_capacity, 0, 0);
    }

    @Override
    public void serializeData(JsonObject pJson) {
    }

    @Override
    public JsonObject serialize(){
        HeatDefinition heatDefinition = new HeatDefinition(this.ingredient, this.heat_capacity, this.forging_temperature, this.welding_temperature);
        return HeatDefinition.CODEC.encodeStart(JsonOps.INSTANCE, heatDefinition).getOrThrow().getAsJsonObject();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
