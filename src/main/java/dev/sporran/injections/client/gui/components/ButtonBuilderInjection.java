/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.components;

import net.minecraft.client.gui.components.Button;

import java.util.function.Function;

public interface ButtonBuilderInjection {
    default Button build(Function<Button.Builder, Button> builder) {
        return builder.apply((Button.Builder) this);
    }
}
