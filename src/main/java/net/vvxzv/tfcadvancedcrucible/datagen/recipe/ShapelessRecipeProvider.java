package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ShapelessRecipeProvider extends RecipeProvider  {
    public ShapelessRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        createRecipe(
                pWriter,
                shapeless(ModItems.UNFIRED_LARGE_CRUCIBLE.get())
                        .addIngredients(TFCItems.UNFIRED_CRUCIBLE.get(), 3)
                        .id("tfcadvancedcrucible:unfired_large_crucible")
        );
    }

    @Override
    public @NotNull String getName() {
        return "recipes/shapeless";
    }

    public ShapelessRecipeBuilder shapeless(ItemLike pResult) {
        return shapeless(pResult, 1);
    }

    public ShapelessRecipeBuilder shapeless(ItemLike pResult, int pCount) {
        return new ShapelessRecipeBuilder(pResult, pCount);
    }
}
