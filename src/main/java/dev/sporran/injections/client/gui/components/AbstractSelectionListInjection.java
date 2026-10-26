/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injections.client.gui.components;

public interface AbstractSelectionListInjection {
    default int getWidth() {
        throw new IllegalStateException();
    }

    default int getHeight() {
        throw new IllegalStateException();
    }

    default int getTop() {
        throw new IllegalStateException();
    }

    default int getBottom() {
        throw new IllegalStateException();
    }

    default int getLeft() {
        throw new IllegalStateException();
    }

    default int getRight() {
        throw new IllegalStateException();
    }
}
