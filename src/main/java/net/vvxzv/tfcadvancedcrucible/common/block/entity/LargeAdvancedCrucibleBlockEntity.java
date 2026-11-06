package net.vvxzv.tfcadvancedcrucible.common.block.entity;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.PartialItemHandler;
import net.dries007.tfc.common.component.fluid.FluidContainerInfo;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.component.mold.IMold;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.FluidAlloy;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.vvxzv.tfcadvancedcrucible.Config;
import net.vvxzv.tfcadvancedcrucible.common.container.LargeAdvancedCrucibleContainer;
import net.vvxzv.tfcadvancedcrucible.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class LargeAdvancedCrucibleBlockEntity extends AbstractCrucibleBlockEntity<LargeAdvancedCrucibleBlockEntity.CrucibleInventory> {
    public static final int HEAT_SLOTS = 27;
    public static final int MOLD_SLOTS = 16;
    public static final int SLOTS = HEAT_SLOTS + MOLD_SLOTS;
    public static final int HEAT_SLOT_START = 0;
    public static final int HEAT_SLOT_END = 26;
    public static final int MOLD_SLOT_START = 27;
    public static final int MOLD_SLOT_END = 42;
    private final HeatingRecipe[] cachedRecipes = new HeatingRecipe[27];
    private @Nullable Direction outputFace = null;

    public LargeAdvancedCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LARGE_ADVANCED_CRUCIBLE.get(), pos, state, CrucibleInventory::new);
        this.sidedInventory.on(
                        new PartialItemHandler(this.inventory).insert(
                                0, 1, 2, 3, 4, 5, 6, 7, 8,
                                9, 10, 11, 12, 13, 14, 15, 16, 17,
                                18, 19, 20, 21, 22, 23, 24, 25, 26), Direction.UP)
                .on(new PartialItemHandler(this.inventory).insert(
                        27, 28, 29, 30,
                        31, 32, 33, 34,
                        35, 36, 37, 38,
                        39, 40, 41, 42
                ).extract(
                        27, 28, 29, 30,
                        31, 32, 33, 34,
                        35, 36, 37, 38,
                        39, 40, 41, 42), Direction.Plane.HORIZONTAL);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LargeAdvancedCrucibleBlockEntity crucible) {
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

        for(int slot = HEAT_SLOT_START; slot <= HEAT_SLOT_END; slot++) {
            ItemStack inputStack = crucible.inventory.getStackInSlot(slot);
            if (!inputStack.isEmpty()) {
                IHeat inputHeat = HeatCapability.get(inputStack);
                if (inputHeat != null) {
                    HeatCapability.addTemp(inputHeat, crucible.temperature, 2.0F + crucible.temperature * 0.0025F);
                    HeatingRecipe recipe = crucible.cachedRecipes[slot];
                    if (recipe != null && recipe.isValidTemperature(inputHeat.getTemperature())) {
                        ItemStack outputItem = recipe.assembleItem(inputStack);
                        FluidStack outputFluid = recipe.assembleFluid(inputStack);
                        FoodCapability.applyTrait(outputItem, FoodTraits.BURNT_TO_A_CRISP);
                        crucible.tryPushOutputItem(level, pos, outputItem, slot);
                        crucible.inventory.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
                        crucible.markForSync();
                    }
                }
            }

            if (canFill) {
                ItemStack drainStack = crucible.inventory.getStackInSlot(slot);
                IMold mold = IMold.get(drainStack);
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
            for (int slot = MOLD_SLOT_START; slot <= MOLD_SLOT_END; slot++) {
                FluidStack outputRemainder = Helpers.mergeOutputFluidIntoSlot(crucible.inventory, outputDrop, crucible.temperature, slot);
                if (outputRemainder.isEmpty()) {
                    crucible.inventory.drain(1, IFluidHandler.FluidAction.EXECUTE);
                }
            }

            crucible.markForSync();
        }
    }

    private void updateCaches() {
        for(int slot = HEAT_SLOT_START; slot <= HEAT_SLOT_END; slot++) {
            this.cachedRecipes[slot] = HeatingRecipe.getRecipe(this.inventory.getStackInSlot(slot));
        }
    }

    private void tryPushOutputItem(Level level, BlockPos pos, ItemStack outputItem, int slot) {
        if (!outputItem.isEmpty()) {
            if (this.outputFace != null) {
                BlockPos targetPos = pos.relative(this.outputFace);
                BlockEntity blockEntity = level.getBlockEntity(targetPos);
                if (blockEntity != null) {
                    IItemHandler targetHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, this.outputFace.getOpposite());
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
    public FluidAlloy getAlloy() {
        return this.inventory.alloy;
    }

    @Override
    public FluidContainerInfo containerInfo() {
        return CrucibleInventory.INFO;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if(HeatCapability.has(stack)) {
            if(slot >= HEAT_SLOT_START && slot <= HEAT_SLOT_END) {
                return true;
            } else {
                return Helpers.mightHaveCapability(stack, Capabilities.FluidHandler.ITEM);
            }
        }

        return false;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return LargeAdvancedCrucibleContainer.create(this, player.getInventory(), containerId);
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
    public void loadAdditional(CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        this.outputFace = this.parseDirectionOrNull(nbt.getString("outputFace"));
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        nbt.putString("outputFace", this.parseOutputFaceToString());
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void setAndUpdateSlots(int slot) {
        super.setAndUpdateSlots(slot);
        if (slot >= HEAT_SLOT_START && slot <= HEAT_SLOT_END) {
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
        private static final FluidContainerInfo INFO = new FluidContainerInfo() {
            public boolean canContainFluid(@NotNull Fluid input) {
                return FluidHeat.get(input) != null;
            }

            public int fluidCapacity() {
                return Config.largeAdvancedCrucibleCapacity;
            }
        };

        CrucibleInventory(InventoryBlockEntity<?> entity) {
            super(entity, SLOTS, INFO);
        }
    }
}
