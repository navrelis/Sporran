/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.math;

import com.mojang.math.Transformation;
import org.joml.Matrix3f;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(Transformation.class)
public interface TransformationInjection {
    Matrix3f getNormalMatrix();
}
