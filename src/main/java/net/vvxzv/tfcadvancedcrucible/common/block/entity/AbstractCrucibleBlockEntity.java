package net.vvxzv.tfcadvancedcrucible.common.block.entity;

import com.google.common.collect.ImmutableList;
import net.dries007.tfc.common.blockentities.IHeatable;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import net.dries007.tfc.common.capabilities.DelegateItemHandler;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.capabilities.PartialFluidHandler;
import net.dries007.tfc.common.capabilities.SidedHandler;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.block.CrucibleComponent;
import net.dries007.tfc.common.component.fluid.FluidContainer;
import net.dries007.tfc.common.component.fluid.FluidContainerInfo;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatComponent;
import net.dries007.tfc.common.component.heat.IHeatConsumer;
import net.dries007.tfc.util.FluidAlloy;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.SyncableContainerData;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCrucibleBlockEntity<C extends DelegateItemHandler & FluidContainer & IHeatConsumer & INBTSerializable<CompoundTag>> extends TickableInventoryBlockEntity<C> implements ICalendarTickable, IHeatable {
    public static final int TARGET_TEMPERATURE_STABILITY_TICKS = 5;
    private final SidedHandler<IFluidHandler> sidedFluidInventory;
    protected final SyncableContainerData syncableData;
    protected float temperature;
    protected float targetTemperature;
    protected boolean needsRecipeUpdate = true;
    protected int targetTemperatureStabilityTicks;
    protected int lastFillTicks;
    protected long lastUpdateTick;
    protected int fastPourSlot;
    protected int fastPourTicks;

    protected AbstractCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<C> inventory) {
        super(type, pos, state, inventory);
        this.temperature = this.targetTemperature = 0;
        this.lastFillTicks = this.fastPourTicks = this.fastPourSlot = 0;
        this.lastUpdateTick = -2147483648L;
        this.sidedFluidInventory = new SidedHandler<>(this.inventory);
        this.sidedFluidInventory.on(PartialFluidHandler::insertOnly, Direction.UP).on(PartialFluidHandler::extractOnly, Direction.Plane.HORIZONTAL);

        this.syncableData = (new SyncableContainerData()).add(() -> (int)this.temperature, (value) -> this.temperature = (float)value);
    }

    public IItemHandlerModifiable getItemInventory(@Nullable Direction context) {
        return this.inventory;
    }

    public @NotNull IHeatConsumer getHeatInventory(@Nullable Direction context) {
        return this.inventory;
    }

    public @Nullable IFluidHandler getSidedFluidInventory(@Nullable Direction context) {
        return this.sidedFluidInventory.get(context);
    }

    public float getTemperature() {
        return this.temperature;
    }

    public ContainerData getSyncableData() {
        return this.syncableData;
    }

    public abstract FluidAlloy getAlloy();

    public FluidStack getAlloyResult() {
        assert this.level != null;

        return this.getAlloy().getResult(this.level);
    }

    public abstract FluidContainerInfo containerInfo();

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
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput components) {
        CrucibleComponent crucible = components.getOrDefault(TFCComponents.CRUCIBLE, CrucibleComponent.EMPTY);
        HeatComponent heat = components.getOrDefault(TFCComponents.HEAT, HeatComponent.EMPTY);
        this.temperature = heat.getTemperature();
        Helpers.copyFrom(crucible.itemContent(), this.inventory);
        this.getAlloy().copyFrom(crucible.fluidContent());
        super.applyImplicitComponents(components);
    }

    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        assert this.level != null;

        FluidHeat heat = FluidHeat.getOrUnknown(this.getAlloy().getResult(this.level));
        float heatCapacity = heat.heatCapacity(this.getAlloy().getAmount());
        ImmutableList.Builder<ItemStack> inventoryList = ImmutableList.builder();
        Helpers.copyTo(inventoryList, this.inventory);
        builder.set(TFCComponents.CRUCIBLE, new CrucibleComponent(inventoryList.build(), this.getAlloy().copy()));
        builder.set(TFCComponents.HEAT, HeatComponent.of(2.0F + heatCapacity, this.temperature));
        super.collectImplicitComponents(builder);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        this.temperature = nbt.getFloat("temperature");
        this.targetTemperature = nbt.getFloat("targetTemperature");
        this.targetTemperatureStabilityTicks = nbt.getInt("targetTemperatureStabilityTicks");
        this.lastUpdateTick = nbt.getLong("lastUpdateTick");
        this.needsRecipeUpdate = true;
        super.loadAdditional(nbt, provider);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        nbt.putFloat("temperature", this.temperature);
        nbt.putFloat("targetTemperature", this.targetTemperature);
        nbt.putInt("targetTemperatureStabilityTicks", this.targetTemperatureStabilityTicks);
        nbt.putLong("lastUpdateTick", this.lastUpdateTick);
        super.saveAdditional(nbt, provider);
    }

    public void setFastPouring(int slot) {
        this.fastPourSlot = slot;
        this.fastPourTicks = 20;
    }

    static class AbstractCrucibleInventory implements DelegateItemHandler, FluidContainer, IHeatConsumer, INBTSerializable<CompoundTag> {
        protected final AbstractCrucibleBlockEntity<?> crucible;
        protected final InventoryItemHandler inventory;
        protected final FluidAlloy alloy;
        private final FluidContainerInfo info;

        protected AbstractCrucibleInventory(InventoryBlockEntity<?> entity, int slots, FluidContainerInfo info) {
            this.crucible = (AbstractCrucibleBlockEntity<?>)entity;
            this.inventory = new InventoryItemHandler(entity, slots);
            this.alloy = FluidAlloy.empty();
            this.info = info;
        }

        @Override
        public @NotNull FluidContainerInfo containerInfo() {
            return this.info;
        }

        public boolean isMolten() {
            assert this.crucible.level != null;

            FluidHeat metal = FluidHeat.get(this.alloy.getResult(this.crucible.level).getFluid());
            return metal == null || this.crucible.temperature > metal.meltTemperature();
        }

        @Override
        public @NotNull IItemHandlerModifiable getItemHandler() {
            return this.inventory;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
            CompoundTag nbt = new CompoundTag();
            nbt.put("inventory", this.inventory.serializeNBT(provider));
            nbt.put("alloy", this.alloy.serializeNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
            this.inventory.deserializeNBT(provider, nbt.getCompound("inventory"));
            this.alloy.deserializeNBT(nbt.getCompound("alloy"));
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            assert this.crucible.level != null;

            return this.alloy.getResult(this.crucible.level);
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.info.fluidCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return FluidHeat.get(stack.getFluid()) != null;
        }

        @Override
        public int fill(@NotNull FluidStack resource, IFluidHandler.@NotNull FluidAction action) {
            return this.alloy.fill(resource, action, this.info);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, IFluidHandler.@NotNull FluidAction action) {
            if (this.isMolten()) {
                assert this.crucible.level != null;

                FluidStack result = this.alloy.drain(this.crucible.level, maxDrain, action);
                if (action.execute()) {
                    this.crucible.markForSync();
                }

                return result;
            } else {
                return FluidStack.EMPTY;
            }
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
    }
}
