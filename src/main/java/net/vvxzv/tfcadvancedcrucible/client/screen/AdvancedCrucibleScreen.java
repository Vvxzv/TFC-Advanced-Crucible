package net.vvxzv.tfcadvancedcrucible.client.screen;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.AlloyView;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.container.AdvancedCrucibleContainer;
import org.jetbrains.annotations.NotNull;

public class AdvancedCrucibleScreen extends AbstractCrucibleScreen<AdvancedCrucibleBlockEntity, AdvancedCrucibleContainer> {
    private static final ResourceLocation BACKGROUND = Helpers.identifier("textures/gui/crucible.png");
    private static final int MAX_ELEMENTS = 3;
    private int scrollPos;
    private boolean scrollPress;

    public AdvancedCrucibleScreen(AdvancedCrucibleContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name, BACKGROUND);
        this.scrollPos = 0;
        this.scrollPress = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= this.leftPos + 154 && mouseX <= this.leftPos + 165 && mouseY >= this.topPos + 11 + this.scrollPos && mouseY <= this.topPos + 26 + this.scrollPos) {
            this.scrollPress = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrollPress) {
            this.scrollPos = Math.min(Math.max((int)mouseY - this.topPos - 18, 0), 49);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.scrollPress && button == 0) {
            this.scrollPress = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        int temperature = Heat.scaleTemperatureForGui(this.blockEntity.getTemperature());
        if (temperature > 0) {
            graphics.blit(this.texture, this.leftPos + 7, this.topPos + 131 - Math.min(temperature, 51), 176, 0, 15, 5);
        }

        graphics.blit(this.texture, this.leftPos + 154, this.topPos + 11 + this.scrollPos, 176, 7, 12, 15);

        graphics.drawString(
                this.font,
                this.blockEntity.getOutputFaceComponent(),
                this.leftPos + 8,
                this.topPos + 222,
                0xffffff,
                false
        );

        AlloyView alloy = this.blockEntity.getAlloy();
        if (alloy.getAmount() > 0) {
            TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(alloy.getResultAsFluidStack());
            int fillHeight = (int)Math.ceil(31.0 * alloy.getAmount() / alloy.getMaxUnits());
            RenderHelpers.fillAreaWithSprite(graphics, sprite, this.leftPos + 97, this.topPos + 124 - fillHeight, 36, fillHeight, 16, 16);
            this.resetToBackgroundSprite();

            Metal result = alloy.getResult(ClientHelpers.getLevelOrThrow());
            String resultText = ChatFormatting.UNDERLINE + I18n.get(result.getTranslationKey());
            graphics.drawString(this.font, resultText, this.leftPos + 10, this.topPos + 11, 0, false);

            int startElement = Math.max(0, (int)Math.floor((alloy.getMetals().size() - 3) / 49.0 * this.scrollPos + 1));

            int yPos = this.topPos + 22;
            int index = -1;

            for (Object2DoubleMap.Entry<Metal> entry : alloy.getMetals().object2DoubleEntrySet()) {
                index++;
                if (index >= startElement) {
                    if (index > startElement - 1 + MAX_ELEMENTS) {
                        break;
                    }

                    String metalName = this.font.plainSubstrByWidth(I18n.get(entry.getKey().getTranslationKey()), 141) + ":";
                    MutableComponent content = Component.translatable("tfc.tooltip.crucible_content_line", Tooltips.fluidUnits(entry.getDoubleValue()), String.format("%2.1f", Math.round(1000.0 * entry.getDoubleValue() / alloy.getAmount()) / 10.0));
                    graphics.drawString(this.font, metalName, this.leftPos + 10, yPos, 4210752, false);
                    graphics.drawString(this.font, content, this.leftPos + 10, yPos + 9, 4210752, false);
                    yPos += 18;
                }
            }
        }

    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (RenderHelpers.isInside(mouseX, mouseY, this.leftPos + 7, this.topPos + 131 - 51, 15, 51)) {
            MutableComponent text = TFCConfig.CLIENT.heatTooltipStyle.get().formatColored(this.blockEntity.getTemperature());
            if (text != null) {
                graphics.renderTooltip(this.font, text, mouseX, mouseY);
            }
        }
    }
}
