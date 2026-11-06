package net.vvxzv.tfcadvancedcrucible.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.CrucibleBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.registry.BlockEntities;
import net.vvxzv.tfcadvancedcrucible.common.utils.TagKeys;
import org.jetbrains.annotations.NotNull;

public class LargeAdvancedCrucibleBlock extends CrucibleBlock {
    public LargeAdvancedCrucibleBlock(ExtendedProperties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if(hand == InteractionHand.MAIN_HAND) {
            if(stack.is(TagKeys.Items.ADJUSTMENT_TOOLS)) {
                if(level.getBlockEntity(pos) instanceof LargeAdvancedCrucibleBlockEntity crucible) {
                    if(crucible.getOutputFace() != hit.getDirection()) {
                        crucible.setOutputFace(hit.getDirection());
                    } else {
                        crucible.setOutputFace(null);
                    }

                    return ItemInteractionResult.SUCCESS;
                }
            }

            if (player instanceof ServerPlayer serverPlayer) {
                level.getBlockEntity(pos, BlockEntities.LARGE_ADVANCED_CRUCIBLE.get()).ifPresent((crucible) -> serverPlayer.openMenu(crucible, crucible.getBlockPos()));
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
