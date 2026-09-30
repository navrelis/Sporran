package dev.sporran.injections.client;

import net.minecraft.client.Camera;
import net.minecraft.world.level.block.state.BlockState;
import dev.sporran.processor.FabricInjectedInterface;
import dev.sporran.util.SporranHelper;

@FabricInjectedInterface(Camera.class)
public interface CameraInjection {
    default void setRotation(float yaw, float pitch, float roll) {
        throw SporranHelper.createMixinException(CameraInjection.class, "setRotation");
    }

    default float getRoll() {
        throw SporranHelper.createMixinException(CameraInjection.class, "getRoll");
    }

    default BlockState getBlockAtCamera() {
        throw SporranHelper.createMixinException(CameraInjection.class, "getBlockAtCamera");
    }
}
