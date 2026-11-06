package net.vvxzv.tfcadvancedcrucible.client.screen;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.FluidAlloy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.client.utils.CustomTooltipComponent;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.container.LargeCrucibleContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LargeCrucibleScreen  extends AbstractCrucibleScreen<LargeCrucibleBlockEntity, LargeCrucibleContainer> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, "textures/gui/large_crucible.png");

    public LargeCrucibleScreen(LargeCrucibleContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name, BACKGROUND);
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        int temperature = Heat.scaleTemperatureForGui(this.blockEntity.getTemperature());
        if (temperature > 0) {
            graphics.blit(this.texture, this.leftPos + 8, this.topPos + 122 - Math.min(temperature, 51), 176, 0, 15, 5);
        }

        FluidAlloy alloy = this.blockEntity.getAlloy();
        FluidStack alloyResult = this.blockEntity.getAlloyResult();
        if (alloy.getAmount() > 0 && !alloyResult.isEmpty()) {
            TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(alloyResult);
            int fillHeight = (int)Math.ceil(31.0 * alloy.getAmount() / this.blockEntity.containerInfo().fluidCapacity());
            RenderHelpers.fillAreaWithSprite(graphics, sprite, this.leftPos + 35, this.topPos + 114 - fillHeight, 36, fillHeight, 16, 16);
            this.resetToBackgroundSprite();

            Object2DoubleMap<Fluid> fluids = alloy.getContent();
            List<Component> texts = new ArrayList<>(List.of(Component.translatable(alloyResult.getFluidType().getDescriptionId()).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
            fluids.forEach((f, d) -> {
                texts.add(
                        Component.translatable(f.getFluidType().getDescriptionId())
                                .append(Component.literal(" (" + String.format("%.1f", d) + "mb)"))
                );
            });

            new CustomTooltipComponent(32, 82, 42, 35){
                @Override
                public Component[] getComponents() {
                    return texts.toArray(new Component[0]);
                }

                @Override
                public boolean hasTooltip() {
                    return true;
                }
            }.draw(graphics, this.leftPos, this.topPos, mouseX, mouseY);
        }
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (RenderHelpers.isInside(mouseX, mouseY, this.leftPos + 8, this.topPos + 122 - 51, 15, 51)) {
            MutableComponent text = TFCConfig.CLIENT.heatTooltipStyle.get().formatColored(this.blockEntity.getTemperature());
            if (text != null) {
                graphics.renderTooltip(this.font, text, mouseX, mouseY);
            }
        }
    }
}
