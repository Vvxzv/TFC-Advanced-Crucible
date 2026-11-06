package net.vvxzv.tfcadvancedcrucible.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AbstractCrucibleBlockEntity;
import org.jetbrains.annotations.Nullable;

public record PourFasterPacket(BlockPos pos, int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PourFasterPacket> TYPE = PacketHandler.type("pour_faster");
    public static final StreamCodec<ByteBuf, PourFasterPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, (c) -> c.pos, ByteBufCodecs.VAR_INT, (c) -> c.slot, PourFasterPacket::new);

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
