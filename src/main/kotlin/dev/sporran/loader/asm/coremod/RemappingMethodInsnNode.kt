/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.asm.coremod

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode
import dev.sporran.loader.remap.SporranRemapper

open class RemappingMethodInsnNode(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean) : MethodInsnNode(opcode,
    SporranRemapper.remapClass(owner, ignoreWorkaround = true),
    name,
    SporranRemapper.remapDescriptor(descriptor), isInterface
) {
    constructor(opcode: Int, owner: String, name: String, descriptor: String) : this(opcode, owner, name, descriptor, opcode == Opcodes.INVOKEINTERFACE)
    constructor(original: MethodInsnNode) : this(original.opcode, original.owner, original.name, original.desc, original.itf)
}
