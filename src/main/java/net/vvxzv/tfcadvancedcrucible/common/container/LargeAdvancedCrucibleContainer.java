package net.vvxzv.tfcadvancedcrucible.common.container;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.MoldLike;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.CallbackSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.registry.ContainerTypes;
import org.jetbrains.annotations.NotNull;

public class LargeAdvancedCrucibleContainer extends BlockEntityContainer<LargeAdvancedCrucibleBlockEntity> {
    public static LargeAdvancedCrucibleContainer create(LargeAdvancedCrucibleBlockEntity crucible, Inventory playerInv, int windowId) {
        return new LargeAdvancedCrucibleContainer(windowId, crucible).init(playerInv, 55);
    }

    protected LargeAdvancedCrucibleContainer(int windowId, LargeAdvancedCrucibleBlockEntity crucible) {
        super(ContainerTypes.LARGE_ADVANCED_CRUCIBLE.get(), windowId, crucible);
        this.addDataSlots(crucible.getSyncableData());
    }

    @Override
    protected boolean moveStack(@NotNull ItemStack stack, int slotIndex) {
        return switch (this.typeOf(slotIndex)) {
            case MAIN_INVENTORY, HOTBAR ->
                    MoldLike.get(stack) != null ? !this.moveItemStackTo(stack, 27, 43, false) : !this.moveItemStackTo(stack, 0, 27, false);
            case CONTAINER -> !this.moveItemStackTo(stack, this.containerSlots, this.slots.size(), false);
        };
    }

    @Override
    protected void addContainerSlots() {
        this.blockEntity.getCapability(Capabilities.ITEM).ifPresent((inventory) -> {
            for(int slot = 0; slot <= 26; slot++) {
                int line = slot / 9;
                int column = slot % 9;
                this.addSlot(new CallbackSlot(this.blockEntity, inventory, slot, 8 + column * 18, 7 + line * 18));
            }

            for (int slot = 27; slot <= 42; slot++) {
                int line = (slot - 27) / 4;
                int column = (slot - 27) % 4;
                this.addSlot(new CallbackSlot(this.blockEntity, inventory, slot, 98 + column * 18, 64 + line * 18));
            }
        });
    }
}
