package net.vvxzv.tfcadvancedcrucible.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketHandler {
    private static final String VERSION = ModList.get().getModFileById(TFCAdvancedCrucible.MODID).versionString();
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, "network"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );
    private static final MutableInt ID = new MutableInt(0);

    public static void send(PacketDistributor.PacketTarget target, Object message) {
        CHANNEL.send(target, message);
    }

    public static void init() {
        register(PourFasterPacket.class, PourFasterPacket::encode, PourFasterPacket::new, PourFasterPacket::handle);
    }

    private static <T> void register(Class<T> cls, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, ServerPlayer> handler) {
        CHANNEL.registerMessage(ID.getAndIncrement(), cls, encoder, decoder, (packet, context) -> {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> handler.accept(packet, context.get().getSender()));
        });
    }
}
