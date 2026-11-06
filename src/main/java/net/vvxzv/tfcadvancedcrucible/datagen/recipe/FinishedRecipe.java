package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface FinishedRecipe {
    void serializeRecipeData(JsonObject pJson);

    default JsonObject serializeRecipe() {
        JsonObject jsonobject = new JsonObject();
        this.serializeRecipeData(this.addRecipeType(jsonobject));
        return jsonobject;
    }

    default JsonObject addRecipeType(JsonObject object) {
        object.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.getType()).toString());
        return object;
    }

    ResourceLocation getId();

    RecipeSerializer<?> getType();
}
