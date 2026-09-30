package dev.sporran.loader.asm.coremod

import org.objectweb.asm.tree.FieldInsnNode
import dev.sporran.loader.remap.SporranRemapper

open class RemappingFieldInsnNode(opcode: Int, owner: String, name: String, descriptor: String) : FieldInsnNode(opcode,
    SporranRemapper.remapClass(owner, ignoreWorkaround = true),
    name,
    SporranRemapper.remapDescriptor(descriptor)
) {
    constructor(original: FieldInsnNode) : this(original.opcode, original.owner, original.name, original.desc)
}
