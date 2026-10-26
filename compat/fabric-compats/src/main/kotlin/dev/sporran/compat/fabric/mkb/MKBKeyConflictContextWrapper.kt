/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.compat.fabric.mkb

import net.neoforged.neoforge.client.settings.IKeyConflictContext
import committee.nova.mkb.api.IKeyConflictContext as MKBKeyConflictContext

class MKBKeyConflictContextWrapper(val wrapped: IKeyConflictContext) : MKBKeyConflictContext {
    override fun isActive(): Boolean {
        return wrapped.isActive
    }

    override fun conflicts(ctx: MKBKeyConflictContext): Boolean {
        return wrapped.conflicts(ctx as IKeyConflictContext)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MKBKeyConflictContextWrapper) {
            return this.wrapped == other.wrapped
        }

        return super.equals(other)
    }
}