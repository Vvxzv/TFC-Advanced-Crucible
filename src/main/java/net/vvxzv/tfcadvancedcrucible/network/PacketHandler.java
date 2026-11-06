package net.vvxzv.tfcadvancedcrucible.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;

import java.util.function.BiConsumer;

public class PacketHandler {
    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> type(String id) {
        return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, id));
    }

    public static void setup(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar register = event.registrar(ModList.get().getModFileById(TFCAdvancedCrucible.MODID).versionString());
        register.playToServer(PourFasterPacket.TYPE, PourFasterPacket.CODEC, onServer(PourFasterPacket::handle));
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> onServer(BiConsumer<T, ServerPlayer> handler) {
        return (payload, context) -> context.enqueueWork(() -> handler.accept(payload, (ServerPlayer)context.player()));
    }
}
