package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HeatingRecipeProvider extends RecipeProvider {
    public HeatingRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        createRecipe(
                pWriter,
                heating(Ingredient.of(ModItems.UNFIRED_LARGE_CRUCIBLE.get()), 1399)
                        .outputItem(ModBlocks.LARGE_CRUCIBLE.get())
                        .id("tfcadvancedcrucible:heating/unfired_large_crucible")
        );
    }

    @Override
    public @NotNull String getName() {
        return "recipes/heating";
    }

    public HeatingRecipeBuilder heating(Ingredient ingredient, float temperature) {
        return new HeatingRecipeBuilder(ingredient, temperature, null, null, null);
    }
}
