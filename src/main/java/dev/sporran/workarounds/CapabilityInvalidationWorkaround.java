package dev.sporran.workarounds;

public interface CapabilityInvalidationWorkaround {
    default void sporran$invalidateCaps() {
        throw new IllegalStateException("this shouldn't happen");
    }
}
