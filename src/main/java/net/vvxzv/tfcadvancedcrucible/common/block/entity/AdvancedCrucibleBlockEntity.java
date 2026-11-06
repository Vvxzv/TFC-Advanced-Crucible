package net.vvxzv.tfcadvancedcrucible.common.block.entity;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.*;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.vvxzv.tfcadvancedcrucible.Config;
import net.vvxzv.tfcadvancedcrucible.common.container.AdvancedCrucibleContainer;
import net.vvxzv.tfcadvancedcrucible.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class AdvancedCrucibleBlockEntity extends AbstractCrucibleBlockEntity<AdvancedCrucibleBlockEntity.CrucibleInventory> {
    public static final int SLOTS = 10;
    public static final int SLOT_INPUT_START = 0;
    public static final int SLOT_INPUT_END = 8;
    public static final int SLOT_OUTPUT = 9;
    private final HeatingRecipe[] cachedRecipes = new HeatingRecipe[9];
    private @Nullable Direction outputFace = null;

    public AdvancedCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ADVANCED_CRUCIBLE.get(), pos, state, CrucibleInventory::new);
        this.sidedInventory.on(
                new PartialItemHandler(this.inventory).insert(0, 1, 2, 3, 4, 5, 6, 7, 8), Direction.UP)
                .on(new PartialItemHandler(this.inventory).insert(9).extract(9), Direction.Plane.HORIZONTAL);

        this.sidedFluidInventory.on(new PartialFluidHandler(this.inventory).insert(), Direction.UP)
                .on(new PartialFluidHandler(this.inventory).extract(), Direction.Plane.HORIZONTAL);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AdvancedCrucibleBlockEntity crucible) {
        crucible.checkForLastTickSync();
        crucible.checkForCalendarUpdate();
        if (crucible.needsRecipeUpdate) {
            crucible.needsRecipeUpdate = false;
            crucible.updateCaches();
        }

        if (crucible.temperature != crucible.targetTemperature) {
            crucible.temperature = HeatCapability.adjustTempTowards(crucible.temperature, crucible.targetTemperature);
        }

        if (crucible.targetTemperatureStabilityTicks > 0) {
            crucible.targetTemperatureStabilityTicks--;
        }

        if (crucible.targetTemperature > 0.0F && crucible.targetTemperatureStabilityTicks == 0) {
            crucible.targetTemperature = HeatCapability.adjustTempTowards(crucible.targetTemperature, 0.0F);
        }

        boolean canFill = crucible.lastFillTicks <= 0;

        for(int slot = SLOT_INPUT_START; slot <= SLOT_INPUT_END; slot++) {
            ItemStack inputStack = crucible.inventory.getStackInSlot(slot);
            if (!inputStack.isEmpty()) {
                IHeat inputHeat = HeatCapability.get(inputStack);
                if (inputHeat != null) {
                    HeatCapability.addTemp(inputHeat, crucible.temperature, 2.5F + crucible.temperature * 0.0025F);
                    HeatingRecipe recipe = crucible.cachedRecipes[slot];
                    if (recipe != null && recipe.isValidTemperature(inputHeat.getTemperature())) {
                        ItemStackInventory inventory = new ItemStackInventory(inputStack);
                        ItemStack outputItem = recipe.assemble(inventory, level.registryAccess());
                        FluidStack outputFluid = recipe.assembleFluid(inventory);
                        FoodCapability.applyTrait(outputItem, FoodTraits.BURNT_TO_A_CRISP);
                        crucible.tryPushOutputItem(level, pos, outputItem, slot);
                        crucible.inventory.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
                        crucible.markForSync();
                    }
                }
            }

            if (canFill) {
                ItemStack drainStack = crucible.inventory.getStackInSlot(slot);
                MoldLike mold = MoldLike.get(drainStack);
                if (mold != null && mold.isMolten() && FluidHelpers.transferExact(mold, crucible.inventory, 1)) {
                    if (crucible.fastPourTicks >= 0 && crucible.fastPourSlot == slot) {
                        crucible.fastPourTicks--;
                        crucible.lastFillTicks = TFCConfig.SERVER.crucibleFastPouringRate.get();
                    } else {
                        crucible.lastFillTicks = TFCConfig.SERVER.cruciblePouringRate.get();
                    }

                    crucible.markForSync();
                }
            }
        }

        if (crucible.lastFillTicks > 0) {
            crucible.lastFillTicks--;
        }

        if (crucible.inventory.isMolten()) {
            FluidStack outputDrop = crucible.inventory.drain(1, IFluidHandler.FluidAction.SIMULATE);
            FluidStack outputRemainder = Helpers.mergeOutputFluidIntoSlot(crucible.inventory, outputDrop, crucible.temperature, 9);
            if (outputRemainder.isEmpty()) {
                crucible.inventory.drain(1, IFluidHandler.FluidAction.EXECUTE);
            }

            crucible.markForSync();
        }
    }

    private void updateCaches() {
        for(int slot = SLOT_INPUT_START; slot <= SLOT_INPUT_END; slot++) {
            this.cachedRecipes[slot] = HeatingRecipe.getRecipe(this.inventory.getStackInSlot(slot));
        }
    }

    private void tryPushOutputItem(Level level, BlockPos pos, ItemStack outputItem, int slot) {
        if (!outputItem.isEmpty()) {
            if (this.outputFace != null) {
                BlockPos targetPos = pos.relative(this.outputFace);
                BlockEntity blockEntity = level.getBlockEntity(targetPos);
                if (blockEntity != null) {
                    IItemHandler targetHandler = blockEntity
                            .getCapability(ForgeCapabilities.ITEM_HANDLER, this.outputFace.getOpposite())
                            .orElse(null);
                    if (targetHandler != null) {
                        ItemStack remainder = outputItem.copy();
                        for (int i = 0; i < targetHandler.getSlots() && !remainder.isEmpty(); i++) {
                            remainder = targetHandler.insertItem(i, remainder, false);
                        }
                        this.inventory.setStackInSlot(slot, remainder);
                        return;
                    }
                }
            }
        }

        this.inventory.setStackInSlot(slot, outputItem);
    }

    @Override
    public AlloyView getAlloy() {
        return this.inventory.alloy;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return HeatCapability.maybeHas(stack) && (slot != SLOT_OUTPUT || Helpers.mightHaveCapability(stack, Capabilities.FLUID_ITEM));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return AdvancedCrucibleContainer.create(this, player.getInventory(), containerId);
    }

    @Nullable
    public Direction parseDirectionOrNull(@Nullable String name) {
        if (name == null) return null;
        return Direction.byName(name.toLowerCase(Locale.ROOT));
    }

    public String parseOutputFaceToString() {
        if(this.outputFace != null) {
            return this.outputFace.toString();
        }

        return "none";
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt) {
        this.outputFace = this.parseDirectionOrNull(nbt.getString("outputFace"));
        super.loadAdditional(nbt);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.putString("outputFace", this.parseOutputFaceToString());
        super.saveAdditional(nbt);
    }

    @Override
    public void setAndUpdateSlots(int slot) {
        super.setAndUpdateSlots(slot);
        if (slot != SLOT_OUTPUT) {
            this.cachedRecipes[slot] = HeatingRecipe.getRecipe(this.inventory.getStackInSlot(slot));
        }
    }

    public @Nullable Direction getOutputFace() {
        return this.outputFace;
    }

    public Component getOutputFaceComponent() {
        MutableComponent component = Component.translatable("gui.tfcadvancedcrucible.mode").append(": ");
        if(this.outputFace != null) {
            return component.append(Component.translatable("gui.tfcadvancedcrucible.mode." + this.outputFace.toString()));
        }
        return component.append(Component.translatable("gui.tfcadvancedcrucible.mode.none"));
    }

    public void setOutputFace(@Nullable Direction direction) {
        this.outputFace = direction;
        this.setChanged();
    }

    static class CrucibleInventory extends AbstractCrucibleInventory {
        CrucibleInventory(InventoryBlockEntity<?> entity) {
            super(entity, SLOTS, new Alloy(Config.advancedCrucibleCapacity));
        }
    }
}
