package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.items.TooltipBlockItem;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.block.AdvancedCrucibleBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.FireBarrelBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.LargeAdvancedCrucibleBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.LargeCrucibleBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TFCAdvancedCrucible.MODID);

    private static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory);
    }

    public static final DeferredHolder<Block, Block> ADVANCED_CRUCIBLE = register("advanced_crucible", () -> new AdvancedCrucibleBlock(ExtendedProperties.of(MapColor.METAL).strength(3.0F).sound(SoundType.METAL).blockEntity(BlockEntities.ADVANCED_CRUCIBLE).serverTicks(AdvancedCrucibleBlockEntity::serverTick)), block -> new TooltipBlockItem(block, new Item.Properties()));

    public static final DeferredHolder<Block, Block> LARGE_CRUCIBLE = register("large_crucible", () -> new LargeCrucibleBlock(ExtendedProperties.of(MapColor.METAL).strength(3.0F).sound(SoundType.METAL).blockEntity(BlockEntities.LARGE_CRUCIBLE).serverTicks(LargeCrucibleBlockEntity::serverTick)), block -> new TooltipBlockItem(block, new Item.Properties()));

    public static final DeferredHolder<Block, Block> LARGE_ADVANCED_CRUCIBLE = register("large_advanced_crucible", () -> new LargeAdvancedCrucibleBlock(ExtendedProperties.of(MapColor.METAL).strength(3.0F).sound(SoundType.METAL).blockEntity(BlockEntities.LARGE_ADVANCED_CRUCIBLE).serverTicks(LargeAdvancedCrucibleBlockEntity::serverTick)), block -> new TooltipBlockItem(block, new Item.Properties()));

    public static final DeferredHolder<Block, Block> FIRE_BARREL = register("fire_barrel", () -> new FireBarrelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASS).strength(4.5F).sound(SoundType.STONE).ignitedByLava().requiresCorrectToolForDrops()), block -> new BlockItem(block, new Item.Properties()));
}
