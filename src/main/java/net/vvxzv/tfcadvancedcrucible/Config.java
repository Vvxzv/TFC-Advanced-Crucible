package net.vvxzv.tfcadvancedcrucible;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCAdvancedCrucible.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue ADVANCED_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a advanced crucible (in mB).").defineInRange("advancedCrucibleCapacity", 4000, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue LARGE_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a large crucible (in mB).").defineInRange("largeCrucibleCapacity", 16000, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue LARGE_ADVANCED_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a large advanced crucible (in mB).").defineInRange("largeAdvancedCrucibleCapacity", 16000, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int advancedCrucibleCapacity;
    public static int largeCrucibleCapacity;
    public static int largeAdvancedCrucibleCapacity;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        advancedCrucibleCapacity = ADVANCED_CRUCIBLE_CAPACITY.get();
        largeCrucibleCapacity = LARGE_CRUCIBLE_CAPACITY.get();
        largeAdvancedCrucibleCapacity= LARGE_ADVANCED_CRUCIBLE_CAPACITY.get();
    }
}
