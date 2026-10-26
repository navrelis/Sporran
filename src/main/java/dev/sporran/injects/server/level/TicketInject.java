/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.injects.server.level;

import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import org.spongepowered.asm.mixin.Mixin;
import dev.sporran.helpers.mixin.CreateInitializer;
import dev.sporran.injections.server.level.TicketInjection;

@Mixin(Ticket.class)
public abstract class TicketInject<T> implements TicketInjection {
    public TicketInject(TicketType<T> type, int ticketLevel, T key) {}

    @CreateInitializer
    public TicketInject(TicketType<T> type, int ticketLevel, T key, boolean forceTicks) {
        this(type, ticketLevel, key);
        this.sporran$setForceTicks(forceTicks);
    }

    // Handled by Porting Lib
}
