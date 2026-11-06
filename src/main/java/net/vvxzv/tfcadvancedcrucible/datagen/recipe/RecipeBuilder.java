package net.vvxzv.tfcadvancedcrucible.datagen.recipe;

import net.minecraft.resources.ResourceLocation;

public abstract class RecipeBuilder implements FinishedRecipe {
    protected ResourceLocation id = null;

    public RecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public RecipeBuilder id(String id) {
        return this.id(ResourceLocation.parse(id));
    }

    @Override
    public ResourceLocation getId() {
        if(this.id != null) {
            return this.id;
        }

        throw new IllegalStateException("Recipe id can not be null!");
    }
}