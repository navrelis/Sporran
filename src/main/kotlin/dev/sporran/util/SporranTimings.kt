package dev.sporran.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dev.sporran.loader.SporranFlags

/**
 * Sporran: wall-clock timings of the translator's startup phases. Logged at DEBUG, or at INFO with `-Dsporran.printTimings=true`.
 */
object SporranTimings {
    @JvmField
    val logger: Logger = LoggerFactory.getLogger("Sporran Timings")

    @JvmStatic
    fun log(phase: String, nanos: Long) {
        if (!SporranFlags.PRINT_TIMINGS && !logger.isDebugEnabled)
            return

        val ms = "%.1f".format(nanos / 1_000_000.0)
        if (SporranFlags.PRINT_TIMINGS)
            logger.info("[timing] {} took {} ms", phase, ms)
        else
            logger.debug("[timing] {} took {} ms", phase, ms)
    }

    inline fun <T> time(phase: String, block: () -> T): T {
        val start = System.nanoTime()
        try {
            return block()
        } finally {
            log(phase, System.nanoTime() - start)
        }
    }
}
