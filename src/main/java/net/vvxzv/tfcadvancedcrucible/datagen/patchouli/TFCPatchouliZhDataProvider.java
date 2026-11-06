package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TFCPatchouliZhDataProvider extends PatchouliDataProvider{
    private final String lang;

    public TFCPatchouliZhDataProvider(PackOutput pOutput, String lang) {
        super(pOutput, "field_guide", lang);
        this.lang = lang;
    }

    @Override
    protected void buildData(Consumer<FinishedPatchouliData> data) {
        data.accept(
                new PatchouliDataBuilder(ResourceLocation.fromNamespaceAndPath("tfc", "advanced_crucible"))
                        .category(
                                "群巒高級坩堝",
                                "關於群巒高級坩堝的模組介紹",
                                ModBlocks.ADVANCED_CRUCIBLE.get().asItem(),
                                20261104
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("advanced_crucible"),
                                        "高級坩堝",
                                        ModBlocks.ADVANCED_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("高級坩堝")
                                                        .add("比")
                                                        .addThing("坩堝")
                                                        .add("熔煉速度更快，而且多一種功能：")
                                                        .addChatFormatting("煆燒物品成品自動彈出", ChatFormatting.BOLD)
                                                        .br2()
                                                        .add("調整輸出方向的方法就是手持")
                                                        .addThing("錘子")
                                                        .add("右鍵")
                                                        .addThing("高級坩堝")
                                                        .add("的一面將其調整為輸出面，然後")
                                                        .addThing("高級坩堝")
                                                        .add("在煆燒完成物品時就會嘗試自動輸出到這一面所對應的容器。")
                                        )

                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_crucible"),
                                        "大型坩堝",
                                        ModBlocks.LARGE_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("大型坩堝")
                                                        .add("是")
                                                        .addThing("坩堝")
                                                        .add("的三合一版本，流體容量是原來的4倍，並且可以同時澆築16個模具！ 但是它的熔煉速度比")
                                                        .addThing("坩堝")
                                                        .add("慢。")
                                        )
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_advanced_crucible"),
                                        "大型高级坩堝",
                                        ModBlocks.LARGE_ADVANCED_CRUCIBLE.get()
                                )
                                        .addTextPage(
                                                "大型高级坩堝",
                                                PatchouliDataBuilder.Text.of("具有")
                                                        .addThing("高级坩堝")
                                                        .add("功能的")
                                                        .addThing("大型坩堝。")
                                        )
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("fire_barrel"),
                                        "耐火桶",
                                        ModBlocks.FIRE_BARREL.get()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("耐火桶")
                                                        .add("是一個不會被點燃的儲物容器，用於配合")
                                                        .addThing("高级坩堝")
                                                        .add("的自動輸出功能從而實現自動化煆燒物品。")
                                        )
                        )
        );
    }

    @Override
    public @NotNull String getName() {
        return "tfc_patchouli/" + this.lang;
    }
}
