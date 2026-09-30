package dev.sporran.injections.client.renderer.texture;

import io.github.fabricators_of_create.porting_lib.extensions.client.AbstractTextureExtension;
import dev.sporran.util.SporranHelper;

public interface AbstractTextureInjection extends AbstractTextureExtension {
    default void setBlurMipmap(boolean blur, boolean mipmap) {
        throw SporranHelper.createMixinException(AbstractTextureInjection.class, "setBlurMipmap");
    }

    default void restoreLastBlurMipmap() {
        throw SporranHelper.createMixinException(AbstractTextureInjection.class, "restoreLastBlurMipmap");
    }
}
