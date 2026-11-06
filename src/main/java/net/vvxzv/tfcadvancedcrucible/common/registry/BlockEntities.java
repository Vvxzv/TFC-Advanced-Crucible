package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.FireBarrelBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TFCAdvancedCrucible.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeCrucibleBlockEntity>> LARGE_CRUCIBLE = BLOCK_ENTITIES.register(
            "large_crucible",
            () -> BlockEntityType.Builder.of(
                    LargeCrucibleBlockEntity::new,
                    ModBlocks.LARGE_CRUCIBLE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedCrucibleBlockEntity>> ADVANCED_CRUCIBLE = BLOCK_ENTITIES.register(
            "advanced_crucible",
            () -> BlockEntityType.Builder.of(
                    AdvancedCrucibleBlockEntity::new,
                    ModBlocks.ADVANCED_CRUCIBLE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeAdvancedCrucibleBlockEntity>> LARGE_ADVANCED_CRUCIBLE = BLOCK_ENTITIES.register(
            "large_advanced_crucible",
            () -> BlockEntityType.Builder.of(
                    LargeAdvancedCrucibleBlockEntity::new,
                    ModBlocks.LARGE_ADVANCED_CRUCIBLE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireBarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register(
            "barrel",
            () -> BlockEntityType.Builder.of(
                    FireBarrelBlockEntity::new,
                    ModBlocks.FIRE_BARREL.get()
            ).build(null));
}
