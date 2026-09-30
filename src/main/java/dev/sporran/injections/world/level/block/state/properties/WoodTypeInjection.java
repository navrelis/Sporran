package dev.sporran.injections.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import dev.sporran.mixin.WoodTypeAccessor;

public interface WoodTypeInjection {
    static WoodType create(String name, BlockSetType setType) {
        return WoodTypeAccessor.createWoodType(name, setType);
    }
}
