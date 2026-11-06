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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.vvxzv.tfcadvancedcrucible.Config;
import net.vvxzv.tfcadvancedcrucible.common.container.LargeCrucibleContainer;
import net.vvxzv.tfcadvancedcrucible.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LargeCrucibleBlockEntity extends AbstractCrucibleBlockEntity<LargeCrucibleBlockEntity.CrucibleInventory> {
    public static final int HEAT_SLOTS = 27;
    public static final int MOLD_SLOTS = 16;
    public static final int SLOTS = HEAT_SLOTS + MOLD_SLOTS;
    public static final int HEAT_SLOT_START = 0;
    public static final int HEAT_SLOT_END = 26;
    public static final int MOLD_SLOT_START = 27;
    public static final int MOLD_SLOT_END = 42;
    private final HeatingRecipe[] cachedRecipes = new HeatingRecipe[27];

    public LargeCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LARGE_CRUCIBLE.get(), pos, state, CrucibleInventory::new);
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, LargeCrucibleBlockEntity crucible) {
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
                    HeatCapability.addTemp(inputHeat, crucible.temperature, 1.5F + crucible.temperature * 0.0025F);
                    HeatingRecipe recipe = crucible.cachedRecipes[slot];
                    if (recipe != null && recipe.isValidTemperature(inputHeat.getTemperature())) {
                        ItemStack outputItem = recipe.assembleItem(inputStack);
                        FluidStack outputFluid = recipe.assembleFluid(inputStack);
                        FoodCapability.applyTrait(outputItem, FoodTraits.BURNT_TO_A_CRISP);
                        crucible.inventory.setStackInSlot(slot, outputItem);
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
        return LargeCrucibleContainer.create(this, player.getInventory(), containerId);
    }

    @Override
    public void setAndUpdateSlots(int slot) {
        super.setAndUpdateSlots(slot);
        if (slot >= HEAT_SLOT_START && slot <= HEAT_SLOT_END) {
            this.cachedRecipes[slot] = HeatingRecipe.getRecipe(this.inventory.getStackInSlot(slot));
        }
    }

    static class CrucibleInventory extends AbstractCrucibleInventory {
        private static final FluidContainerInfo INFO = new FluidContainerInfo() {
            public boolean canContainFluid(@NotNull Fluid input) {
                return FluidHeat.get(input) != null;
            }

            public int fluidCapacity() {
                return Config.largeCrucibleCapacity;
            }
        };

        CrucibleInventory(InventoryBlockEntity<?> entity) {
            super(entity, SLOTS, INFO);
        }
    }
}
