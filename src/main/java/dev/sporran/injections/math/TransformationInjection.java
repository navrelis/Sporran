package dev.sporran.injections.math;

import com.mojang.math.Transformation;
import org.joml.Matrix3f;
import dev.sporran.processor.FabricInjectedInterface;

@FabricInjectedInterface(Transformation.class)
public interface TransformationInjection {
    Matrix3f getNormalMatrix();
}
