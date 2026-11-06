package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ShapedRecipeProvider extends RecipeProvider {
    public ShapedRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        createRecipe(
                pWriter,
                shaped(ModBlocks.ADVANCED_CRUCIBLE.get().asItem())
                        .patterns(List.of("a", "b", "c"))
                        .define('a', Items.OBSERVER)
                        .define('b', TFCBlocks.CRUCIBLE.get())
                        .define('c', Items.HOPPER)
                        .id("tfcadvancedcrucible:advanced_crucible")
        );

        createRecipe(
                pWriter,
                shaped(ModBlocks.FIRE_BARREL.get().asItem())
                        .patterns(List.of("aba", "a a", "aba"))
                        .define('a', TFCBlocks.FIRE_BRICKS.get())
                        .define('b', Items.BRICKS)
                        .id("tfcadvancedcrucible:fire_barrel")
        );

        createRecipe(
                pWriter,
                shaped(ModBlocks.LARGE_ADVANCED_CRUCIBLE.get().asItem())
                        .patterns(List.of(" a ", "dbd", " c "))
                        .define('a', Items.OBSERVER)
                        .define('b', ModBlocks.LARGE_CRUCIBLE.get())
                        .define('c', Items.HOPPER)
                        .define('d', TFCItems.METAL_ITEMS.get(Metal.Default.STEEL).get(Metal.ItemType.SHEET).get())
                        .id("tfcadvancedcrucible:large_advanced_crucible")
        );
    }

    @Override
    public @NotNull String getName() {
        return "recipes/shaped";
    }

    public ShapedRecipeBuilder shaped(ItemLike pResult) {
        return shaped(pResult, 1);
    }

    public ShapedRecipeBuilder shaped(ItemLike pResult, int pCount) {
        return new ShapedRecipeBuilder(pResult, pCount);
    }
}
