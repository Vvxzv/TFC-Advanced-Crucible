package net.vvxzv.tfcadvancedcrucible.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.CrucibleBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vvxzv.tfcadvancedcrucible.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class LargeCrucibleBlock extends CrucibleBlock {
    public LargeCrucibleBlock(ExtendedProperties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer) {
            level.getBlockEntity(pos, BlockEntities.LARGE_CRUCIBLE.get()).ifPresent((crucible) -> Helpers.openScreen(serverPlayer, crucible, crucible.getBlockPos()));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
