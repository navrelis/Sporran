package dev.sporran.injections;

import dev.sporran.util.SporranHelper;

public interface CrashReportCategoryInjection {
    default void applyStackTrace(Throwable trace) {
        throw new RuntimeException("mixin wtf");
    }

    default void setStackTrace(StackTraceElement[] stackTrace) {
        throw SporranHelper.createMixinException(CrashReportCategoryInjection.class, "setStackTrace");
    }
}
