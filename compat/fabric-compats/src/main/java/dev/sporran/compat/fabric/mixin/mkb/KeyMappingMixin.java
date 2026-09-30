package dev.sporran.compat.fabric.mixin.mkb;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import committee.nova.mkb.ModernKeyBinding;
import committee.nova.mkb.api.IKeyBinding;
import committee.nova.mkb.api.IKeyConflictContext;
import committee.nova.mkb.keybinding.KeyBindingMap;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.extensions.IKeyMappingExtension;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.sporran.compat.fabric.mkb.MKBKeyConflictContextWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IfModLoaded("mkb")
@Mixin(value = KeyMapping.class, priority = 1010)
public abstract class KeyMappingMixin implements IKeyBinding {
    @Shadow
    private InputConstants.Key key;
    @Unique private static final KeyBindingMap sporran$keybindingMap = new KeyBindingMap();
    @Unique private static final Map<net.neoforged.neoforge.client.settings.IKeyConflictContext, MKBKeyConflictContextWrapper> sporran$contextWrappers = new HashMap<>();

    // Sporran: bridge MKB and Forge contexts
    @Override
    public IKeyConflictContext getKeyConflictContext() {
        var originalContext = ((IKeyMappingExtension) this).getKeyConflictContext();

        if (originalContext instanceof IKeyConflictContext keyConflictContext)
            return keyConflictContext;

        return sporran$contextWrappers.computeIfAbsent(originalContext, MKBKeyConflictContextWrapper::new);
    }

    @Override
    public void setKeyConflictContext(IKeyConflictContext iKeyConflictContext) {
        ((IKeyMappingExtension) this).setKeyConflictContext((net.neoforged.neoforge.client.settings.IKeyConflictContext) iKeyConflictContext);
    }

    @Override
    public committee.nova.mkb.keybinding.KeyModifier getKeyModifier() {
        return committee.nova.mkb.keybinding.KeyModifier.valueFromString(((IKeyMappingExtension) this).getKeyModifier().name());
    }

    @Override
    public committee.nova.mkb.keybinding.KeyModifier getKeyModifierDefault() {
        return committee.nova.mkb.keybinding.KeyModifier.valueFromString(((IKeyMappingExtension) this).getDefaultKeyModifier().name());
    }

    @Override
    public void setKeyModifierAndCode(committee.nova.mkb.keybinding.KeyModifier keyModifier, InputConstants.Key key) {
        ((IKeyMappingExtension) this).setKeyModifierAndCode(KeyModifier.valueFromString(keyModifier.name()), key);
    }

    @Override
    public InputConstants.Key getKey() {
        return this.key;
    }

    @Override
    public void press() {
        // Sporran: no need to increment, we're wrapping the operation.
    }

    // Sporran: Reimplement everything MKB was doing, but with improved compatibility.
    @TargetHandler(mixin = "dev.sporran.injects.client.KeyMappingInject", name = "setKeyModifierAndCode")
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/settings/KeyMappingLookup;remove(Lnet/minecraft/client/KeyMapping;)V"))
    private void sporran$mkb$removeFromMkbMap(KeyModifier keyModifier, InputConstants.Key keyCode, CallbackInfo ci) {
        sporran$keybindingMap.removeKey((KeyMapping) (Object) this);
    }

    @TargetHandler(mixin = "dev.sporran.injects.client.KeyMappingInject", name = "setKeyModifierAndCode")
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/settings/KeyMappingLookup;put(Lcom/mojang/blaze3d/platform/InputConstants$Key;Lnet/minecraft/client/KeyMapping;)V"))
    private void sporran$mkb$addToMkbMap(KeyModifier keyModifier, InputConstants.Key keyCode, CallbackInfo ci) {
        sporran$keybindingMap.addKey(keyCode, (KeyMapping) (Object) this);
    }

    @Inject(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At("TAIL"))
    private void sporran$mkb$addToMkbMap(String name, InputConstants.Type type, int keyCode, String category, CallbackInfo ci) {
        sporran$keybindingMap.addKey(this.key, (KeyMapping) (Object) this);
    }

    @Inject(method = "resetMapping", at = @At("HEAD"))
    private static void sporran$mkb$updateMkbMappings(CallbackInfo ci) {
        sporran$keybindingMap.clearMap();
    }

    @Inject(method = "resetMapping", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static void sporran$mkb$registerMappingToMkbMap(CallbackInfo ci, @Local KeyMapping mapping) {
        sporran$keybindingMap.addKey(((IKeyBinding) mapping).getKey(), mapping);
    }

    @TargetHandler(mixin = "dev.sporran.injects.client.KeyMappingInject", name = "sporran$wrapKeyClick")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/settings/KeyMappingLookup;getAll(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Ljava/util/List;"))
    private static List<KeyMapping> sporran$mkb$addMkbKeys(List<KeyMapping> original, @Local(argsOnly = true) InputConstants.Key key) {
        if (ModernKeyBinding.nonConflictKeys()) {
            var existing = sporran$keybindingMap.lookupActives(key);
            original.removeAll(existing);
            original.addAll(existing);
        } else {
            var keyBinding = sporran$keybindingMap.lookupActive(key);
            if (keyBinding != null && !original.contains(keyBinding))
                original.add(keyBinding);
        }

        return original;
    }

    @TargetHandler(mixin = "dev.sporran.injects.client.KeyMappingInject", name = "sporran$wrapKeySet")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/settings/KeyMappingLookup;getAll(Lcom/mojang/blaze3d/platform/InputConstants$Key;)Ljava/util/List;"))
    private static List<KeyMapping> sporran$mkb$addMkbKeysToSet(List<KeyMapping> original, @Local(argsOnly = true) InputConstants.Key key) {
        if (ModernKeyBinding.nonConflictKeys()) {
            var existing = sporran$keybindingMap.lookupActives(key);
            original.removeAll(existing);
            original.addAll(sporran$keybindingMap.lookupActives(key));
        } else {
            var keyBinding = sporran$keybindingMap.lookupActive(key);
            if (keyBinding != null && !original.contains(keyBinding))
                original.add(keyBinding);
        }

        return original;
    }

    @Inject(method = "click", at = @At(value = "FIELD", target = "Lnet/minecraft/client/KeyMapping;clickCount:I", ordinal = 0))
    private static void sporran$mkb$callMkbClick(InputConstants.Key key, CallbackInfo ci, @Local KeyMapping keyMapping) {
        ((IKeyBinding) keyMapping).press();
    }
}
