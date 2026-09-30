// TRACKED HASH: 988ae85739fcd12e564f775b34a9e6306e31f01f
package dev.sporran.injects.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import dev.sporran.injections.client.CameraInjection;
import dev.sporran.util.SporranHelper;

@Mixin(Camera.class)
public abstract class CameraInject implements CameraInjection {
    @Shadow private float yRot;
    @Shadow private float xRot;
    @Shadow private boolean initialized;
    @Shadow private BlockGetter level;
    @Shadow @Final private BlockPos.MutableBlockPos blockPosition;
    @Shadow private Vec3 position;
    @Shadow protected abstract void setRotation(float yRot, float xRot);

    @Unique private float sporran$roll;

    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0))
    private void sporran$handleCameraAngleSetup(Camera instance, float yRot, float xRot, Operation<Void> original, @Local(argsOnly = true) float partialTick) {
        var cameraSetup = NeoForge.EVENT_BUS.post(new ViewportEvent.ComputeCameraAngles(instance, partialTick, yRot, xRot, 0));

        if (SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), Camera.class, "setRotation", float.class, float.class, float.class)) {
            instance.setRotation(cameraSetup.getYaw(), cameraSetup.getPitch(), cameraSetup.getRoll());
        } else {
            this.sporran$roll = cameraSetup.getRoll();
            original.call(instance, cameraSetup.getYaw(), cameraSetup.getPitch());
        }
    }

    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 1))
    private void sporran$handleThirdPersonReverse(Camera instance, float yRot, float xRot, Operation<Void> original, @Local(argsOnly = true) float partialTick) {
        if (SporranHelper.INSTANCE.hasMethodOverride(this.getClass(), Camera.class, "setRotation", float.class, float.class, float.class)) {
            this.setRotation(yRot, xRot, -this.sporran$roll);
        } else {
            this.sporran$roll = -this.sporran$roll;
            original.call(instance, yRot, xRot);
        }
    }

    @Definition(id = "getMaxZoom", method = "Lnet/minecraft/client/Camera;getMaxZoom(F)F")
    @Expression("-this.getMaxZoom(@(4.0) * ?)")
    @ModifyExpressionValue(method = "setup", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float sporran$tryGetDetachedCameraDistance(float original, @Local(argsOnly = true, ordinal = 1) boolean thirdPersonReverse, @Local(ordinal = 1) float scale) {
        return ClientHooks.getDetachedCameraDistance((Camera) (Object) this, thirdPersonReverse, scale, original);
    }

    @Override
    public void setRotation(float yaw, float pitch, float roll) {
        this.sporran$roll = roll;
        this.setRotation(yaw, pitch);
    }

    @ModifyArg(method = "setRotation", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"), index = 2)
    private float sporran$addRollToRotation(float angleY) {
        return angleY + this.sporran$roll;
    }

    @Override
    public float getRoll() {
        return this.sporran$roll;
    }

    @Override
    public BlockState getBlockAtCamera() {
        if (!this.initialized)
            return Blocks.AIR.defaultBlockState();
        else
            return this.level.getBlockState(this.blockPosition).getStateAtViewpoint(this.level, this.blockPosition, this.position);
    }
}