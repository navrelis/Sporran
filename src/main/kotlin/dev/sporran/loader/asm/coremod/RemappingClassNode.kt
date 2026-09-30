package dev.sporran.loader.asm.coremod

import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
import dev.sporran.loader.remap.SporranEnhancedRemapper
import dev.sporran.loader.remap.SporranRemapper

class RemappingClassNode(api: Int = Opcodes.ASM9, private val remapper: SporranEnhancedRemapper) : ClassNode(api) {
    init {
        this.fields = TransformingList(this.fields) {
            FieldNode(Opcodes.ASM9, it.access, remapper.mapFieldName(this.name, it.name, it.desc), SporranRemapper.remapDescriptor(it.desc), it.signature, it.value)
        }

        this.methods = TransformingList(this.methods) {
            MethodNode(Opcodes.ASM9, it.access, remapper.mapMethodName(this.name, it.name, it.desc), SporranRemapper.remapDescriptor(it.desc), it.signature, it.exceptions.toTypedArray())
                .apply {
                    RemappingInsnList(it.instructions)
                }
        }
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?
    ): FieldVisitor? {
        return super.visitField(access, remapper.mapFieldName(this.name, name, descriptor), descriptor, signature, value)
    }
}
