package net.vvxzv.tfcadvancedcrucible.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JsonHelper {
    public static JsonElement toJson(FluidStack fluidStack) {
        JsonObject o = new JsonObject();
        o.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString());
        o.addProperty("amount", fluidStack.getAmount());
        if (fluidStack.hasTag()) {
            o.addProperty("nbt", fluidStack.getTag().toString());
        }

        return o;
    }

    public static JsonElement toJson(ItemStack stack) {
        JsonObject object = new JsonObject();
        object.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getCount() > 1) {
            object.addProperty("count", stack.getCount());
        }
        if (stack.hasTag()) {
            object.addProperty("nbt", stack.getTag().toString());
        }

        return object;
    }
}
