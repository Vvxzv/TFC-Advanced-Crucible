package net.vvxzv.tfcadvancedcrucible.common.registry;

import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcadvancedcrucible.TFCAdvancedCrucible;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.AdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeAdvancedCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.block.entity.LargeCrucibleBlockEntity;
import net.vvxzv.tfcadvancedcrucible.common.container.AdvancedCrucibleContainer;
import net.vvxzv.tfcadvancedcrucible.common.container.LargeAdvancedCrucibleContainer;
import net.vvxzv.tfcadvancedcrucible.common.container.LargeCrucibleContainer;

public class ContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, TFCAdvancedCrucible.MODID);

    public static final RegistryObject<MenuType<AdvancedCrucibleContainer>> ADVANCED_CRUCIBLE = RegistrationHelpers.<AdvancedCrucibleBlockEntity, AdvancedCrucibleContainer>registerBlockEntityContainer(CONTAINERS, "advanced_crucible", BlockEntities.ADVANCED_CRUCIBLE, AdvancedCrucibleContainer::create);

    public static final RegistryObject<MenuType<LargeCrucibleContainer>> LARGE_CRUCIBLE = RegistrationHelpers.<LargeCrucibleBlockEntity, LargeCrucibleContainer>registerBlockEntityContainer(CONTAINERS, "large_crucible", BlockEntities.LARGE_CRUCIBLE, LargeCrucibleContainer::create);

    public static final RegistryObject<MenuType<LargeAdvancedCrucibleContainer>> LARGE_ADVANCED_CRUCIBLE = RegistrationHelpers.<LargeAdvancedCrucibleBlockEntity, LargeAdvancedCrucibleContainer>registerBlockEntityContainer(CONTAINERS, "large_advanced_crucible", BlockEntities.LARGE_ADVANCED_CRUCIBLE, LargeAdvancedCrucibleContainer::create);
}
