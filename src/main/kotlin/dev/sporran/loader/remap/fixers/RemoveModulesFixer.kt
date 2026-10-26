/*
 * Changed by the Sporran project on 2026-09-29; see docs/CHANGES.md.
 * Based on Kilt by KiltMC (https://github.com/KiltMC/Kilt). Licensed under the GNU LGPL 2.1.
 */

package dev.sporran.loader.remap.fixers

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

// Wraps all calls relating to "layer.findModule().orElseThrow()"
object RemoveModulesFixer {
    fun fixClass(classNode: ClassNode) {
        for (methodNode in classNode.methods) {
            val afterInsns = mutableListOf<AbstractInsnNode>()

            for (insnNode in methodNode.instructions) {
                if (insnNode is MethodInsnNode && insnNode.opcode == Opcodes.INVOKEVIRTUAL && insnNode.owner == "java/lang/ModuleLayer" && insnNode.name == "findModule") {
                    afterInsns.add(insnNode)
                }
            }

            for (insnNode in afterInsns) {
                methodNode.instructions.insert(insnNode, MethodInsnNode(Opcodes.INVOKESTATIC, "dev/sporran/util/SporranHelper", "resolveModuleToUnnamed", "(Ljava/util/Optional;)Ljava/util/Optional;", false))
            }
        }
    }
}
