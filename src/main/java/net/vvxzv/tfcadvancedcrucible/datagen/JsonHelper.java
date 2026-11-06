package net.vvxzv.tfcadvancedcrucible.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public class JsonHelper {
    public static JsonElement of(FluidStack fluidStack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, fluidStack).getOrThrow();
    }

    public static JsonElement of(ItemStack stack) {
        return ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getOrThrow();
    }

    public static JsonElement of(Ingredient ingredient) {
        return Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).getOrThrow();
    }
}
