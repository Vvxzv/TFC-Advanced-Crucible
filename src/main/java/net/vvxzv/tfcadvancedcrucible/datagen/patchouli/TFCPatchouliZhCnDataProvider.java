package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TFCPatchouliZhCnDataProvider extends PatchouliDataProvider{
    public TFCPatchouliZhCnDataProvider(PackOutput pOutput) {
        super(pOutput, "field_guide", "zh_cn");
    }

    @Override
    protected void buildData(Consumer<FinishedPatchouliData> data) {
        data.accept(
                new PatchouliDataBuilder(ResourceLocation.fromNamespaceAndPath("tfc", "advanced_crucible"))
                        .category(
                                "群峦高级坩埚",
                                "关于群峦高级坩埚的模组介绍",
                                ModBlocks.ADVANCED_CRUCIBLE.get().asItem(),
                                20261104
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("advanced_crucible"),
                                        "高级坩埚",
                                        ModBlocks.ADVANCED_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("高级坩埚")
                                                        .add("比")
                                                        .addThing("坩埚")
                                                        .add("熔炼速度更快，而且多一种功能：")
                                                        .addChatFormatting("煅烧物品成品自动弹出", ChatFormatting.BOLD)
                                                        .br2()
                                                        .add("调整输出方向的方法就是手持")
                                                        .addThing("锤子")
                                                        .add("右键")
                                                        .addThing("高级坩埚")
                                                        .add("的一面将其调整为输出面，然后")
                                                        .addThing("高级坩埚")
                                                        .add("在煅烧完成物品时就会尝试自动输出到这一面所对应的容器。")
                                        )

                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_crucible"),
                                        "大型坩埚",
                                        ModBlocks.LARGE_CRUCIBLE.get().asItem()
                                )
                                        .addTextPage(
                                                PatchouliDataBuilder.Text.of()
                                                        .addThing("大型坩埚")
                                                        .add("是")
                                                        .addThing("坩埚")
                                                        .add("的三合一版本，流体容量是原来的4倍，并且可以同时浇筑16个模具！但是它的熔炼速度比")
                                                        .addThing("坩埚")
                                                        .add("慢。")
                                        )
                        )
                        .addEntry(
                                new PatchouliDataBuilder.EntriesBuilder(
                                        ResourceLocation.parse("large_advanced_crucible"),
                                        "大型高级坩埚",
                                        ModBlocks.LARGE_ADVANCED_CRUCIBLE.get()
                                )
                                        .addTextPage(
                                                "大型高级坩埚",
                                                PatchouliDataBuilder.Text.of("具有")
                                                        .addThing("高级坩埚")
                                                        .add("功能的")
                                                        .addThing("大型坩埚。")
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
                                                        .add("是一个不会被点燃的储物容器，用于配合")
                                                        .addThing("高级坩埚")
                                                        .add("的自动输出功能从而实现自动化煅烧物品。")
                                        )
                        )
        );
    }

    @Override
    public @NotNull String getName() {
        return "tfc_patchouli/zh_cn";
    }
}
