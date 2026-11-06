package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.vvxzv.tfcadvancedcrucible.datagen.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class HeatingRecipeBuilder extends RecipeBuilder {
    private final Ingredient ingredient;
    private ItemStack outputItem;
    private JsonArray modifiers;
    private FluidStack outputFluid;
    private final float temperature;
    private boolean useDurability;
    private float chance;

    public HeatingRecipeBuilder(Ingredient ingredient, float temperature, @Nullable ItemStack outputItem, @Nullable JsonArray modifiers, @Nullable FluidStack outputFluid) {
        this.ingredient = ingredient;
        this.outputItem = outputItem;
        this.modifiers = modifiers;
        this.outputFluid = outputFluid;
        this.temperature = temperature;
        this.useDurability = false;
        this.chance = 1;
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike) {
        return this.outputItem(itemLike, 1);
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike, int count) {
        return this.outputItem(itemLike, count, null);
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike, int count, @Nullable JsonArray modifiers) {
        return this.outputItem(new ItemStack(itemLike, count), modifiers);
    }

    public HeatingRecipeBuilder outputItem(ItemStack stack, @Nullable JsonArray modifiers) {
        this.outputItem = stack;
        this.modifiers = modifiers;
        return this;
    }

    public HeatingRecipeBuilder outputFluid(Fluid fluid) {
        return this.outputFluid(fluid, 1000);
    }

    public HeatingRecipeBuilder outputFluid(Fluid fluid, int amount) {
        return this.outputFluid(new FluidStack(fluid, amount));
    }

    public HeatingRecipeBuilder outputFluid(FluidStack fluidStack) {
        this.outputFluid = fluidStack;
        return this;
    }

    public HeatingRecipeBuilder useDurability() {
        this.useDurability = true;
        return this;
    }

    public HeatingRecipeBuilder chance(float chance) {
        this.chance = chance;
        return this;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        pJson.add("ingredient", this.ingredient.toJson());

        if(this.outputItem != null) {
            JsonObject resultItem = new JsonObject();
            if (this.modifiers != null) {
                resultItem.add("modifiers", this.modifiers);
            }
            resultItem.add("stack", JsonHelper.toJson(this.outputItem));
            pJson.add("result_item", resultItem);
        }

        if(this.outputFluid != null) {
            pJson.add("result_fluid", JsonHelper.toJson(this.outputFluid));
        }
        pJson.addProperty("temperature", this.temperature);
        if(this.useDurability) {
            pJson.addProperty("use_durability", true);
        }
        if(this.chance > 0 && this.chance < 1) {
            pJson.addProperty("chance", this.chance);
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        return TFCRecipeSerializers.HEATING.get();
    }
}
