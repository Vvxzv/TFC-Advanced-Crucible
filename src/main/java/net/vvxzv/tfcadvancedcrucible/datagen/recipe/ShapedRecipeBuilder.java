package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.vvxzv.tfcadvancedcrucible.datagen.JsonHelper;

import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder extends RecipeBuilder {
    private final ItemStack result;
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private List<String> patterns;

    public ShapedRecipeBuilder(ItemLike pResult, int pCount) {
        this(new ItemStack(pResult, pCount));
    }

    public ShapedRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public ShapedRecipeBuilder patterns(List<String> patterns) {
        this.patterns = patterns;
        return this;
    }

    public ShapedRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
        return this.define(pSymbol, Ingredient.of(pTag));
    }

    public ShapedRecipeBuilder define(Character pSymbol, ItemLike pItem) {
        return this.define(pSymbol, Ingredient.of(pItem));
    }

    public ShapedRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
        } else if (pSymbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(pSymbol, pIngredient);
            return this;
        }
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        JsonArray jsonarray = new JsonArray();
        for(String s : this.patterns) {
            jsonarray.add(s);
        }
        pJson.add("pattern", jsonarray);

        JsonObject jsonobject = new JsonObject();
        for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
            jsonobject.add(String.valueOf(entry.getKey()), JsonHelper.of(entry.getValue()));
        }
        pJson.add("key", jsonobject);

        pJson.add("result", JsonHelper.of(this.result));

    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SHAPED_RECIPE;
    }
}
