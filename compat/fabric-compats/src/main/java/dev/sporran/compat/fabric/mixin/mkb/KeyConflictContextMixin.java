/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mixin.mkb;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import committee.nova.mkb.keybinding.KeyConflictContext;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import dev.sporran.compat.fabric.mkb.MKBKeyConflictContextWrapper;

import java.util.HashMap;
import java.util.Map;

@IfModLoaded("mkb")
@Mixin(KeyConflictContext.class)
public abstract class KeyConflictContextMixin implements IKeyConflictContext {
    @Unique
    private static final Map<IKeyConflictContext, MKBKeyConflictContextWrapper> sporran$contextWrappers = new HashMap<>();

    @Override
    public boolean conflicts(IKeyConflictContext other) {
        return ((committee.nova.mkb.api.IKeyConflictContext) this).conflicts(sporran$contextWrappers.computeIfAbsent(other, MKBKeyConflictContextWrapper::new));
    }
}
