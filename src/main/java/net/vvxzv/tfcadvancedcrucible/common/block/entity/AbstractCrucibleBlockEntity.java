package net.vvxzv.tfcadvancedcrucible.common.block.entity;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import net.dries007.tfc.common.capabilities.*;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import net.dries007.tfc.util.*;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCrucibleBlockEntity<C extends IItemHandlerModifiable & INBTSerializable<CompoundTag> & SimpleFluidHandler & IHeatBlock> extends TickableInventoryBlockEntity<C> implements ICalendarTickable {
    public static final int TARGET_TEMPERATURE_STABILITY_TICKS = 5;
    public static final Component NAME = Component.translatable("tfcadvancedcrucible.tile_entity.crucible");
    protected final SidedHandler.Builder<IFluidHandler> sidedFluidInventory;
    protected final SidedHandler.Noop<IHeatBlock> sidedHeat;
    protected final IntArrayBuilder syncableData;
    protected float temperature;
    protected float targetTemperature;
    protected boolean needsRecipeUpdate = true;
    protected int targetTemperatureStabilityTicks;
    protected int lastFillTicks;
    protected long lastUpdateTick;
    protected int fastPourSlot;
    protected int fastPourTicks;

    protected AbstractCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<C> inventory, Component defaultName) {
        super(type, pos, state, inventory, defaultName);
        this.temperature = this.targetTemperature = 0;
        this.lastFillTicks = this.fastPourTicks = this.fastPourSlot = 0;
        this.lastUpdateTick = -2147483648L;
        this.syncableData = new IntArrayBuilder().add(() -> (int)this.temperature, (value) -> this.temperature = (float)value);
        this.sidedFluidInventory = new SidedHandler.Builder<>(this.inventory);
        this.sidedHeat = new SidedHandler.Noop<>(this.inventory);
    }

    protected AbstractCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<C> inventory) {
        this(type, pos, state, inventory, NAME);
    }

    public float getTemperature() {
        return this.temperature;
    }

    public ContainerData getSyncableData() {
        return this.syncableData;
    }

    public abstract AlloyView getAlloy();

    @Override
    public abstract boolean isItemValid(int slot, @NotNull ItemStack stack);

    @Override
    public void onCalendarUpdate(long ticks) {
        assert this.level != null;

        this.targetTemperature = HeatCapability.adjustTempTowards(this.targetTemperature, 0.0F, (float)ticks);
        this.temperature = HeatCapability.adjustTempTowards(this.temperature, this.targetTemperature, (float)ticks);
    }

    @Override
    public int getSlotStackLimit(int slot) {
        return 1;
    }

    @Override
    public long getLastCalendarUpdateTick() {
        return this.lastUpdateTick;
    }

    @Override
    public void setLastCalendarUpdateTick(long tick) {
        this.lastUpdateTick = tick;
    }

    @Override
    public abstract @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player);

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt) {
        this.temperature = nbt.getFloat("temperature");
        this.targetTemperature = nbt.getFloat("targetTemperature");
        this.targetTemperatureStabilityTicks = nbt.getInt("targetTemperatureStabilityTicks");
        this.lastUpdateTick = nbt.getLong("lastUpdateTick");
        this.needsRecipeUpdate = true;
        super.loadAdditional(nbt);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.putFloat("temperature", this.temperature);
        nbt.putFloat("targetTemperature", this.targetTemperature);
        nbt.putInt("targetTemperatureStabilityTicks", this.targetTemperatureStabilityTicks);
        nbt.putBoolean("empty", Helpers.isEmpty(this.inventory) && this.getAlloy().isEmpty());
        nbt.putLong("lastUpdateTick", this.lastUpdateTick);
        super.saveAdditional(nbt);
    }

    public void setFastPouring(int slot) {
        this.fastPourSlot = slot;
        this.fastPourTicks = 20;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.FLUID) {
            return this.sidedFluidInventory.getSidedHandler(side).cast();
        } else {
            return cap == HeatCapability.BLOCK_CAPABILITY ? this.sidedHeat.getSidedHandler(side).cast() : super.getCapability(cap, side);
        }
    }

    static class AbstractCrucibleInventory implements DelegateItemHandler, SimpleFluidHandler, INBTSerializable<CompoundTag>, IHeatBlock {
        protected final AbstractCrucibleBlockEntity<?> crucible;
        protected final InventoryItemHandler inventory;
        protected final Alloy alloy;

        protected AbstractCrucibleInventory(InventoryBlockEntity<?> entity, int slots, Alloy alloy) {
            this.crucible = (AbstractCrucibleBlockEntity<?>)entity;
            this.inventory = new InventoryItemHandler(entity, slots);
            this.alloy = alloy;
        }

        public boolean isMolten() {
            assert this.crucible.getLevel() != null;

            return this.crucible.temperature > this.alloy.getResult(this.crucible.getLevel()).getMeltTemperature();
        }

        @Override
        public @NotNull IItemHandlerModifiable getItemHandler() {
            return this.inventory;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.put("inventory", this.inventory.serializeNBT());
            nbt.put("alloy", this.alloy.serializeNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
            this.alloy.deserializeNBT(nbt.getCompound("alloy"));
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return this.alloy.getResultAsFluidStack();
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.alloy.getMaxUnits();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return Metal.get(stack.getFluid()) != null;
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            Metal metal = Metal.get(resource.getFluid());
            return metal != null ? this.alloy.add(metal, resource.getAmount(), action.simulate()) : 0;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (this.isMolten()) {
                assert this.crucible.getLevel() != null;

                Metal result = this.alloy.getResult(this.crucible.getLevel());
                int amount = this.alloy.removeAlloy(maxDrain, action.simulate());
                if (action.execute()) {
                    this.crucible.markForSync();
                }

                return new FluidStack(result.getFluid(), amount);
            }

            return FluidStack.EMPTY;
        }

        @Override
        public float getTemperature() {
            return this.crucible.temperature;
        }

        @Override
        public void setTemperature(float temperature) {
            this.crucible.targetTemperature = temperature;
            this.crucible.targetTemperatureStabilityTicks = AbstractCrucibleBlockEntity.TARGET_TEMPERATURE_STABILITY_TICKS;
            this.crucible.markForSync();
        }

        @Override
        public void setTemperatureIfWarmer(float temperature) {
            if (temperature >= this.crucible.temperature) {
                this.crucible.temperature = temperature;
                this.crucible.targetTemperatureStabilityTicks = AbstractCrucibleBlockEntity.TARGET_TEMPERATURE_STABILITY_TICKS;
                this.crucible.markForSync();
            }
        }
    }
}
