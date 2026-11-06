package net.vvxzv.tfcadvancedcrucible.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.datagen.loot.ModLootTablesProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.patchouli.TFCPatchouliDataProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.patchouli.TFCPatchouliZhCnDataProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.patchouli.TFCPatchouliZhDataProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.recipe.HeatingRecipeProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.recipe.ShapedRecipeProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.recipe.ShapelessRecipeProvider;
import net.vvxzv.tfcadvancedcrucible.datagen.tfcdata.TFCHeatDataProvider;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCAdvancedCrucible.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        genData(gen, event.includeServer(), output, lookup, helper);

        gen.addProvider(event.includeClient(), new TFCPatchouliDataProvider(output));
        gen.addProvider(event.includeClient(), new TFCPatchouliZhCnDataProvider(output));
        gen.addProvider(event.includeClient(), new TFCPatchouliZhDataProvider(output, "zh_hk"));
        gen.addProvider(event.includeClient(), new TFCPatchouliZhDataProvider(output, "zh_tw"));
    }

    private static void genData(DataGenerator gen, boolean includeServer, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper helper) {
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookup, helper);
        gen.addProvider(includeServer, blockTagsProvider);
        gen.addProvider(includeServer, new ModItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        gen.addProvider(includeServer, new ModLootTablesProvider(output, lookup));

        genRecipes(gen, includeServer, output);

        gen.addProvider(includeServer, new TFCHeatDataProvider(output));
    }

    private static void genRecipes(DataGenerator gen, boolean includeServer, PackOutput output) {
        gen.addProvider(includeServer, new ShapedRecipeProvider(output));
        gen.addProvider(includeServer, new ShapelessRecipeProvider(output));
        gen.addProvider(includeServer, new HeatingRecipeProvider(output));
    }
}
