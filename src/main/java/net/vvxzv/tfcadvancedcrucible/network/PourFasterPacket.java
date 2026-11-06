package net.vvxzv.tfcadvancedcrucible.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AbstractCrucibleBlockEntity;
import org.jetbrains.annotations.Nullable;

public record PourFasterPacket(BlockPos pos, int slot) {
    PourFasterPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readVarInt());
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeVarInt(this.slot);
    }

    void handle(@Nullable ServerPlayer player) {
        if (player != null) {
            Level level = player.level();
            if (level.isLoaded(this.pos)) {
                if (level.getBlockEntity(this.pos) instanceof AbstractCrucibleBlockEntity<?> crucible) {
                    crucible.setFastPouring(this.slot);
                }
            }
        }
    }
}
