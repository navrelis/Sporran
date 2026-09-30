package dev.sporran.injections.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

public interface BlockElementFaceInjection {
    static BlockElementFace create(@Nullable Direction cullForDirection, int tintIndex, String texture, BlockFaceUV uv, @Nullable ExtraFaceData faceData) {
        return create(cullForDirection, tintIndex, texture, uv, faceData, new MutableObject<>());
    }

    static BlockElementFace create(@Nullable Direction cullForDirection, int tintIndex, String texture, BlockFaceUV uv, @Nullable ExtraFaceData faceData, MutableObject<BlockElement> parent) {
        var face = new BlockElementFace(cullForDirection, tintIndex, texture, uv);
        face.sporran$setFaceData(faceData);
        face.sporran$setParent(parent);
        return face;
    }

    default MutableObject<BlockElement> parent() {
        throw SporranHelper.createMixinException(BlockElementFaceInjection.class, "parent");
    }

    default void sporran$setParent(MutableObject<BlockElement> parent) {
        throw SporranHelper.createMixinException(BlockElementFaceInjection.class, "sporran$setParent");
    }

    default ExtraFaceData faceData() {
        throw SporranHelper.createMixinException(BlockElementFaceInjection.class, "faceData");
    }

    default void sporran$setFaceData(ExtraFaceData faceData) {
        throw SporranHelper.createMixinException(BlockElementFaceInjection.class, "sporran$setFaceData");
    }
}
