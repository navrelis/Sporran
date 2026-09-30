// TRACKED HASH: 39b2eb34331b57077e911b6c5e5ab0553ca207cf
package dev.sporran.injects.world.entity.ai.attributes;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.world.entity.ai.attributes.AttributeSupplierBuilderInjection;
import dev.sporran.mixin.AttributeSupplierAccessor;
import dev.sporran.mixin.AttributeSupplierBuilderAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeSupplierInject {
    @Mixin(AttributeSupplier.Builder.class)
    public static abstract class BuilderInject implements AttributeSupplierBuilderInjection {
        @Shadow
        @Final
        private ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder;

        private List<AttributeSupplier.Builder> others = new ArrayList<>();

        @Override
        public void combine(AttributeSupplier.Builder other) {
            this.builder.putAll(((AttributeSupplierBuilderAccessor) other).getBuilder().build());
            others.add(other);
        }

        @Override
        public boolean hasAttribute(Holder<Attribute> attribute) {
            return this.builder.build().containsKey(attribute);
        }

        @Inject(at = @At("TAIL"), method = "build")
        public void sporran$build(CallbackInfoReturnable<AttributeSupplier> cir) {
            for (AttributeSupplier.Builder other : others) {
                ((AttributeSupplierBuilderAccessor) other).setInstanceFrozen(true);
            }
        }

        public BuilderInject() {}

        @CreateInitializer
        public BuilderInject(AttributeSupplier attributeMap) {
            this();

            this.builder.putAll(((AttributeSupplierAccessor) attributeMap).getInstances());
        }
    }

}