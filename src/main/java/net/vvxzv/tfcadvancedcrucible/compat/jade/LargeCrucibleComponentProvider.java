package net.vvxzv.tfcadvancedcrucible.compat.jade;

import net.dries007.tfc.config.TFCConfig;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LargeCrucibleComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(blockAccessor.getBlockEntity() instanceof LargeCrucibleBlockEntity crucible) {
            float temperature = crucible.getTemperature();
            if(temperature > 2) {
                iTooltip.add(TFCConfig.CLIENT.heatTooltipStyle.get().formatColored((int)temperature));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(TFCAdvancedCrucible.MODID, "large_crucible");
    }
}
