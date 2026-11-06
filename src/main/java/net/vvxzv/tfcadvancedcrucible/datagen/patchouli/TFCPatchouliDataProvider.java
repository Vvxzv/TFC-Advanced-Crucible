package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TFCPatchouliDataProvider extends PatchouliDataProvider{
    public TFCPatchouliDataProvider(PackOutput pOutput) {
        super(pOutput, "field_guide");
    }

    @Override
    protected void buildData(Consumer<FinishedPatchouliData> data) {
        data.accept(
                new PatchouliDataBuilder(ResourceLocation.fromNamespaceAndPath("tfc", "advanced_crucible"))
                        .category(
                                "TFC Advanced Crucible",
                                "An introduction about TFC Advanced Crucible",
                                ModBlocks.ADVANCED_CRUCIBLE.get().asItem(),
                                20261104
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("advanced_crucible"),
                                        "Advanced Crucible",
                                        ModBlocks.ADVANCED_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("Advanced Crucible ")
                                                        .add("have a faster melting speed than ")
                                                        .addThing("Crucible ")
                                                        .add("and an additional function: ")
                                                        .addChatFormatting("automatic ejection of calcined products into finished products", ChatFormatting.BOLD)
                                                        .br2()
                                                        .add("The method to adjust the output direction is to hold a ")
                                                        .addThing("Hammer ")
                                                        .add("and right-click on one side of the ")
                                                        .addThing("Advanced Crucible ")
                                                        .add("to adjust it to the output side. Then, when the item is calcined, the ")
                                                        .addThing("Advanced Crucible ")
                                                        .add("will attempt to automatically output to the container corresponding to this side.")

                                        )

                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_crucible"),
                                        "Large Crucible",
                                        ModBlocks.LARGE_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("Large Crucible ")
                                                        .add("is a three in one version of ")
                                                        .addThing("Crucible")
                                                        .add(", with a fluid capacity four times that of the original, and can simultaneously pour 16 molds! But its melting speed is slower than that of ")
                                                        .addThing("Crucible")
                                                        .add(".")
                                        )
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_advanced_crucible"),
                                        "Large Advanced Crucible",
                                        ModBlocks.LARGE_ADVANCED_CRUCIBLE.get()
                                )
                                        .addTextPage(
                                                "Large Advanced Crucible",
                                                PatchouliDataBuilder.Text.of("The ")
                                                        .addThing("Large crucible ")
                                                        .add("with ")
                                                        .addThing("Advanced Crucible ")
                                                        .add("function.")
                                        )
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("fire_barrel"),
                                        "Fire Barrel",
                                        ModBlocks.FIRE_BARREL.get()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("Fire Barrel ")
                                                        .add(" is a non flammable storage container used in conjunction with the automatic output function of ")
                                                        .addThing("Advanced Crucible ")
                                                        .add("to achieve automated calcination of items.")
                                        )
                        )
        );
    }

    @Override
    public @NotNull String getName() {
        return "tfc_patchouli/en_us";
    }
}
