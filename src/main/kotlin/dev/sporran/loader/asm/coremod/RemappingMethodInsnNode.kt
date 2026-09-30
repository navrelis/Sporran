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
