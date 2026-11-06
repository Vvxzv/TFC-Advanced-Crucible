package net.vvxzv.tfcadvancedcrucible.compat.jade;

import net.vvxzv.tfcadvancedcrucible.common.block.AdvancedCrucibleBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.LargeAdvancedCrucibleBlock;
import net.vvxzv.tfcadvancedcrucible.common.block.LargeCrucibleBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeIntegration implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration reg) {
        reg.registerBlockComponent(AdvanceCrucibleComponentProvider.INSTANCE, AdvancedCrucibleBlock.class);
        reg.registerBlockComponent(LargeCrucibleComponentProvider.INSTANCE, LargeCrucibleBlock.class);
        reg.registerBlockComponent(LargeAdvanceCrucibleComponentProvider.INSTANCE, LargeAdvancedCrucibleBlock.class);
    }
}
