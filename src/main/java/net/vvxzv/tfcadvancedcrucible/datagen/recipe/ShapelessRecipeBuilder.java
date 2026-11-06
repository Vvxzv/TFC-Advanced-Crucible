package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class ShapelessRecipeBuilder extends RecipeBuilder {
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();

    public ShapelessRecipeBuilder(ItemLike result, int count) {
        this.result = result.asItem();
        this.count = count;
    }

    public ShapelessRecipeBuilder addIngredient(ItemLike pItem) {
        return this.addIngredients(pItem, 1);
    }

    public ShapelessRecipeBuilder addIngredient(TagKey<Item> pTag) {
        return this.addIngredients(pTag, 1);
    }

    public ShapelessRecipeBuilder addIngredients(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; i++) {
            this.ingredients.add(Ingredient.of(pItem));
        }
        return this;
    }

    public ShapelessRecipeBuilder addIngredients(TagKey<Item> pTag, int pQuantity) {
        for (int i = 0; i < pQuantity; i++) {
            this.ingredients.add(Ingredient.of(pTag));
        }
        return this;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        JsonArray jsonarray = new JsonArray();

        for(Ingredient ingredient : this.ingredients) {
            jsonarray.add(ingredient.toJson());
        }

        pJson.add("ingredients", jsonarray);
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
        if (this.count > 1) {
            jsonobject.addProperty("count", this.count);
        }

        pJson.add("result", jsonobject);

    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }
}
