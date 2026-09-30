package dev.sporran.compat.fabric.mixin.snowrealmagic;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import snownee.snow.block.SnowVariant;

@IfModLoaded("snowrealmagic")
@Mixin(SnowVariant.class)
public interface SnowVariantMixin extends IBlockExtension {
}
