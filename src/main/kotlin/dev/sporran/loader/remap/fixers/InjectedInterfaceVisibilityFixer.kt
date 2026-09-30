package dev.sporran.loader.remap.fixers

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import dev.sporran.loader.remap.SporranRemapper

object InjectedInterfaceVisibilityFixer {
    private val methodsToFix = listOf("getCustomMapData" to SporranRemapper.remapDescriptor("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)"))
    fun fixClass(classNode: ClassNode) {
        for (methodNode in classNode.methods) {
            for (method in methodsToFix) {
                if (methodNode.name == method.first && methodNode.desc.contains(method.second)) {
                    methodNode.access = Opcodes.ACC_PUBLIC
                }
            }
        }
    }
}
