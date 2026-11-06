package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class HeatingRecipeBuilder extends RecipeBuilder {
    private final Ingredient ingredient;
    private ItemStackProvider outputItem = ItemStackProvider.empty();
    private FluidStack outputFluid = FluidStack.EMPTY;
    private final float temperature;
    private boolean useDurability;

    public HeatingRecipeBuilder(Ingredient ingredient, float temperature) {
        this.ingredient = ingredient;
        this.temperature = temperature;
        this.useDurability = false;
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike) {
        return this.outputItem(itemLike, 1);
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike, int count) {
        return this.outputItem(itemLike, count, List.of());
    }

    public HeatingRecipeBuilder outputItem(ItemLike itemLike, int count, List<ItemStackModifier> modifiers) {
        return this.outputItem(new ItemStack(itemLike, count), modifiers);
    }

    public HeatingRecipeBuilder outputItem(ItemStack stack, List<ItemStackModifier> modifiers) {
        this.outputItem = ItemStackProvider.of(stack, modifiers);
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

    @Override
    public void serializeRecipeData(JsonObject pJson) {
    }

    public JsonObject serializeRecipe() {
        HeatingRecipe recipe = new HeatingRecipe(this.ingredient, this.outputItem, this.outputFluid, this.temperature, this.useDurability);
        JsonObject jsonObject = HeatingRecipe.CODEC.codec().encodeStart(JsonOps.INSTANCE, recipe).getOrThrow().getAsJsonObject();
        return this.addRecipeType(jsonObject);
    }

    @Override
    public RecipeSerializer<?> getType() {
        return TFCRecipeSerializers.HEATING.get();
    }
}
