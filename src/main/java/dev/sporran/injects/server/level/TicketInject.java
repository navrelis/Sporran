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
