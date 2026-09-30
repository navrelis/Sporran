package dev.sporran.injections.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import dev.sporran.mixin.AttributeSupplierAccessor;
import dev.sporran.mixin.AttributeSupplierBuilderAccessor;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(AttributeSupplier.Builder.class)
public interface AttributeSupplierBuilderInjection {
    static AttributeSupplier.Builder create(AttributeSupplier attributeMap) {
        var builder = new AttributeSupplier.Builder();
        ((AttributeSupplierBuilderAccessor) builder).getBuilder().putAll(((AttributeSupplierAccessor) attributeMap).getInstances());

        return builder;
    }

    default void combine(AttributeSupplier.Builder other) {
        throw SporranHelper.createMixinException(AttributeSupplierBuilderInjection.class, "combine");
    }

    default boolean hasAttribute(Holder<Attribute> attribute) {
        throw SporranHelper.createMixinException(AttributeSupplierBuilderInjection.class, "hasAttribute");
    }
}
