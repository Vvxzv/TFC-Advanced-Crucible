package net.vvxzv.tfcadvancedcrucible.common.container;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.MoldLike;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.CallbackSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.registry.ContainerTypes;
import org.jetbrains.annotations.NotNull;

public class AdvancedCrucibleContainer extends BlockEntityContainer<AdvancedCrucibleBlockEntity> {
    public static AdvancedCrucibleContainer create(AdvancedCrucibleBlockEntity crucible, Inventory playerInv, int windowId) {
        return new AdvancedCrucibleContainer(windowId, crucible).init(playerInv, 55);
    }

    protected AdvancedCrucibleContainer(int windowId, AdvancedCrucibleBlockEntity crucible) {
        super(ContainerTypes.ADVANCED_CRUCIBLE.get(), windowId, crucible);
        this.addDataSlots(crucible.getSyncableData());
    }

    @Override
    protected boolean moveStack(@NotNull ItemStack stack, int slotIndex) {
        return switch (this.typeOf(slotIndex)) {
            case MAIN_INVENTORY, HOTBAR ->
                    MoldLike.get(stack) != null ? !this.moveItemStackTo(stack, 9, 10, false) : !this.moveItemStackTo(stack, 0, 9, false);
            case CONTAINER -> !this.moveItemStackTo(stack, this.containerSlots, this.slots.size(), false);
        };
    }

    @Override
    protected void addContainerSlots() {
        this.blockEntity.getCapability(Capabilities.ITEM).ifPresent((inventory) -> {
            for(int slot = 0; slot <= 8; slot++) {
                int line = slot / 3;
                int column = slot % 3;
                this.addSlot(new CallbackSlot(this.blockEntity, inventory, slot, 26 + column * 18, 82 + line * 18));
            }

            this.addSlot(new CallbackSlot(this.blockEntity, inventory, 9, 152, 100));
        });
    }
}
