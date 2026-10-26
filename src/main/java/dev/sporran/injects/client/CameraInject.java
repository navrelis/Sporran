// TRACKED HASH: 988ae85739fcd12e564f775b34a9e6306e31f01f
/*
 * Changed by the Sporran project between 2026-09-29 and 2026-10-08; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

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
import dev.sporran.util.CameraRoll;
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

    // Sporran: runs in every frame, resolve the override check only once.
    @Unique private static final SporranHelper.MethodOverrideCheck sporran$SET_ROTATION_OVERRIDE = SporranHelper.MethodOverrideCheck.of(Camera.class, "setRotation", float.class, float.class, float.class);

    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0))
    private void sporran$handleCameraAngleSetup(Camera instance, float yRot, float xRot, Operation<Void> original, @Local(argsOnly = true) float partialTick) {
        var cameraSetup = NeoForge.EVENT_BUS.post(new ViewportEvent.ComputeCameraAngles(instance, partialTick, yRot, xRot, 0));

        if (sporran$SET_ROTATION_OVERRIDE.test(this.getClass())) {
            instance.setRotation(cameraSetup.getYaw(), cameraSetup.getPitch(), cameraSetup.getRoll());
        } else {
            this.sporran$roll = cameraSetup.getRoll();
            original.call(instance, cameraSetup.getYaw(), cameraSetup.getPitch());
        }
    }

    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 1))
    private void sporran$handleThirdPersonReverse(Camera instance, float yRot, float xRot, Operation<Void> original, @Local(argsOnly = true) float partialTick) {
        if (sporran$SET_ROTATION_OVERRIDE.test(this.getClass())) {
            this.setRotation(yRot, xRot, -this.sporran$roll);
        } else {
            this.sporran$roll = -this.sporran$roll;
            original.call(instance, yRot, xRot);
        }
    }

    // Sporran: NeoForge's two-argument setRotation hands on a roll of 0, so the sleeping view does not keep the roll of the event.
    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 2))
    private void sporran$handleSleepingView(Camera instance, float yRot, float xRot, Operation<Void> original) {
        if (sporran$SET_ROTATION_OVERRIDE.test(this.getClass())) {
            this.setRotation(yRot, xRot, 0.0F);
        } else {
            this.sporran$roll = 0.0F;
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

    // Sporran: the roll is in degrees, the argument is in radians, and NeoForge passes -roll * (PI / 180) (see CameraRoll).
    @ModifyArg(method = "setRotation", at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"), index = 2)
    private float sporran$addRollToRotation(float angleZ) {
        return CameraRoll.addRoll(angleZ, this.sporran$roll);
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