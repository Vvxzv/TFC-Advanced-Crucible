package net.vvxzv.tfcadvancedcrucible;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TFCAdvancedCrucible.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue ADVANCED_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a advanced crucible (in mB).").defineInRange("advancedCrucibleCapacity", 4000, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue LARGE_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a large crucible (in mB).").defineInRange("largeCrucibleCapacity", 16000, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue LARGE_ADVANCED_CRUCIBLE_CAPACITY = BUILDER.comment("Tank capacity of a large advanced crucible (in mB).").defineInRange("largeAdvancedCrucibleCapacity", 16000, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

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
