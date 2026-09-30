package dev.sporran.injections.server.level;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.Nullable;
import dev.sporran.util.SporranHelper;

import java.util.OptionalInt;
import java.util.function.Consumer;

public interface ServerPlayerInjection {
    default String getLanguage() {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "getLanguage");
    }

    default Component getTabListHeader() {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "getTabListHeader");
    }

    default void setTabListHeader(Component header) {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "setTabListHeader");
    }

    default Component getTabListFooter() {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "getTabListFooter");
    }

    default void setTabListFooter(Component footer) {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "setTabListFooter");
    }

    default void setTabListHeaderFooter(Component header, Component footer) {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "setTabListHeaderFooter");
    }

    default void refreshTabListName() {
        throw SporranHelper.createMixinException(ServerPlayerInjection.class, "refreshTabListName");
    }
}
