package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class RecipeProvider implements DataProvider {
    protected final PackOutput.PathProvider recipePathProvider;

    public RecipeProvider(PackOutput pOutput) {
        this.recipePathProvider = pOutput.createRegistryElementsPathProvider(Registries.RECIPE);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildRecipes((recipe) -> {
            if (!set.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());
            } else {
                list.add(DataProvider.saveStable(pOutput, recipe.serializeRecipe(), this.recipePathProvider.json(recipe.getId())));

            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildRecipes(Consumer<FinishedRecipe> pWriter);

    public void createRecipe(Consumer<FinishedRecipe> pWriter, RecipeBuilder recipe) {
        pWriter.accept(recipe);
    }

    @Override
    public abstract @NotNull String getName();
}
