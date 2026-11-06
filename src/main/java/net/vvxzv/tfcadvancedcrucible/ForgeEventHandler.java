package net.vvxzv.tfcadvancedcrucible;

import net.dries007.tfc.common.blockentities.CharcoalForgeBlockEntity;
import net.dries007.tfc.common.blocks.devices.CharcoalForgeBlock;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.vvxzv.tfcadvancedcrucible.common.registry.ModBlocks;
import net.vvxzv.tfcadvancedcrucible.common.utils.TagKeys;

public class ForgeEventHandler {

    public static void init() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ForgeEventHandler::onFireStart);
    }

    public static void onFireStart(StartFireEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (state.is(TagKeys.Blocks.CRUCIBLE) && CharcoalForgeBlock.isValid(level, pos.below()) && event.isStrong()) {
            BlockEntity entity = level.getBlockEntity(pos.below());
            if (entity instanceof CharcoalForgeBlockEntity forge) {
                if (forge.light(level.getBlockState(pos.below()))) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
