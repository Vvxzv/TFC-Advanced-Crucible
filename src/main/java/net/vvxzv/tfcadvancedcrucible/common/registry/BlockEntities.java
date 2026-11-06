package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.FireBarrelBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TFCAdvancedCrucible.MODID);

    public static final RegistryObject<BlockEntityType<LargeCrucibleBlockEntity>> LARGE_CRUCIBLE = BLOCK_ENTITIES.register(
            "large_crucible",
            () -> BlockEntityType.Builder.of(
                    LargeCrucibleBlockEntity::new,
                    ModBlocks.LARGE_CRUCIBLE.get()
            ).build(null));

    public static final RegistryObject<BlockEntityType<AdvancedCrucibleBlockEntity>> ADVANCED_CRUCIBLE = BLOCK_ENTITIES.register(
            "advanced_crucible",
            () -> BlockEntityType.Builder.of(
                    AdvancedCrucibleBlockEntity::new,
                    ModBlocks.ADVANCED_CRUCIBLE.get()
            ).build(null));

    public static final RegistryObject<BlockEntityType<LargeAdvancedCrucibleBlockEntity>> LARGE_ADVANCED_CRUCIBLE = BLOCK_ENTITIES.register(
            "large_advanced_crucible",
            () -> BlockEntityType.Builder.of(
                    LargeAdvancedCrucibleBlockEntity::new,
                    ModBlocks.LARGE_ADVANCED_CRUCIBLE.get()
            ).build(null));

    public static final RegistryObject<BlockEntityType<FireBarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register(
            "barrel",
            () -> BlockEntityType.Builder.of(
                    FireBarrelBlockEntity::new,
                    ModBlocks.FIRE_BARREL.get()
            ).build(null));
}
