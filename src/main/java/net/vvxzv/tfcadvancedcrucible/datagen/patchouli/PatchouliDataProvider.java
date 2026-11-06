package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class PatchouliDataProvider implements DataProvider {
    protected final PackOutput.PathProvider dataPath;
    private final boolean onlyEntries;

    public PatchouliDataProvider(PackOutput pOutput, String bookName, String lang, boolean onlyEntries) {
        this.dataPath = pOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "patchouli_books/"+bookName+"/"+lang);
        this.onlyEntries = onlyEntries;
    }

    public PatchouliDataProvider(PackOutput pOutput, String bookName, String lang) {
        this(pOutput, bookName, lang, false);
    }

    public PatchouliDataProvider(PackOutput pOutput, String bookName) {
        this(pOutput, bookName, "en_us");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildData(data -> {
            if(!this.onlyEntries) {
                ResourceLocation categoriesID = ResourceLocation.fromNamespaceAndPath(data.getModId(), "categories/" + data.getCategoryName());
                list.add(DataProvider.saveStable(pOutput, data.serializeCategories(), this.dataPath.json(categoriesID)));
            }
            data.serializeEntries().forEach((name, jsonObject) -> {
                ResourceLocation entryId = ResourceLocation.fromNamespaceAndPath(data.getModId(), "entries/"+data.getCategoryName()+"/"+name);
                list.add(DataProvider.saveStable(pOutput, jsonObject, this.dataPath.json(entryId)));
            });
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildData(Consumer<FinishedPatchouliData> data);

    @Override
    public abstract @NotNull String getName();
}
