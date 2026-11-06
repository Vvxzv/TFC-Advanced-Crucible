package net.vvxzv.tfcadvancedcrucible.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.component.mold.IMold;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vvxzv.tfcadvancedcrucible.network.PourFasterPacket;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCrucibleScreen<T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> extends BlockEntityScreen<T, C> {
    protected int pourFasterDecayTicks = 0;

    public AbstractCrucibleScreen(C container, Inventory playerInventory, Component name, ResourceLocation texture) {
        super(container, playerInventory, name, texture);
        this.inventoryLabelY += 55;
        this.imageHeight += 55;
    }

    @Override
    protected void containerTick() {
        if (this.pourFasterDecayTicks <= 0 && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)) {
            if (this.hoveredSlot != null) {
                IMold mold = IMold.get(this.hoveredSlot.getItem());
                if (mold != null) {
                    PacketDistributor.sendToServer(new PourFasterPacket(this.blockEntity.getBlockPos(), this.hoveredSlot.index));
                    this.pourFasterDecayTicks = 10;
                }
            }
        } else {
            this.pourFasterDecayTicks--;
        }

        super.containerTick();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics stack, int mouseX, int mouseY) {
    }
}
