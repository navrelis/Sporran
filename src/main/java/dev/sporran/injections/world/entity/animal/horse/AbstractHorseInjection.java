package dev.sporran.injections.world.entity.animal.horse;

import net.minecraft.world.Container;
import dev.sporran.util.SporranHelper;

public interface AbstractHorseInjection {
    default Container getInventory() {
        throw SporranHelper.createMixinException(AbstractHorseInjection.class, "getInventory");
    }
}
