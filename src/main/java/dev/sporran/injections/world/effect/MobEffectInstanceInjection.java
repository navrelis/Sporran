package dev.sporran.injections.world.effect;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCure;
import dev.sporran.util.SporranHelper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MobEffectInstanceInjection {
    default Set<EffectCure> neoforge$getCures() {
        throw SporranHelper.createMixinException(MobEffectInstanceInjection.class, "neoforge$getCures");
    }

    interface DetailsInjection {
        Optional<Set<EffectCure>> cures();
        void sporran$setCures(Optional<Set<EffectCure>> cures);
    }
}
